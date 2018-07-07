package edu.buaa.rse.dotx.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class Config {
	protected static Config _instance;
	protected Properties properties;
	protected String configBaseDir;
	protected int envFlag = 0 ;//0:web, 1:agent/worker
	
    public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public String getConfigBaseDir(){
		return this.configBaseDir;
	}
	public static Config getInstance(){
		if(Config._instance==null){
			synchronized(Config.class){
				if(Config._instance == null){
					Config instance = new Config();
					String configFilePath = null;
					try {
						Class<?> threadClazz = Class.forName("edu.buaa.rse.dotx.backend.util.ServletUtil");
						Method method = threadClazz.getMethod("getConfigDir");
						String configDir = (String) method.invoke(null);
						configFilePath = configDir + "";
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println("class edu.buaa.rse.dotx.backend.util.ServletUtil is not eixted.");
						configFilePath = (new File("config")).getAbsolutePath()+"";
					}
					instance.configBaseDir = configFilePath;
					instance.load();
					Config._instance = instance;
				}
			}
		}
		return Config._instance;
    }
    public void load(){
    	Properties prts = new Properties();
		try {
			prts.load(new FileInputStream(this.getConfigBaseDir()+"/config.properties"));
			this.properties = prts;
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public String get(String key){
    	String value = (String) this.properties.get(key);
    	return value;
    }
    
}
