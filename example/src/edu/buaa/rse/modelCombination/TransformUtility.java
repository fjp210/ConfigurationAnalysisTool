package edu.buaa.rse.modelCombination;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.Connection;
import edu.buaa.rse.dotx.model.Element;
import edu.buaa.rse.dotx.model.Feature;
import edu.buaa.rse.dotx.model.Property;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.model.deploy.AppToPartion;
import edu.buaa.rse.dotx.model.deploy.Deploy;

// after testing, it appears okay.
/**
 * @author Li Yaonan
 */
public class TransformUtility {
	// tested
	/***
	 * SystemModule 序列化
	 * 
	 * @param sm 系统模型
	 * @param xmlPath xml路径
	 */
	public static void systemModel2XML(SystemModel sm, String xmlPath) {

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(xmlPath);
			XStream x = new XStream(new DomDriver());
			XStream.setupDefaultSecurity(x);
			Class<?>[] classes = new Class[] { SystemModel.class, edu.buaa.rse.dotx.model.Property.class, Feature.class,
					Element.class, Connection.class, Component.class };
			x.allowTypes(classes);
			x.autodetectAnnotations(true);

			x.toXML(sm, fos);
			fos.flush();
		} catch (IOException ioeOut) {
			ioeOut.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/***
	 * SystemModule 反序列化
	 * 
	 * @param xmlPath xml路径
	 */
	public static SystemModel xML2SystemModel(String xmlPath) {
		File modelFile = new File(xmlPath);

		FileInputStream modelStream = null;
		SystemModel systemModel = null;
		try {
			modelStream = new FileInputStream(modelFile);
			XStream modelXStream = new XStream(new DomDriver());
			XStream.setupDefaultSecurity(modelXStream);
			Class<?>[] classes2 = new Class[] { SystemModel.class, Property.class, Feature.class,
					edu.buaa.rse.dotx.model.Element.class, Connection.class, Component.class };
			modelXStream.allowTypes(classes2);
			modelXStream.autodetectAnnotations(true);
			modelXStream.alias("system", SystemModel.class);
			systemModel = (SystemModel) modelXStream.fromXML(modelStream);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (modelStream != null) {
				try {
					modelStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return systemModel;
	}

	/**
	 * 任务链 反序列化
	 * @param xmlPath
	 * @return
	 */
	public static ArrayList<TaskSequence> taskChainReader(String xmlPath) {
		// 从文件流中读取需要添加的信息
		ArrayList<TaskSequence> taskSequences = new ArrayList<TaskSequence>();
		FileInputStream infoStream = null;
		File infoFile = new File(xmlPath);

		try {
			infoStream = new FileInputStream(infoFile);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(infoStream);
			org.dom4j.Element rootElement = doc.getRootElement();
			List<org.dom4j.Element> sequenceElements = rootElement.elements();
			// iter.next() 的东西是task
			Iterator<org.dom4j.Element> taskIter = sequenceElements.iterator();
			for (int i = 0; i < sequenceElements.size(); i++) {
				org.dom4j.Element tempTask = taskIter.next();

				List<org.dom4j.Element> items = tempTask.elements();
				Iterator<org.dom4j.Element> itemsIter = items.iterator();
				TaskSequence taskOb = new TaskSequence();
				while (itemsIter.hasNext()) {
					org.dom4j.Element temp = itemsIter.next();
					if ("number".equals(temp.getName())) {
						taskOb.number = temp.getText().trim();
					} else if ("taskSequence".equals(temp.getName())) {
						taskOb.taskSequence = temp.getText().trim().split("-");
					} else if ("timeRequest".equals(temp.getName())) {
						taskOb.timeRequest = temp.getText().trim();
					}
				}

				taskSequences.add(taskOb);

			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			if (infoStream != null) {
				try {
					infoStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return taskSequences;
	}
	
	/**
	 * 获取从xml文件解析得到的ArrayList<TaskSequence> taskSequences，将其转化为id的列表
	 * @param taskSequences
	 * @param model
	 * @return HashMap<String, LinkedList<Integer>> taskHashMap， 其中String键位任务的名字，LinkedList<Integer>为一条任务链。
	 */
	public static ArrayList<LinkedList<Integer>> getTaskSequence(ArrayList<TaskSequence> taskSequences, SystemModel model) {
		ArrayList<LinkedList<Integer>> taskArrayList = new ArrayList<LinkedList<Integer>>();
		for (TaskSequence task : taskSequences){
			LinkedList<Integer> taskList = new LinkedList<>();
			for (int i = 0; i<task.taskSequence.length; i++){
				for (Component component : model.getComponents()){
					if (component.getName().equalsIgnoreCase(task.taskSequence[i])) {
						taskList.add(component.getComponentId());
						break;
					}
				}
			}
			taskArrayList.add(taskList);
		}
	return taskArrayList;
	}
	/**
	 * 获取任务链的运行时间阈值
	 * @param taskSequences
	 * @return
	 */
	public static ArrayList<Integer> getRuntimeThresholdValue(ArrayList<TaskSequence> taskSequences){
		ArrayList<Integer> runtimeThresholdValue = new ArrayList<Integer>();
		for (TaskSequence task : taskSequences) {
			runtimeThresholdValue.add(Integer.valueOf(task.timeRequest));
		}
		return runtimeThresholdValue;
	}
	
	/**
	 * 读取Deploy的文件位置，生成Deploy对象
	 * @param xmlPath
	 * @return
	 */
	public static Deploy xml2Deploy(String xmlPath) {
		File modelFile = new File(xmlPath);

		FileInputStream modelStream = null;
		Deploy deploy = null;
		try {
			modelStream = new FileInputStream(modelFile);
			
			XStream x = new XStream();
			x.autodetectAnnotations(true);
			x.alias("system", SystemModel.class);
			x.alias("deploy", Deploy.class);
			deploy = (Deploy) x.fromXML(modelStream);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (modelStream != null) {
				try {
					modelStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return deploy;
	}	
}
