package edu.buaa.rse.dotx.xstream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;

import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.model.deploy.Deploy;

public class XstreamAdaptor {
	private static XstreamAdaptor _instance;
	
	public static XstreamAdaptor getInstance(){
		if(XstreamAdaptor._instance==null){
			synchronized(XstreamAdaptor.class){
				if(XstreamAdaptor._instance == null){
					XstreamAdaptor instance = new XstreamAdaptor();
					XstreamAdaptor._instance = instance;
				}
			}
		}
		return XstreamAdaptor._instance;
    }
	
	public String objectToFile(Object object, String filePath){
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			XStream x = new XStream();
			x.autodetectAnnotations(true);
			x.toXML(object,fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}
	
	public Object fileToObject(String filePath){
		FileInputStream fis = null;
		Object object = null;
		try {
			fis = new FileInputStream(filePath);
			XStream x = new XStream();
			x.autodetectAnnotations(true);
			x.alias("system", SystemModel.class);
			x.alias("deploy", Deploy.class);
			object = x.fromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
}
