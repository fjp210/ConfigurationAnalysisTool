package edu.buaa.rse.dotx.queue;

import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.log4j.Logger;

import edu.buaa.rse.dotx.job.JobConsumer;
import edu.buaa.rse.dotx.task.TaskConsumer;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

public class Queue {
	private DistributedQueue<String> _jobQueue;
	private DistributedQueue<String> _taskQueue;
	private static Queue _queueInstance;
	protected Logger logger = Logger.getLogger(this.getClass());
	public static Queue getAgentInstance(){
		if(_queueInstance==null){
			synchronized(Queue.class){
				if(_queueInstance == null){
					Queue temp = new Queue();
					temp.initForAgent();
					_queueInstance = temp;
				}
			}
		}
		return _queueInstance;
	}
	
	public static Queue getWorkerInstance(){
		if(_queueInstance==null){
			synchronized(Queue.class){
				if(_queueInstance == null){
					Queue temp = new Queue();
					temp.initForWorker();
					_queueInstance = temp;
				}
			}
		}
		return _queueInstance;
	}
	
	public static Queue getBackendInstance(){
		if(_queueInstance==null){
			synchronized(Queue.class){
				if(_queueInstance == null){
					Queue temp = new Queue();
					temp.initForBackend();
					_queueInstance = temp;
				}
			}
		}
		return _queueInstance;
	}
	
	private void initForAgent(){
		try {
			QueueBuilder<String> builder = QueueBuilder.builder(
					ZkAdaptor.getDotxInstance()._client,
					null, 
					new QueueSerializer<String>(){
						public byte[] serialize(String item){
							return item.getBytes();
						}
					    public String deserialize(byte[] bytes){
					    	return new String(bytes);
					    }
					},
					"/jobqueue");
			DistributedQueue<String> queue = builder.buildQueue();
			queue.start();
			this._jobQueue = queue;
			//builder.lockPath("/jobqueue");
			
			
			QueueBuilder<String> builder2 = QueueBuilder.builder(
					ZkAdaptor.getDotxInstance()._client,
					new TaskConsumer(), 
					new QueueSerializer<String>(){
						public byte[] serialize(String item){
							return item.getBytes();
						}
					    public String deserialize(byte[] bytes){
					    	return new String(bytes);
					    }
					},
					"/taskqueue");
			DistributedQueue<String> queue2 = builder2.buildQueue();
			queue2.start();
			this._taskQueue = queue2;
			//builder.lockPath("/taskqueue");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initForBackend(){
		try {
			QueueBuilder<String> builder = QueueBuilder.builder(
					ZkAdaptor.getDotxInstance()._client,
					null, 
					new QueueSerializer<String>(){
						public byte[] serialize(String item){
							return item.getBytes();
						}
					    public String deserialize(byte[] bytes){
					    	return new String(bytes);
					    }
					},
					"/taskqueue");
			DistributedQueue<String> queue = builder.buildQueue();
			queue.start();
			this._taskQueue = queue;
			//builder.lockPath("/taskqueue");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initForWorker(){
		try {
			QueueBuilder<String> builder = QueueBuilder.builder(
					ZkAdaptor.getDotxInstance()._client,
					new JobConsumer(), 
					new QueueSerializer<String>(){
						public byte[] serialize(String item){
							return item.getBytes();
						}
					    public String deserialize(byte[] bytes){
					    	return new String(bytes);
					    }
					},
					"/jobqueue");
			DistributedQueue<String> queue = builder.buildQueue();
			queue.start();
			this._jobQueue = queue;
			//builder.lockPath("/jobqueue");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pushJob(String jobPath){
		try {
			this._jobQueue.put(jobPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pushTask(String taskPath){
		try {
			this._taskQueue.put(taskPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getTaskId(){
		return null;
	}
	
}
