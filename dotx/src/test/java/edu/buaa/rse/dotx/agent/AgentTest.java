package edu.buaa.rse.dotx.agent;

import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.Connection;
import edu.buaa.rse.dotx.model.Feature;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.task.Task;
import edu.buaa.rse.dotx.xstream.XstreamAdaptor;

public class AgentTest {

	public static void main(String[] args) {
		//genTestData();
		runTask();
	}
	
	private static void runTask(){
		Agent.getInstance();
		Task task = new Task();
		task.setId("1000");
		task.initForAgent();
	}

	
	private static void genTestData(){
		SystemModel sys = new SystemModel("imasys");
		Component comp1 = new Component("comp1");
		Feature f1= new Feature("feat1");
		Feature f2= new Feature("feat2");
		comp1.addFeature(f1);
		Component comp2 = new Component("comp2");
		comp2.addFeature(f2);
		Connection con1 = new Connection("con1");
		Connection con2 = new Connection("con2");
		con1.setSource(f1);
		con1.setDestination(f2);
		con2.setSource(f2);
		con2.setDestination(f1);
		sys.addComponent(comp1).addComponent(comp2);
		sys.addConnection(con1).addConnection(con2);
		
		Component comp3 = new Component("comp3");
		comp3.setCategory(Component.Category.PARTION);	
		Component comp4 = new Component("comp4");
		comp4.setCategory(Component.Category.PARTION);	
		Component comp5 = new Component("comp5");
		comp5.setCategory(Component.Category.APP);	
		Component comp6 = new Component("comp6");
		comp6.setCategory(Component.Category.APP);
		sys.addComponent(comp3).addComponent(comp4).addComponent(comp5).addComponent(comp6);

		XstreamAdaptor.getInstance().objectToFile(sys, "temp"+"/"+"model.xml");
	}
}
