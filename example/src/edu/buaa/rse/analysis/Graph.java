package edu.buaa.rse.analysis;

import java.awt.HeadlessException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import edu.buaa.rse.dotx.model.*;
import edu.buaa.rse.dotx.model.SystemModel;
public class Graph extends Base{

	/**
	 * TODO �޸���֮����Ҫ�������л�ID
	 * 2017/11/4 serialVersionUID
	 */
	private static final long serialVersionUID = 123L;
	//ͼ�����
	private int firstNode;
	//ͼ������б�������
	public LinkedList<Integer> firstNodeList = new LinkedList<>();
	//ͼ�������������ڶ��������ʼ����������ϵͳ�ܹ��ɿ���
	private int firstNodeNumber = 0;
	//ͼ�����������Ӳ������
	private int softwareNumber;
	private int hardwareNumber;
	private int processorNumber;
	//�������Ľڵ���
	private ArrayList<Integer> processorIdList = new ArrayList<Integer>();
	//����Ľڵ���
	public ArrayList<Integer> processIdList = new ArrayList<Integer>();
	//ͼ�ڵ���Ŀ
	private int numOfNode = 0;
	//ͼ���ڽӱ�
	public HashMap<Integer, LinkedList<Integer>> adjList = new HashMap<>();
	//�����㷨
	private Algorithm algorithm;
	//dfs�õ���·���б���
	public LinkedList<LinkedList<Integer>> dfsPathList = new LinkedList<LinkedList<Integer>>();
	//bfs�õ���·���б�
	public ArrayList<LinkedList<Integer>> bfsPathList = new ArrayList<LinkedList<Integer>>();
	/**
	 * �����ĸ�����Ϣ
	 */
	//��ͼ�Ľڵ���Ϣ�洢��nodeList
	public ArrayList<Node> nodeList = new ArrayList<>();
	//ͼ�Ľڵ�Ŀɿ�����Ϣ
	public ArrayList<Float> nodeReliabilityList = new ArrayList<Float>();
	//ͼ�Ľڵ������ʱ��
	public ArrayList<Integer> nodeRuntimeList = new ArrayList<Integer>();	
	//ͼ����������
	public ArrayList<LinkedList<Integer>> taskList;
	//ͼ�Ľڵ����������
	public ArrayList<Integer> ConnectionNumberOfNode = new ArrayList<Integer>();
	
	//2017/12/3 �޸�  ��ӿɵ����Է������ԡ����Ӽ�����ʱ��
	public ArrayList<String> processorPolicy = new ArrayList<String>();
	public ArrayList<Integer> partTimeChipSize =new ArrayList<Integer>();
	public ArrayList<Integer> processPeriod = new ArrayList<Integer>();
	public ArrayList<Integer> processDeadlineTime = new ArrayList<Integer>();
	//�洢���Ӽ�����ʱ��
	public HashMap<Integer, HashMap<Integer, Integer>> connectionTimeMap = new HashMap<>();
	
	//�洢���������ʱ����������Ϣ��map
	HashMap<Integer, ArrayList<Integer>> mainTimeCalMap = new HashMap<Integer, ArrayList<Integer>>();
	//�洢���סʱ����map
	HashMap<Integer, Integer> mainTimeMap = new HashMap<Integer, Integer>();
	
	//�����Է���������Ϣ�������б�
	ArrayList<ArrayList<Integer>> schedulabilityList = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> processorInfoList = new ArrayList<Integer>();
	/**
	 * ���캯��
	 * @param num, algorithm
	 */
	public Graph() {
		//this.numOfNode = num;
		//this.algorithm = algorithm;		
	}
	
	
	
	/**
	 * ��ӱ�
	 * @param fromNode
	 * @param toNode
	 * �ټ����ʱͬʱ�����ýڵ�������ӵı�����һ
	 */
	public void addEdge(Integer fromNode, Integer toNode) {		
		//java.lang.System.out.println("adjList.size: " + toNode);
		this.adjList.get(fromNode).add(toNode);
		int temp1 = this.ConnectionNumberOfNode.get(fromNode);
		this.ConnectionNumberOfNode.set(fromNode, temp1+1);
		//java.lang.System.out.println("adjList.toNode: " + adjList.get(toNode).toString());
		this.adjList.get(toNode).add(fromNode);
		int temp2 = this.ConnectionNumberOfNode.get(toNode);
		this.ConnectionNumberOfNode.set(toNode, temp2+1);
//		System.out.println("11: " + ConnectionNumberOfNode.toString());
	  }
	
	/**
	 * ��Ӷ���
	 * @param num
	 * @return
	 */
	public void addVertex(Integer node) {
		adjList.put(node, new LinkedList<>());
		}
	
	/**
	 * ��ȡ�ڽӱ�
	 * @return
	 */
	public HashMap<Integer, LinkedList<Integer>> getAdj() {
		    return adjList;
	}
	
	/**
	 * ��ȡͼ�Ľڵ���Ŀ
	 * @param initialPoint
	 */
	public int getSize() {
		return this.numOfNode;
	}
	
	/**
	 * ��ȡ��ʼ�ڵ������
	 */
	public int getFirstNodeNumber() {
		return this.firstNodeNumber;
	}
	/**
	 * �趨ͼ�������㷨
	 */
	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}
	/**
	 * �����㷨
	 */
	public void done() {
	    algorithm.perform(this);
	 }
	/**
	 * ��ȡϵͳ���������
	 * @return ����
	 */
	public int getSoftwareNumber() {
		return this.softwareNumber;
	}
	
	/**
	 * ��ȡϵͳ��Ӳ������
	 * @return ����
	 */
	public int getHardwareNumber() {
		return this.hardwareNumber;
		
	}
	
	/**
	 * ��ȡϵͳ�Ĵ���������
	 * @return ����
	 */
	public int getProcessorNumber() {
		return this.processorNumber;
	}

	/**
	 * ��ȡ�ڽӱ�
	 */
	public HashMap<Integer, LinkedList<Integer>> getAdjList(){
		return this.adjList;
	}
	

	 /**
	  * ��ȡ��ʼ��
	  * @return
	  */
	public int getFirstNode() {
		return firstNode;
	}

	/**
	 * ������ʼ��
	 * @param firstNode
	 */
	public void setFirstNode(int firstNode) {
		this.firstNode = firstNode;
	}
	/**
	 * ��ȡ�������б�
	 * @return
	 */
	public ArrayList<Integer> getProcessorIdList() {
		return this.processorIdList;
	}
	 
	 /**
	  * �����������ɳ�������ض����ö�Ӧ�ı�
	  */
	public void addSoftToHardEdge(ArrayList<Integer> processorIdList, LinkedList<Integer> list) {
		//java.lang.System.out.println("+++++:   " + processorIdList.toString());
		//System.out.println("���÷���: " + list.toString());
		for (int i = 0; i < list.size(); i++) {
			//��һ���ǣ�Ӳ���ı�ţ� �ڶ����ǣ� ����ı��
			int softId = i + this.hardwareNumber;
			this.addEdge(processorIdList.get(list.get(i)), softId);			
			
			//�����ʱ���ܵ�map
			if (mainTimeCalMap.containsKey(processorIdList.get(list.get(i)))) {
				mainTimeCalMap.get(processorIdList.get(list.get(i))).add(softId);
			}else {
				ArrayList<Integer> softIdList = new ArrayList<Integer>();
				softIdList.add(softId);
				mainTimeCalMap.put(processorIdList.get(list.get(i)),  softIdList);
			}
			
		}
		//�ٽ�mapͨ�����㽫��ʱ���ܴ浽�����������
		for (Integer temp : mainTimeCalMap.keySet()) {
			ArrayList<Integer> mainTimeList = new ArrayList<Integer>();
			for (Integer integer : mainTimeCalMap.get(temp)) {
				mainTimeList.add(this.processPeriod.get(integer));
			}
			int maintime = LeastCommonMultiple.of(mainTimeList);
			for (Integer integer : mainTimeCalMap.get(temp)) {
//				if (mainTimeMap.containsKey(integer)) {
//					
//				}else {
					mainTimeMap.put(integer, maintime);
				//}
				
			}
		}
		//System.out.println("fjp2: " + mainTimeMap.toString());
	}
	
	
	
	
	/**
	 * ��ȡsystem�����й����ĸ�����Ϣ�������ɿ��ȡ�����ʱ���
	 * @param systemModel ϵͳģ��
	 */
	public void getSystemNode(SystemModel systemModel) {
		List<Connection> connectionList = systemModel.getConnections();
		List<Component> componentList = systemModel.getComponents();
		
		//��ϵͳ�����ı�������Ϣ��ȡ
		for (Component com : componentList) {
			extractComponentInfo(com);			
			addNodeInfo(com);
			
			//��ȡ�ɵ����������Ϣ
			if (com.getCategory().equals("PROCESS")) {
				ArrayList<Integer> processSchedulability = new ArrayList<Integer>();
				//�ֱ����id����ʼʱ�䡢ִ��ʱ�䡢����
				processSchedulability.add(com.getComponentId() - this.hardwareNumber);
				processSchedulability.add(0);
				processSchedulability.add(Integer.valueOf(com.getProperty("runtime")));
				processSchedulability.add(Integer.valueOf(com.getProperty("ProcessPeriod")));
				this.schedulabilityList.add(processSchedulability);
			}
			//��ȡ�ɵ����Դ�������Ϣ
			if (com.getCategory().equals("PROCESSOR")) {
				this.processorInfoList.add(Integer.valueOf(com.getProperty("PartTimeChipSize")));
			}
		}
		processorNumber = processorIdList.size();
		//��Ӳ������֮�͵���componentList�Ĵ�С
		assert(this.softwareNumber + this.hardwareNumber == componentList.size());
		//�����Ӽ��ı�������Ϣ��ȡ
		for (Connection con : connectionList) {			
			int fromNode = con.getSourceComponent().getComponentId();
			int toNode = con.getTargetComponent().getComponentId();
			this.addEdge(fromNode, toNode);
			//������ӵ�ʱ��
			

			if (this.connectionTimeMap.containsKey(fromNode)) {
				this.connectionTimeMap.get(fromNode).put(toNode, Integer.valueOf(con.getProperty("ConnectionTime")));
			}else {
				HashMap<Integer, Integer> fromNode2toNode = new HashMap<Integer, Integer>();
				fromNode2toNode.put(toNode, Integer.valueOf(con.getProperty("ConnectionTime")));
				this.connectionTimeMap.put(fromNode, fromNode2toNode);
			}
			
			if (this.connectionTimeMap.containsKey(toNode)) {
				this.connectionTimeMap.get(toNode).put(fromNode, Integer.valueOf(con.getProperty("ConnectionTime")));
			}
			else {
				HashMap<Integer, Integer> toNode2fromNode = new HashMap<Integer, Integer>();
				toNode2fromNode.put(fromNode, Integer.valueOf(con.getProperty("ConnectionTime")));
				this.connectionTimeMap.put(toNode, toNode2fromNode);
			}
		}
	}


	/**
	 * ��ӽڵ���Ϣ
	 * 	�ɿ���
	 * 	����ʱ��
	 * ���ڵ��б�
	 * @param com
	 */
	private void addNodeInfo(Component com) {
		Node node = new Node(com.getComponentId(), com.getName());
		//�ڵ���ӿɿ�����Ϣ
		node.setReliability(Float.valueOf(com.getProperty("reliability")));
		//�ڵ��������ʱ����Ϣ
		node.setRunTime(Integer.valueOf(com.getProperty("runtime")));
		//������������Ϣ�Ľڵ���뵽ͼ��nodeList��
		this.nodeList.add(node);
		
		//���ڵ�Id�ͽڵ�Ŀɿ���,����ʱ����Ϣ����nodeReliabilityList��nodeRuntimeList
		this.nodeReliabilityList.add(Float.valueOf(com.getProperty("reliability")));
		this.nodeRuntimeList.add(Integer.valueOf(com.getProperty("runtime")));	
		
		//20171203 ��ӿɵ���������
		this.processorPolicy.add(String.valueOf(com.getProperty("ProcessorPolicy")));
		this.partTimeChipSize.add(Integer.valueOf(com.getProperty("PartTimeChipSize")));
		this.processPeriod.add(Integer.valueOf(com.getProperty("ProcessPeriod")));
		this.processDeadlineTime.add(Integer.valueOf(com.getProperty("ProcessDeadlineTime")));
		
		//��ͼ�ڵ�
		addVertex(com.componentId);
		//java.lang.System.out.println("");
		this.ConnectionNumberOfNode.add(0);
	}


	/***
	 * ��ȡ���������ϵͳ����
	 * @param com ģ�͹���
	 */
	private void extractComponentInfo(Component com) {
		if (com.getComponentCategory().equalsIgnoreCase("processor") ) {
			processorIdList.add(com.getComponentId());
		}
		if (Integer.valueOf(com.getProperty("initialNode")) == 1) {
			this.setFirstNode(com.getComponentId());
			this.firstNodeList.add(com.getComponentId());
			this.firstNodeNumber++;
		}
		if (com.getComponentCategory().equalsIgnoreCase("process")) {
			this.processIdList.add(com.getComponentId());
			this.softwareNumber++;
		}else {
			this.hardwareNumber++;
		}
	}
}

