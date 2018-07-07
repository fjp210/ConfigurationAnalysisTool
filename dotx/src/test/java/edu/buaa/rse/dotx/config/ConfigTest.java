package edu.buaa.rse.dotx.config;

public class ConfigTest {

	public static void main(String[] args) {
		Config.getInstance();
		String aa = Config.getInstance().get("agent.maxjob");
		long bb = Long.parseLong(aa);
		
	}

}
