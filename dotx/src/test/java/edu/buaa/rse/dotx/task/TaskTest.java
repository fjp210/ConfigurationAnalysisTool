package edu.buaa.rse.dotx.task;

import java.util.List;

import edu.buaa.rse.dotx.job.Job;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.model.deploy.AppToPartion;
import edu.buaa.rse.dotx.xstream.XstreamAdaptor;
import edu.buaa.rse.dotx.zk.ZkAdaptor;

public class TaskTest {
	public static void main(String[] args) {
		int pSize=6,aSize=16;
		long totalDeloySize = (long) Math.pow(pSize,aSize);
		long oneDeploySize = (long) Math.pow(pSize,aSize-1);
		
		long maxDeploySize = 470184984574L;
		long maxJobSize = 100;
		if(oneDeploySize > maxDeploySize && maxDeploySize*maxJobSize>totalDeloySize ){
			oneDeploySize = maxDeploySize;
		}
		System.out.println("DeplaySize"+oneDeploySize);
		System.out.println("totalDeloySize"+totalDeloySize);
		System.out.println("JobSize"+(totalDeloySize/oneDeploySize+1));

		
		for(int i=0; (i)*oneDeploySize<totalDeloySize; i++){
			AppToPartion ap = new AppToPartion();
			long begin = i*oneDeploySize;
			long end = (i+1)*oneDeploySize-1;
			if(end > totalDeloySize){
				end = totalDeloySize-1;
			}
			System.out.println("Begein:"+begin+" End:"+end);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static void test1(){
		String agentDir = "/Users/zhangquan07/Documents/workspace_aadl/backend/src/main/webapp/temp";
		SystemModel software = (SystemModel) XstreamAdaptor.getInstance().fileToObject(agentDir+"/"+"softwareModel.xml");
		SystemModel hardware = (SystemModel) XstreamAdaptor.getInstance().fileToObject(agentDir+"/"+"hardwareModel.xml");
		List<Component> apps = software.getComponentsByCategory(Component.Category.APP);
		List<Component> partions = hardware.getComponentsByCategory(Component.Category.PARTION);
		System.out.println(apps.size());
		System.out.println(partions.size());
	}
}
