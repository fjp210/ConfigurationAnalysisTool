package edu.buaa.rse.dotx.job;

public class JobTest {

	public static void main(String[] args) {
		Job job = new Job();
		job.setId("1000");
		
		//job.agentInit();
		job.pull();
		job.run();
	}

}
