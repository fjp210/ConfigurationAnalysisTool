package edu.buaa.rse.dotx.worker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.PropertyConfigurator;

public class WorkerMain {
	
	public static void main(String[] args) {
		initLog();
		Worker worker = Worker.getInstance();
		try {
			worker.work();
			while (true){
			    Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
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