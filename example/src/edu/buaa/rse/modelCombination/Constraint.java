package edu.buaa.rse.modelCombination;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import edu.buaa.rse.analysis.Tuple;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.SystemModel;

public class Constraint {
	String number;
	String type;
	String level;
	String info;
	// a,b��ʾԼ���Ե���������
	String a;
	String b;
	// i,j��ʾԼ���Ե��������ֶ�Ӧ������id
	int i;
	int j;

	/**
	 * ��ȡǰ�δ����Լ����Ϣ��XML�ļ��������õ�constraintList,��key��levelֵ�����Ǹ�level��������Լ�������б�
	 * 
	 * @param xmlPath
	 * @return Constraint
	 */
	public static HashMap<String, ArrayList<Constraint>> constraintReader(String xmlPath, SystemModel model) {
		File file = new File(xmlPath);
		FileInputStream infoStream = null;
		HashMap<String, ArrayList<Constraint>> constraintList = new HashMap<String, ArrayList<Constraint>>();
		try {
			infoStream = new FileInputStream(file);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(infoStream);
			org.dom4j.Element rootElement = doc.getRootElement();
			List<org.dom4j.Element> constraints = rootElement.elements();
			for (org.dom4j.Element constraint : constraints) {
				Constraint cst = new Constraint();
				cst.number = constraint.elementTextTrim("number");
				//System.out.println('\n'+"number: "+ cst.number);
				cst.type = constraint.elementTextTrim("type");
				//System.out.println("type: " + cst.type);
				cst.level = constraint.elementTextTrim("level");
				//System.out.println("level: " + cst.level);
				cst.info = constraint.elementTextTrim("info");
				//System.out.println("info: " + cst.info);
				String[] things = cst.info.split("!?=");
				cst.a = things[0];
				cst.b = things[1];
				//System.out.println("a: " + cst.a + "  b: " + cst.b);
				// ����id��ϢҲ����Constraint����
				for (Component component : model.getComponents()) {
					if (component.getName().equalsIgnoreCase(cst.a)) {
						cst.i = component.getComponentId();
					}
					if (component.getName().equalsIgnoreCase(cst.b)) {
						cst.j = component.getComponentId();
					}
				}
				//System.out.println("cst.i: " + cst.i + "   cst.j: " + cst.j);
				
				if (constraintList.containsKey(cst.level)){
					constraintList.get(cst.level).add(cst);
				}else{
					ArrayList<Constraint> con = new ArrayList<>();
					con.add(cst);
					constraintList.put(cst.level, con);
					//System.out.println("key: " + constraintList.keySet().toString());
				}
					
				//constraintList.getOrDefault(cst.level, new ArrayList<Constraint>());
				
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			if (infoStream != null) {
				try {
					infoStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("constraintList.size: " + constraintList.get("Partition").size());
		return constraintList;
	}

	/**
	 * ͨ�÷����� ȡlevel����Լ����Ϣ��level ������Partition, Module��
	 * 
	 * @param HashMap<String,
	 *            ArrayList<Constraint>> constraintList, ����String����ʾʲôlevel����
	 *            ֵ��ʾ�ü����Լ���б���������Լ��������Լ����������Լ��
	 * @return HashMap<String, Tuple> constraints������String����ʾʲô���͵�Լ����
	 *         ֵTuple��һ���Լ�����Ķ�ԪԪ�飬����һ��Լ���ԡ�
	 */
	public static HashMap<String, ArrayList<Tuple>> allKindsOfConstraintMap(
			HashMap<String, ArrayList<Constraint>> constraintList, String level) {
		HashMap<String, ArrayList<Tuple>> constraints = new HashMap<String, ArrayList<Tuple>>();
		if(!constraintList.isEmpty()) {
			if(constraintList.containsKey(level)) {
				for (Constraint constraint : constraintList.get(level)) {
					if (constraint.type.equals("Coexist")) {
						if (constraints.containsKey("Coexist")){
							constraints.get("Coexist").add(new Tuple(constraint.i, constraint.j));
						}else{
							ArrayList<Tuple> con = new ArrayList<>();
							con.add(new Tuple(constraint.i, constraint.j));
							constraints.put("Coexist", con);				
						}
						//System.out.println("constraints: " + constraints.keySet().toString());
					}
					if (constraint.type.equals("Repulsive")) {
						if (constraints.containsKey("Repulsive")){
							constraints.get("Repulsive").add(new Tuple(constraint.i, constraint.j));
						}else{
							ArrayList<Tuple> con = new ArrayList<>();
							con.add(new Tuple(constraint.i, constraint.j));
							constraints.put("Repulsive", con);
						}
					}
				}
			}	
		}
		return constraints;
	}
}



/*
 * 			System.out.println("constraint.getName(): " + constraint.get
				if ("number".equals(constraint.)) {
					cst.number = constraint.getText();
					System.out.println("name: " + constraint.getName() + " cst.number: " + cst.number);
				} else if ("type".equals(constraint.getName())) {
					cst.type = constraint.getText();
					System.out.println(constraint.getName() + "  "+ constraint.getText());
					
				} else if ("level".equals(constraint.getName())) {
					cst.level = constraint.getText();
					System.out.println(constraint.getName() + " " + constraint.getText());
				} else if ("info".equals(constraint.getName())) {
					cst.info = constraint.getText();
					System.out.println(constraint.getName() + " " + constraint.getText());
				}
 */

