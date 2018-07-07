package edu.buaa.rse.dotx.job;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;

import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.zk.ZKException;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

public class JobConsumer implements QueueConsumer<String>{
	private String jobId;
	private String taskId;
	private ZkAdaptor zk;
	
	public void stateChanged(CuratorFramework client, ConnectionState conState) {
		// TODO Auto-generated method stub
	}
	
	public void consumeMessage(String jobpath){
		this.zk = ZkAdaptor.getDotxInstance();
		this.jobId = this.zk.getData(jobpath+"/id");
		this.taskId = this.zk.getData(jobpath+"/taskid");
		this.zk.setData(DotxRuntime.Zk.TASKPATH+"/"+this.taskId+"/joblist/"+jobId+"/status", DotxRuntime.Zk.JOBRUNNING);
		System.out.println("consume started: "+jobpath);
		this.consume();
		System.out.println("consume finished: "+jobpath);
		this.zk.setData(DotxRuntime.Zk.TASKPATH+"/"+this.taskId+"/joblist/"+jobId+"/status", DotxRuntime.Zk.JOBFINISHED);
	}
	
	private void consume(){
		Job job = new Job();
		job.setId(this.jobId);
		job.pull();
		job.run();
	}
	
}
