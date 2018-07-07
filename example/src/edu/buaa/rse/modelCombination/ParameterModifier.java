package edu.buaa.rse.modelCombination;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.Connection;
import edu.buaa.rse.dotx.model.Feature;
import edu.buaa.rse.dotx.model.Property;
import edu.buaa.rse.dotx.model.SystemModel;
/**
 * 
 * @author Li Yaonan 
 * @description the main object of this class is to modify the model XML, specifically
 */
// TO DO Unit test
public class ParameterModifier {
	String modelXMLPath;
	String componentinfoXMLPath;
	String connectionInfoXML;
	public ParameterModifier(String modelXMLPath, String componentinfoXMLPath, String connectionInfoXML){
		this.modelXMLPath = modelXMLPath;
		this.componentinfoXMLPath = componentinfoXMLPath;
		this.connectionInfoXML = connectionInfoXML;
	}
	public void modifyComponent(){
		
		File infoFile = new File(this.componentinfoXMLPath);
		
		SystemModel systemModel = null;
		FileInputStream modelStream = null;
		FileInputStream infoStream = null;
		try {

			systemModel = TransformUtility.xML2SystemModel(this.modelXMLPath);
			
//			reading info from info file
			infoStream = new FileInputStream(infoFile);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(infoStream);
			org.dom4j.Element rootElement = doc.getRootElement();
//			element below is component tag
			
			//component included here corresponds to the component from info XML
			List<Component> componentsModel = systemModel.getComponents();
			List<org.dom4j.Element> infoRootList = rootElement.elements();
//			这两个list中的元素的数目可能不一样，所以，首先要让componentModel中的元素的个数迁就infoComponent中的元素
//			situation:componentsModel size more than infoComponents size
			if(componentsModel.size() > infoRootList.size()){
				System.out.println("the number of components in the model is bigger than it from info ");
				
			}else if(componentsModel.size() < infoRootList.size()){
				System.out.println("the number of components in the model is smaller than it from info ");
			}
			
			Iterator<Component> modelIter = componentsModel.iterator();
			Iterator<org.dom4j.Element> infoIter = infoRootList.iterator();
			
			while(modelIter.hasNext() && infoIter.hasNext()){
//				componentInfo means a component tag
//				the info from the two corresponds to each other
				org.dom4j.Element componentInfo = infoIter.next();
				Component eachComponent = modelIter.next();
				
//				
				List<Property> properties = eachComponent.properties;
				List<org.dom4j.Element> infos = componentInfo.elements();
//				思路是将infos存成一个以名字为键的hash表，针对每一个需要的Property，在这个hash表中去找到相应的信息，就好了
				HashMap<String, org.dom4j.Element> infosDict = new HashMap<String, org.dom4j.Element>();
				for(org.dom4j.Element eachInfo : infos){
					infosDict.put(eachInfo.getName(), eachInfo);
				}
				
				for(Property eachProperty:properties){
					if("Reliability".equals(eachProperty.key)){
						eachProperty.value = infosDict.get("basicReliability").getText().trim();
					}else if("RunTime".equals(eachProperty.key)){
						eachProperty.value = infosDict.get("basicExecutingTime").getText().trim();
					}else if("InitialNode".equals(eachProperty.key)){
						eachProperty.value = infosDict.get("initialMark").getText().trim().equals("True")?"1":"0";
					}else {
						eachProperty.value = "-1";
					}
				}
				
				
				if("PROCESS".equals(eachComponent.getCategory())){
					for(Property eachProperty:properties){
						if("PartTimeChipSize".equals(eachProperty.key)){
							eachProperty.value = infosDict.get("PartTimeChipSize").getText().trim();
						}else if("ProcessPeriod".equals(eachProperty.key)){
							eachProperty.value = infosDict.get("ProcessPeriod").getText().trim();
						}else if("ProcessDeadlineTime".equals(eachProperty.key)){
							eachProperty.value = infosDict.get("ProcessDeadlineTime").getText().trim();
						}
					}

				}

				
				if("PROCESSOR".equals(eachComponent.getCategory())){
					for(Property eachProperty:properties){
						if("ProcessorPolicy".equals(eachProperty.key)){
							eachProperty.value = infosDict.get("ProcessorPolicy").getText().trim();
						}else if("PartTimeChipSize".equals(eachProperty.key)){
							eachProperty.value = infosDict.get("PartTimeChipSize").getText().trim();
						}
					}
				}

			}
			
			
			
//			这里需要对systemModel进行读出
			TransformUtility.systemModel2XML(systemModel, modelXMLPath);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(modelStream != null){
				try {
					modelStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
public void modifyConnection(){
		
		File infoFile = new File(this.connectionInfoXML);
		
		SystemModel systemModel = null;
		FileInputStream modelStream = null;
		FileInputStream infoStream = null;
		try {

			systemModel = TransformUtility.xML2SystemModel(this.modelXMLPath);
			
//			从文件流中读取需要添加的信息
			infoStream = new FileInputStream(infoFile);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(infoStream);
			org.dom4j.Element rootElement = doc.getRootElement();
//			element below is component tag
			
			//component included here corresponds to the component from info XML
			List<Connection> connectionsModel = systemModel.getConnections();
			List<org.dom4j.Element> infoConnections = rootElement.elements();
//			这两个list中的元素的数目可能不一样，所以，首先要让componentModel中的元素的个数迁就infoComponent中的元素
//			situation:componentsModel size more than infoComponents size
			if(connectionsModel.size() > infoConnections.size()){
				System.out.println("AAXL 中构件数目比信息输入中的构件数目多");
				
			}else if(connectionsModel.size() < infoConnections.size()){
				System.out.println("AAXL 中构件数目比信息输入中的构件数目少");
			}
			
			Iterator<Connection> modelIter = connectionsModel.iterator();
			Iterator<org.dom4j.Element> infoIter = infoConnections.iterator();
			
			while(modelIter.hasNext() && infoIter.hasNext()){
				org.dom4j.Element connectionInfo = infoIter.next();
				Connection eachConnection = modelIter.next();
//				这两个list中的信息相互对应
				List<Property> properties = eachConnection.properties;
				List<org.dom4j.Element> infos = connectionInfo.elements();
//				思路是将infos存成一个以名字为键的hash表，针对每一个需要的Property，在这个hash表中去找到相应的信息，就好了
				HashMap<String, org.dom4j.Element> infosDict = new HashMap<String, org.dom4j.Element>();
				for(org.dom4j.Element eachInfo:infos){
					infosDict.put(eachInfo.getName(), eachInfo);
				}
				
				
				for(Property eachProperty:properties){
					if("ConnectionTime".equals(eachProperty.key)){
						eachProperty.value = infosDict.get("transferTime").getText().trim();
					}
				}
				
				
				
				
				
			}
			
			
			
//			这里需要对systemModel进行读出
			TransformUtility.systemModel2XML(systemModel, modelXMLPath);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(modelStream != null){
				try {
					modelStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
