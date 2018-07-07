package edu.buaa.rse.dotx.agent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.log4j.PropertyConfigurator;

public class AgentMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    try {
			initLog();
			start();
			while (true){
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void start(){
		Agent.getInstance().start();
	}
	
	private static void initLog(){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
		Properties prts = new Properties();
		try {
			prts.load(new FileInputStream("config/log4j.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		PropertyConfigurator.configure(prts);
	}
}
