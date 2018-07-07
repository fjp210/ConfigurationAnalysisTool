package edu.buaa.rse.dotx.task;

import java.util.List;

import org.hibernate.Session;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.buaa.rse.dotx.agent.Agent;
import edu.buaa.rse.dotx.config.Config;
import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.dbmodel.JobModel;
import edu.buaa.rse.dotx.dbmodel.JobResultModel;
import edu.buaa.rse.dotx.dbmodel.TaskModel;
import edu.buaa.rse.dotx.dbmodel.TaskResultModel;
import edu.buaa.rse.dotx.dbmodel.util.HibernateUtils;
import edu.buaa.rse.dotx.ftp.FtpAdaptor;
import edu.buaa.rse.dotx.job.Job;
import edu.buaa.rse.dotx.json.JsonAdaptor;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.model.deploy.AppToPartion;
import edu.buaa.rse.dotx.model.deploy.Deploy;
import edu.buaa.rse.dotx.xstream.XstreamAdaptor;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

public class Task {
	String id;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAgentDir() {
		return DotxRuntime.Agent.TASKPATH+"/"+this.id;
	}
	public String getFtpDir() {
		return DotxRuntime.Ftp.TASKPATH+"/"+this.id;
	}
	public String getWorkerDir() {
		return DotxRuntime.Worker.TASKPATH+"/"+this.id;
	}
	public String getZkPath() {
		return DotxRuntime.Zk.TASKPATH+"/"+this.id;
	}
	
	public void initForAgent(){
		this.agentPush();
		this.mapToJob();
	}
	
	public void initForWorker(){
		this.workerPull();
	}
	
	private void agentPush(){
		ZkAdaptor zk = ZkAdaptor.getDotxInstance();
		String taskPath = this.getZkPath();
		zk.create(taskPath);
		zk.setData(taskPath+"/id", this.id);
		FtpAdaptor.getDotxInstance().uploadDir2Dir(this.getAgentDir(), DotxRuntime.Ftp.TASKPATH);
	}
	
	private void workerPull(){
		FtpAdaptor.getDotxInstance().downloadDir2Dir(this.getFtpDir(), DotxRuntime.Worker.TASKPATH);
	}
	
	public void mapToJob(){
		Session session = HibernateUtils.getSeesion();
		session.getTransaction().begin();
		Integer taskId = Integer.valueOf(this.getId());
		TaskModel taskModel = TaskModel.getTask(session, taskId);
		
		String agentDir = this.getAgentDir();
		SystemModel software = (SystemModel) XstreamAdaptor.getInstance().fileToObject(agentDir+"/"+"softwareModel.xml");
		SystemModel hardware = (SystemModel) XstreamAdaptor.getInstance().fileToObject(agentDir+"/"+"hardwareModel.xml");
		List<Component> apps = software.getComponentsByCategory(Component.Category.APP);
		List<Component> partions = hardware.getComponentsByCategory(Component.Category.PARTION);
		int aSize = apps.size();
		int pSize = partions.size();
//		long totalDeloySize = (long) Math.pow(pSize,aSize);
//		long oneDeploySize = (long) Math.pow(pSize,aSize-1);
		
		//====================================
		int zhishu = aSize-2;
		long totalDeloySize = 0;
		if (pSize > 2) {
			for(int xishu=1; xishu < pSize-1 && zhishu >= 0; xishu++) {
				if (xishu == pSize-2) {
					xishu++;
				}
				long tempDeploySize = xishu * (long)Math.pow(pSize, zhishu);
				System.out.println("test1: " + tempDeploySize);
				totalDeloySize += tempDeploySize;
				zhishu--;			
			}
		}else {
			totalDeloySize = (long) Math.pow(pSize,aSize);
		}
		
		long oneDeploySize  = totalDeloySize/pSize;
		System.out.println("test: " + totalDeloySize);
		//=====================================
		long maxDeploySize = 1000000L;
		long maxJobSize = 100;
		long maxResultSize = 1000;
		try{
			String maxJob = Config.getInstance().get("agent.maxjob");
			maxJobSize = Long.parseLong(maxJob);
		}catch(Exception e){
			System.out.println("agent.maxjob is not given, use the default maxjob:"+maxJobSize);
		}
		try{
			String maxDeploy = Config.getInstance().get("agent.maxdeploy");
			maxDeploySize = Long.parseLong(maxDeploy);
		}catch(Exception e){
			System.out.println("agent.maxdeploy is not given, use the default maxdeploay:"+maxDeploySize);
		}
		try{
			String maxResult = Config.getInstance().get("worker.maxresult");
			maxResultSize = Long.parseLong(maxResult);
		}catch(Exception e){
			System.out.println("worker.maxresult is not given, use the default maxresult:"+maxResultSize);
		}
							//100Эђ						//10вк
//		if(oneDeploySize > maxDeploySize && maxDeploySize*maxJobSize>totalDeloySize ){
//			oneDeploySize = maxDeploySize;
//		}
		ZkAdaptor zk = ZkAdaptor.getDotxInstance();
		String taskPath = this.getZkPath();
		for(int i=0; (i)*oneDeploySize<totalDeloySize; i++){
			AppToPartion ap = new AppToPartion();
			long begin = i*oneDeploySize;
			long end = (i+1)*oneDeploySize-1;
			if(end > totalDeloySize){
				end = totalDeloySize-1;
			}
			ap.setBegin(begin);
			ap.setEnd(end);
			ap.setMaxresult(maxResultSize);
			Deploy deploy = new Deploy();
			deploy.setAppToPartion(ap);
			
			JobModel jobModel = new JobModel();
			jobModel.setTask(taskModel);
			jobModel.setStatus(2);
			session.persist(jobModel);
			
			Job job = new Job();
			//job.setId(Agent.getInstance().getNewJobId());
			job.setId(jobModel.getId().toString());
			job.setTaskId(this.id);
			job.setDeploy(deploy);
			job.push();
			zk.create(taskPath+"/joblist/"+job.getId());
			zk.setData(taskPath+"/joblist/"+job.getId()+"/id",job.getId());
		}
		zk.setChildUpdateWatcher(taskPath+"/joblist", new TaskStatusCallback());
		
		session.getTransaction().commit();
	}
	
	public void reduce(){
		Session session = HibernateUtils.getSeesion();
		session.getTransaction().begin();
		
		ZkAdaptor zk = ZkAdaptor.getDotxInstance();
		List<String> jobList = zk.getChildren(this.getZkPath()+"/joblist");
		int total = jobList.size();
		JsonArray allResult = new JsonArray();
		for(int i=0; i<total; i++){
			String path = jobList.get(i);
			String jobId = zk.getData(path+"/id");
			Job job = new Job();
			job.setId(jobId);
			job.setTaskId(this.getZkPath());
			JsonElement result = job.getResult();
			
			Integer id = Integer.valueOf(jobId);
			JobModel jobModel = JobModel.getJob(session, id);
			JobResultModel jobResult = new JobResultModel();
			jobResult.setJob(jobModel);
			jobResult.setResult(result.toString());
			session.persist(jobResult);
			
			allResult.addAll(result.getAsJsonArray());
		}
		JsonAdaptor.getInstance().objectToFile(allResult, this.getAgentDir()+"/result.json");
		FtpAdaptor.getDotxInstance().uploadDir2Dir(this.getAgentDir(), DotxRuntime.Ftp.TASKPATH);
		
		Integer taskId = Integer.valueOf(this.getId());
		TaskModel taskModel = TaskModel.getTask(session, taskId);
		TaskResultModel taskResult = new TaskResultModel();
		taskResult.setTask(taskModel);
		taskResult.setResult(allResult.toString());
		session.persist(taskResult);
		
		session.getTransaction().commit();
	}
	
	public void getResult(){
		
	}
	
}
