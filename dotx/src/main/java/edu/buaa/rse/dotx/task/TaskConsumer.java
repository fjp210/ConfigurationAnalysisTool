package edu.buaa.rse.dotx.task;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;
import org.hibernate.Session;

import edu.buaa.rse.dotx.agent.Agent;
import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.dbmodel.TaskModel;
import edu.buaa.rse.dotx.dbmodel.util.HibernateUtils;
import edu.buaa.rse.dotx.ftp.FtpAdaptor;
import edu.buaa.rse.dotx.job.Job;
import edu.buaa.rse.dotx.zk.ZKException;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

public class TaskConsumer implements QueueConsumer<String>{

	public void stateChanged(CuratorFramework client, ConnectionState conState) {
		// TODO Auto-generated method stub
	}
	public void consumeMessage(String taskIdStr){
		Integer id = Integer.valueOf(taskIdStr);
		Session session = HibernateUtils.getSeesion();
		session.getTransaction().begin();
		TaskModel taskModel = TaskModel.getTask(session, id);
		String tempDir = taskModel.getTempDir();
		session.getTransaction().commit();
		
		
		//String taskId = Agent.getInstance().getNewTaskId();
		FtpAdaptor.getDotxInstance().downloadDir(tempDir, DotxRuntime.Agent.TASKPATH, taskIdStr);
		Task task = new Task();
		task.setId(taskIdStr);
		task.initForAgent();
	}

}
