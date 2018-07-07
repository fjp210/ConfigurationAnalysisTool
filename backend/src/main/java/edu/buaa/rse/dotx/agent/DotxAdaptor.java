package edu.buaa.rse.dotx.agent;

import edu.buaa.rse.dotx.ftp.FtpAdaptor;
import edu.buaa.rse.dotx.json.JsonAdaptor;
import edu.buaa.rse.dotx.queue.Queue;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import edu.buaa.rse.dotx.backend.util.ServletUtil;
import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.dbmodel.TaskModel;
import edu.buaa.rse.dotx.dbmodel.util.HibernateUtils;

public class DotxAdaptor {
	public static DotxAdaptor _instance;
	
	public static DotxAdaptor getInstance(){
		if(_instance==null){
			synchronized(DotxAdaptor.class){
				if(_instance == null){
					DotxAdaptor temp = new DotxAdaptor();
					_instance = temp;
				}
			}
		}
		return _instance;
	}
	
	public String push(String taskPath){
		String tempFile = this.pushFile(taskPath);
		
		Session session = HibernateUtils.getSeesion();
		session.getTransaction().begin();
		TaskModel task = new TaskModel();
		task.setStatus(0);
		task.setTempDir(tempFile);
		session.persist(task);
		session.getTransaction().commit();
		Integer taskId = task.getId();
		String taskIdStr = taskId.toString();
		
		this.pushZk(taskIdStr);
		return taskIdStr;
	}
	
	public String pull(String taskId){
		String path = ServletUtil.getServletContext().getRealPath("/temp");
		System.out.println("--------temppath is:"+path);
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		FtpAdaptor.getDotxInstance().downloadDir2Dir(DotxRuntime.Ftp.TASKPATH+"/"+taskId, path);
		JsonElement r = JsonAdaptor.getInstance().fileToObject(path+"/"+taskId+"/result.json");
		return r.toString();
	}
	
	public Map temp_status(String taskId){
		Integer id = Integer.valueOf(taskId);
		Session session = HibernateUtils.getSeesion();
		session.getTransaction().begin();
		TaskModel task = TaskModel.getTask(session, id);
		Map<String, Integer> status = task.getJobStatus();
		session.getTransaction().commit();
		return status;
	}
	
	public Map status(String taskId){
		ZkAdaptor zk = ZkAdaptor.getDotxInstance();
		String taskPath = DotxRuntime.Zk.TASKPATH+"/"+taskId;
		List<String> jobs = zk.getChildren(taskPath+"/joblist");
		int total = jobs.size();
		int inited = 0;
		int running = 0;
		int finshed = 0;
		for(int i=0; i<total; i++){
			String path = jobs.get(i);
			String status = zk.getData(path+"/status");
			if("0".equals(status))inited++;
			if("1".equals(status))running++;
			if("2".equals(status))finshed++;
		}
		Map status = new HashMap();
		status.put("inited", inited);
		status.put("running", running);
		status.put("finished", finshed);
		status.put("total", total);
		return status;
	}
	
	private String pushFile(String basePath){
		String tempDir = String.valueOf(System.currentTimeMillis());
		FtpAdaptor.getDotxInstance().uploadDir(basePath, DotxRuntime.Ftp.STAGEPATH,tempDir);
		return DotxRuntime.Ftp.STAGEPATH+"/"+tempDir+"/target";
	}
	
	private void pushZk(String taskPath){
		Queue.getBackendInstance().pushTask(taskPath);
	}
	
	public String getCurrentTaskId(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ZkAdaptor zk = ZkAdaptor.getDotxInstance();
		List<String> paths = zk.getChildren(DotxRuntime.Zk.TASKPATH);
		int id = 1;
		for(int i=0; i<paths.size(); i++){
			String taskId = zk.getData(paths.get(i)+"/id");
			int temp = Integer.valueOf(taskId).intValue();
			if(temp>id){
				id=temp;
			}
		}
		System.out.println("----taskid is-------"+id);
		return String.valueOf(id);
	}
	
}
