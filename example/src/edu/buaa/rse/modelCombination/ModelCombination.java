package edu.buaa.rse.modelCombination;

import java.io.*;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.buaa.rse.dotx.model.util.RuntimeConstants;
import edu.buaa.rse.dotx.model.*;

public class ModelCombination {
	
	public static void main(String[] args){
		String hardwareXml = "E:/615software/temp/input/hardwareModel.xml";
		String softwareXml = "E:/615software/temp/input/softwareModel.xml";
		//generateSystem(hardwareXml, softwareXml);
		//System.out.println("It's over!!");
	}
	
	public static SystemModel generateSystem(String hadrwareXml, String softwareXml, String URL){
		
		File hardwareFile = new File(hadrwareXml);
		File softwareFile = new File(softwareXml);
		SystemModel hardwareModel = new SystemModel("hardwareSystem");
		SystemModel softwareModel = new SystemModel("softwareSystem");
		try{
			//读取硬件模型的xml文件，生成硬件systemModel
			FileInputStream hardwareStream = new FileInputStream(hardwareFile);
			XStream hardwareXStream = new XStream(new DomDriver());
			XStream.setupDefaultSecurity(hardwareXStream);
			Class<?>[] classes1 = new Class[] { SystemModel.class, Property.class, Feature.class, Element.class, Connection.class, Component.class};
			hardwareXStream.allowTypes(classes1);
			hardwareXStream.autodetectAnnotations(true);
			hardwareXStream.alias("system", SystemModel.class);
			hardwareModel = (SystemModel) hardwareXStream.fromXML(hardwareStream);
			hardwareStream.close();
			
			//读取软件模型的xml文件，生成软件systemModel
			FileInputStream softwareStream = new FileInputStream(softwareFile);
			XStream softwareXStream = new XStream(new DomDriver());
			XStream.setupDefaultSecurity(softwareXStream);
			Class<?>[] classes2 = new Class[] { SystemModel.class, Property.class, Feature.class, Element.class, Connection.class, Component.class};
			softwareXStream.allowTypes(classes2);
			softwareXStream.autodetectAnnotations(true);
			softwareXStream.alias("system", SystemModel.class);
			softwareModel = (SystemModel) hardwareXStream.fromXML(softwareStream);
			softwareStream.close();

			
			
			
		}catch (Exception e){
			e.printStackTrace();
		}	
		//将hardwareModel和softwareModel合成一个模型
		//List<Connection> softwareConnectionList = softwareModel.getConnections();
		List<Component> softwareComponentList = softwareModel.getComponents();
		//List<Connection> hardwareConnectionList = hardwareModel.getConnections();
		List<Component> hardwareComponentList = hardwareModel.getComponents();
		int hardwareComponentListSize = hardwareComponentList.size();
		for (Component softwareComponent : softwareComponentList){
			softwareComponent.setComponentId(softwareComponent.getComponentId() + hardwareComponentListSize);
			hardwareModel.addComponent(softwareComponent);
		}
		//将hardwareModel转化为一个xml文件
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(URL + "//systemModel.xml");
			XStream x = new XStream(new DomDriver());
			XStream.setupDefaultSecurity(x);
			Class<?>[] classes = new Class[] { SystemModel.class, edu.buaa.rse.dotx.model.Property.class, Feature.class, Element.class, Connection.class, Component.class};
			x.allowTypes(classes);
			x.autodetectAnnotations(true);
//			test
			//Feature featurehello = new Feature("helloworld");
			x.toXML(hardwareModel, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//forTest 测试这个xml文件是否还可以转化为systemModel
//		File inputFile = new File(RuntimeConstants.inputPath+"systemModel.xml");
//		SystemModel systemModel = null;
//		try {
//			FileInputStream modelStream = new FileInputStream(inputFile);
//			XStream modelXStream = new XStream(new DomDriver());
//			XStream.setupDefaultSecurity(modelXStream);
//			Class<?>[] classes2 = new Class[] { SystemModel.class, Property.class, Feature.class, Element.class, Connection.class, Component.class};
//			modelXStream.allowTypes(classes2);
//			modelXStream.autodetectAnnotations(true);
//			modelXStream.alias("system", SystemModel.class);
//			systemModel = (SystemModel) modelXStream.fromXML(modelStream);
//			modelStream.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return hardwareModel;
	}
}
