package edu.buaa.rse.dotx.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.buaa.rse.dotx.model.util.RuntimeConstants;

public class TestMain {
	public static void main(String[] args) {
		saveSystemToFile();
		getSystFromFile();
	} 
	
	public static void saveSystemToFile(){
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
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(RuntimeConstants.inputPath + "test.xml");
			XStream x = new XStream(new DomDriver());
			XStream.setupDefaultSecurity(x);
			Class<?>[] classes = new Class[] { SystemModel.class, Property.class, Feature.class, Element.class, Connection.class, Component.class};
			x.allowTypes(classes);
			x.autodetectAnnotations(true);
			x.toXML(sys,fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//sys是系统模型对象
	public static void getSystFromFile(){
		File inputFile = new File(RuntimeConstants.inputPath+"test.xml");
		FileInputStream fis = null;
		SystemModel sys = null;
		try {
			fis = new FileInputStream(inputFile);
			XStream x = new XStream(new DomDriver());
			XStream.setupDefaultSecurity(x);
			Class<?>[] classes = new Class[] { SystemModel.class, Property.class, Feature.class, Element.class, Connection.class, Component.class};
			x.allowTypes(classes);
			x.autodetectAnnotations(true);
			x.alias("system", SystemModel.class);
			sys = (SystemModel) x.fromXML(fis);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(RuntimeConstants.inputPath + "test2.xml");
			//http://x-stream.github.io/tutorial.html, Xtream use use a standard JAXP DOM parser instead of 
			XStream x = new XStream(new DomDriver());
			//test, solve problem "Security framework of XStream not initialized, XStream is probably vulnerable"
			XStream.setupDefaultSecurity(x);
			Class<?>[] classes = new Class[] { SystemModel.class, Property.class, Feature.class, Element.class, Connection.class, Component.class};
			x.allowTypes(classes);
			//
			x.autodetectAnnotations(true);
			x.toXML(sys,fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
