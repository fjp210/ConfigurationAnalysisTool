
package edu.buaa.rse.dotx.zk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.data.Stat;

import edu.buaa.rse.dotx.config.Config;
import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.ftp.FtpAdaptor;

import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

/**
 * zookerper java API operations for MTC
 */ 

public class ZkAdaptor {
    
	private static ZkAdaptor _instance;
    private String _zkAddr = "";
    public CuratorFramework _client;
    private static final Logger logger=Logger.getLogger(FtpAdaptor.class.getName());
    private int retryTimes =10;
    private int timeout =30000;
    /**
     * default constructor
     */
    public ZkAdaptor() {

    }
    /**
     * constructor
     * @param zkAddr: format (ip:port)
     * @throws IOException 
     * @throws ConfigureException 
     * @throws NumberFormatException 
     *
     */
    public ZkAdaptor(String zkAddr){
    	this.retryTimes=Integer.parseInt(Config.getInstance().get("zk.retry.times"));
    	this.timeout=Integer.parseInt(Config.getInstance().get("zk.timeout"));
        this._zkAddr = zkAddr;
        this._client = CuratorFrameworkFactory.newClient(this._zkAddr, new RetryNTimes(retryTimes, timeout));
        logger.info("default data: "+CuratorFrameworkFactory.builder().getDefaultData());
        this._client.start();
        logger.info("get new zk adaptor success , timeout: "+this.timeout+" retry times : "+this.retryTimes);
        
    }
    
    public static ZkAdaptor getDotxInstance(){
		if(_instance==null){
			synchronized(ZkAdaptor.class){
				if(_instance == null){
					String zkhost = Config.getInstance().get("zk.host");
					ZkAdaptor _instance_tmp = new ZkAdaptor(zkhost);
					_instance = _instance_tmp;
				}
			}
		}
		return _instance;
    }

    public void setData(String path, String data) {
        try {
        	if(!exists(path)){
    			this.create(path);
    		}
			this._client.setData().forPath(path,data.getBytes());
		} catch (Exception e) {
			new ZKException("set data failed for path "+path,e);
		}
    }
    
    public void setData(String path,String data, int version) throws ZKException{
    	try {
    		if(!exists(path)){
    			this.create(path);
    		}
			this._client.setData().forPath(path,data.getBytes()).setVersion(version);
		} catch (Exception e) {
			 throw new ZKException("set data failed for path "+path,e);
		}
    	
    }
    
    public String getData(String path){
    	try {
    	       return new String(this._client.getData().forPath(path));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public String  getData(String path, Stat stat){
    	try {
 	       return new String(this._client.getData().storingStatIn(stat).forPath(path));
 	    }catch(Exception e){
 		   e.printStackTrace();
 	    }
    	return null;
    }
    
    public List<String> getChildren(String path)  {
    	try {
    		List<String> temp = this._client.getChildren().forPath(path);	
    		List<String> result = new ArrayList<String>();
    		for(int i=0; i<temp.size(); i++){
    			result.add(path+"/"+temp.get(i));
    		}
    		return result;
    	}catch(Exception e){
    	     logger.error("get children failed for path "+path,e);	
    	     return new ArrayList<String>();
    	}
    }
    
    public boolean exists(String path) {
    	try {
             if (this._client.checkExists().forPath(path) != null ) {
           	     return true;
             }
             else {
            	 return false; 
             }
        }catch(Exception e){
             logger.error("zk error ",e);	
             return false;
        }
    }
    
    public String create(String path){
    	try {
    		String[] pathLevel = path.split("/");
    		String tempPath = "";
    		int level = pathLevel.length;
    		for(int i=0; i<level-1; i++){
    			tempPath = tempPath + "/" + pathLevel[i+1];
    			if(!exists(tempPath)){
    				this._client.create().forPath(tempPath,"".getBytes());
    			}
    		}
    		return path;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public void remove(String path){
        try {
			this._client.delete().deletingChildrenIfNeeded().forPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}	
    }
    
    public PathChildrenCache setChildAddedWatcher(final String path,final WatchCallBack callback){
        PathChildrenCache cache = new PathChildrenCache(this._client,path,true); 
        try {
			cache.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	cache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event){
                        if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                        	callback.run(path);
                        	if (event.getData()!= null) {
                                callback.run(event.getData().getPath());
                            } else {
                                callback.fail();
                            }
                        }
                    }
                });
    	return cache;
    }
    
    public PathChildrenCache setChildUpdateWatcher(final String path,final WatchCallBack callback){
        PathChildrenCache cache = new PathChildrenCache(this._client,path,true); 
        try {
			cache.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
        List<String> childrenPath = this.getChildren(path);
        for(int i=0; i<childrenPath.size(); i++){
        	String onePath = childrenPath.get(i);
        	List<String> cc = this.getChildren(onePath);
        	if(cc.size()>0){
        		this.setChildUpdateWatcher(onePath, callback);
        	}
        }
    	cache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event){
                    	System.out.println("-----------------");
                    	System.out.println(path+" has changed, type is "+event.getType());
                        if (  event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED ) {
                        	callback.run(event.getData().getPath());
                            if (event.getData()!= null) {
                            	String value = new String ( event.getData().getData());
                            	if ( ! value.trim().equals("")){
                                     callback.run(event.getData().getPath(),value);
                            	}else {
                            		 logger.info("node changed ,but value is null path is :"+event.getData().getPath());
                            	}
                            } else {
                                callback.fail();
                            }
                        }
                    }
                });
    	return cache;
    }
    
    public NodeCache setValueChangedWatcher(final String path,final WatchCallBack callback) throws Exception{
    	NodeCache cache = new NodeCache(this._client,path,true); 
        cache.start();
    	cache.getListenable().addListener(
                new NodeCacheListener() {
                    public void nodeChanged() throws Exception {
                    	callback.run(path);
                    }
                });
    	return cache;
    }
    
    public DistributedAtomicLong createCounter(String path){
    	return new DistributedAtomicLong(this._client,path,null);
    }
 
}
