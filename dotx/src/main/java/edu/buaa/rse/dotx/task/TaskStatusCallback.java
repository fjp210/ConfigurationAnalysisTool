package edu.buaa.rse.dotx.task;

import java.io.File;
import java.util.List;

import edu.buaa.rse.dotx.zk.WatchCallBack;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

public class TaskStatusCallback implements WatchCallBack{

	public void run(String temppath) {
		if(temppath.indexOf("status")<0 || temppath.indexOf("joblist")<0){
			return;
		}
		temppath = temppath.substring(0, temppath.lastIndexOf("joblist")+7);
		ZkAdaptor zk = ZkAdaptor.getDotxInstance();
		List<String> pathList = zk.getChildren(temppath);
		System.out.println("=="+temppath+"===");
		int total = pathList.size();
		for(int i=0; i<total; i++){
			String path = pathList.get(i);
			String status = zk.getData(path+"/status");
			System.out.println("status of "+path+" is: "+status);
			if(!"2".equals(status)){
				return;
			}
		}
		System.out.println("----------start to reduce--------");
		String taskPath = temppath.substring(0, temppath.lastIndexOf("/"));
		String taskId = zk.getData(taskPath+"/id");
		Task task = new Task();
		task.setId(taskId);
		task.reduce();
	}

	public void fail() {
		// TODO Auto-generated method stub
		
	}

	public void run(String path, String value) {
		// TODO Auto-generated method stub
		
	}

}
