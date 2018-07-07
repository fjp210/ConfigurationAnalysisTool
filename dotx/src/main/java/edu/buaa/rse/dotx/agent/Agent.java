package edu.buaa.rse.dotx.agent;

import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.log4j.Logger;

import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.queue.Queue;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

public class Agent {
	private DistributedAtomicLong taskCounter;
	private DistributedAtomicLong jobCounter;
	private Queue agentQueue;
	private static Agent _instance;
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public Queue getJobQueue() {
		return agentQueue;
	}

	public static Agent getInstance(){
		if(_instance==null){
			synchronized(Agent.class){
				if(_instance == null){
					Agent temp = new Agent();
					temp.init();
					_instance = temp;
				}
			}
		}
		return _instance;
	}
	
	public void start(){
		//1.getfile form gui to temp file dir;
		//2.init task dir
	}
	
	private void init(){
		try {
			ZkAdaptor zk = ZkAdaptor.getDotxInstance();
			zk.create(DotxRuntime.Zk.TASKPATH);
			zk.create(DotxRuntime.Zk.JOBPATH);
			this.taskCounter = zk.createCounter(DotxRuntime.Zk.TASKCOUNTOERPATH);
			this.jobCounter = zk.createCounter(DotxRuntime.Zk.JOBCOUNTOERPATH);
			this.agentQueue = Queue.getAgentInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getNewTaskId(){
		String res = "";
		try {
			this.taskCounter.increment();
			int id = this.taskCounter.get().postValue().intValue();
			res = String.valueOf(id);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String getNewJobId(){
		String res = "";
		try {
			this.jobCounter.increment();
			int id = this.jobCounter.get().postValue().intValue();
			res = String.valueOf(id);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
}
