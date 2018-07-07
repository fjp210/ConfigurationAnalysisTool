package edu.buaa.rse.dotx.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thoughtworks.xstream.XStream;

import edu.buaa.rse.dotx.agent.Agent;
import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.ftp.FtpAdaptor;
import edu.buaa.rse.dotx.json.JsonAdaptor;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.Connection;
import edu.buaa.rse.dotx.model.Feature;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.model.deploy.AppToPartion;
import edu.buaa.rse.dotx.model.deploy.Deploy;
import edu.buaa.rse.dotx.task.Task;
import edu.buaa.rse.dotx.worker.digester.Digester;
import edu.buaa.rse.dotx.worker.digester.util.DigesterFactory;
import edu.buaa.rse.dotx.xstream.XstreamAdaptor;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

public class Job {
	protected String id;
	protected String taskId;
	protected Task task;
	
	SystemModel model;
	Deploy deploy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public SystemModel getModel() {
		return model;
	}

	public void setModel(SystemModel model) {
		this.model = model;
	}

	public Deploy getDeploy() {
		return deploy;
	}

	public void setDeploy(Deploy deploy) {
		this.deploy = deploy;
	}

	public String getAgentDir() {
		return DotxRuntime.Agent.JOBPATH+"/"+this.id;
	}
	public String getFtpDir() {
		return DotxRuntime.Ftp.JOBPATH+"/"+this.id;
	}
	public String getWorkerDir() {
		return DotxRuntime.Worker.JOBPATH+"/"+this.id;
	}
	public String getZkPath() {
		return DotxRuntime.Zk.JOBPATH+"/"+this.id;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * 1.register to zk node
	 * 2.upload file to ftp
	 */
	public void push(){
		XstreamAdaptor.getInstance().objectToFile(this.getDeploy(), this.getAgentDir()+"/"+"deploy.xml");
		this.agentPush();
	}
	
	/**
	 * 1.get files from ftp to worker dir
	 */
	public void pull(){
		this.workerPull();
	}
	
	public void run(){
		List<Digester> digesterList = DigesterFactory.getAllDigesters();
		for(int i=0; i<digesterList.size(); i++){
			Digester digester = digesterList.get(i);
			digester.setJobPath(this.getWorkerDir());
			digester.setBasePath(this.getTask().getWorkerDir());
			JsonElement result = digester.degest();
			this.saveResult(result);
		}
	}
	
	public void saveResult(JsonElement result){
		JsonAdaptor.getInstance().objectToFile(result, this.getWorkerDir()+"/result.json");
		FtpAdaptor.getDotxInstance().uploadDir2Dir(this.getWorkerDir(), DotxRuntime.Ftp.JOBPATH);
	}
	
	public JsonElement getResult(){
		FtpAdaptor.getDotxInstance().downloadDir2Dir(this.getFtpDir(), DotxRuntime.Agent.JOBPATH);
		JsonElement result = JsonAdaptor.getInstance().fileToObject(this.getAgentDir()+"/result.json");
		return result;
	}
	
	private void agentPush(){
		ZkAdaptor zk = ZkAdaptor.getDotxInstance();
		zk.create(this.getZkPath());
		zk.setData(this.getZkPath()+"/id", this.getId());
		zk.setData(this.getZkPath()+"/taskid", this.getTaskId());
		zk.setData(DotxRuntime.Zk.TASKPATH+"/"+this.taskId+"/joblist/"+this.getId()+"/status", DotxRuntime.Zk.JOBINIT);
		FtpAdaptor.getDotxInstance().uploadDir2Dir(this.getAgentDir(), DotxRuntime.Ftp.JOBPATH);
		Agent.getInstance().getJobQueue().pushJob(this.getZkPath());
	}
		
	private void workerPull(){
		FtpAdaptor.getDotxInstance().downloadDir2Dir(this.getFtpDir(), DotxRuntime.Worker.JOBPATH);
		ZkAdaptor zk = ZkAdaptor.getDotxInstance();
		String taskId = zk.getData(this.getZkPath()+"/taskid");
		Task task = new Task();
		task.setId(taskId);
		task.initForWorker();
		this.task = task;
	}

}
