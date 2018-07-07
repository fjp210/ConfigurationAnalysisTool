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
	 * TODO 修改完之后需要更新序列化ID
	 * 2017/11/4 serialVersionUID
	 */
	private static final long serialVersionUID = 123L;
	//图的起点
	private int firstNode;
	//图的起点列表，多个起点
	public LinkedList<Integer> firstNodeList = new LinkedList<>();
	//图的起点个数，用于多个任务起始遍历，计算系统架构可靠度
	private int firstNodeNumber = 0;
	//图的软件数量和硬件数量
	private int softwareNumber;
	private int hardwareNumber;
	private int processorNumber;
	//处理器的节点编号
	private ArrayList<Integer> processorIdList = new ArrayList<Integer>();
	//软件的节点编号
	public ArrayList<Integer> processIdList = new ArrayList<Integer>();
	//图节点数目
	private int numOfNode = 0;
	//图的邻接表
	public HashMap<Integer, LinkedList<Integer>> adjList = new HashMap<>();
	//遍历算法
	private Algorithm algorithm;
	//dfs得到的路径列表集合
	public LinkedList<LinkedList<Integer>> dfsPathList = new LinkedList<LinkedList<Integer>>();
	//bfs得到的路径列表
	public ArrayList<LinkedList<Integer>> bfsPathList = new ArrayList<LinkedList<Integer>>();
	/**
	 * 构件的各类信息
	 */
	//将图的节点信息存储到nodeList
	public ArrayList<Node> nodeList = new ArrayList<>();
	//图的节点的可靠度信息
	public ArrayList<Float> nodeReliabilityList = new ArrayList<Float>();
	//图的节点的运行时间
	public ArrayList<Integer> nodeRuntimeList = new ArrayList<Integer>();	
	//图的任务序列
	public ArrayList<LinkedList<Integer>> taskList;
	//图的节点的连接数量
	public ArrayList<Integer> ConnectionNumberOfNode = new ArrayList<Integer>();
	
	//2017/12/3 修改  添加可调度性分析属性、连接件运行时间
	public ArrayList<String> processorPolicy = new ArrayList<String>();
	public ArrayList<Integer> partTimeChipSize =new ArrayList<Integer>();
	public ArrayList<Integer> processPeriod = new ArrayList<Integer>();
	public ArrayList<Integer> processDeadlineTime = new ArrayList<Integer>();
	//存储连接件运行时间
	public HashMap<Integer, HashMap<Integer, Integer>> connectionTimeMap = new HashMap<>();
	
	//存储计算软件主时间框架所需信息的map
	HashMap<Integer, ArrayList<Integer>> mainTimeCalMap = new HashMap<Integer, ArrayList<Integer>>();
	//存储软件住时间框架map
	HashMap<Integer, Integer> mainTimeMap = new HashMap<Integer, Integer>();
	
	//调度性分析所需信息的数组列表
	ArrayList<ArrayList<Integer>> schedulabilityList = new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> processorInfoList = new ArrayList<Integer>();
	/**
	 * 构造函数
	 * @param num, algorithm
	 */
	public Graph() {
		//this.numOfNode = num;
		//this.algorithm = algorithm;		
	}
	
	
	
	/**
	 * 添加边
	 * @param fromNode
	 * @param toNode
	 * 再加入边时同时计数该节点的所连接的边数加一
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
	 * 添加顶点
	 * @param num
	 * @return
	 */
	public void addVertex(Integer node) {
		adjList.put(node, new LinkedList<>());
		}
	
	/**
	 * 获取邻接表
	 * @return
	 */
	public HashMap<Integer, LinkedList<Integer>> getAdj() {
		    return adjList;
	}
	
	/**
	 * 获取图的节点数目
	 * @param initialPoint
	 */
	public int getSize() {
		return this.numOfNode;
	}
	
	/**
	 * 获取起始节点的数量
	 */
	public int getFirstNodeNumber() {
		return this.firstNodeNumber;
	}
	/**
	 * 设定图遍历的算法
	 */
	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}
	/**
	 * 运行算法
	 */
	public void done() {
	    algorithm.perform(this);
	 }
	/**
	 * 获取系统的软件数量
	 * @return 个数
	 */
	public int getSoftwareNumber() {
		return this.softwareNumber;
	}
	
	/**
	 * 获取系统的硬件数量
	 * @return 个数
	 */
	public int getHardwareNumber() {
		return this.hardwareNumber;
		
	}
	
	/**
	 * 获取系统的处理器数量
	 * @return 个数
	 */
	public int getProcessorNumber() {
		return this.processorNumber;
	}

	/**
	 * 获取邻接表
	 */
	public HashMap<Integer, LinkedList<Integer>> getAdjList(){
		return this.adjList;
	}
	

	 /**
	  * 获取起始点
	  * @return
	  */
	public int getFirstNode() {
		return firstNode;
	}

	/**
	 * 设置起始点
	 * @param firstNode
	 */
	public void setFirstNode(int firstNode) {
		this.firstNode = firstNode;
	}
	/**
	 * 获取处理器列表
	 * @return
	 */
	public ArrayList<Integer> getProcessorIdList() {
		return this.processorIdList;
	}
	 
	 /**
	  * 依据配置生成程序，添加特定配置对应的边
	  */
	public void addSoftToHardEdge(ArrayList<Integer> processorIdList, LinkedList<Integer> list) {
		//java.lang.System.out.println("+++++:   " + processorIdList.toString());
		//System.out.println("配置方案: " + list.toString());
		for (int i = 0; i < list.size(); i++) {
			//第一项是：硬件的编号， 第二项是： 软件的编号
			int softId = i + this.hardwareNumber;
			this.addEdge(processorIdList.get(list.get(i)), softId);			
			
			//添加主时间框架的map
			if (mainTimeCalMap.containsKey(processorIdList.get(list.get(i)))) {
				mainTimeCalMap.get(processorIdList.get(list.get(i))).add(softId);
			}else {
				ArrayList<Integer> softIdList = new ArrayList<Integer>();
				softIdList.add(softId);
				mainTimeCalMap.put(processorIdList.get(list.get(i)),  softIdList);
			}
			
		}
		//再将map通过计算将主时间框架存到软件的属性里
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
	 * 获取system对象中构件的各类信息，包括可靠度、运行时间等
	 * @param systemModel 系统模型
	 */
	public void getSystemNode(SystemModel systemModel) {
		List<Connection> connectionList = systemModel.getConnections();
		List<Component> componentList = systemModel.getComponents();
		
		//对系统构件的遍历和信息提取
		for (Component com : componentList) {
			extractComponentInfo(com);			
			addNodeInfo(com);
			
			//提取可调度性软件信息
			if (com.getCategory().equals("PROCESS")) {
				ArrayList<Integer> processSchedulability = new ArrayList<Integer>();
				//分别添加id、起始时间、执行时间、周期
				processSchedulability.add(com.getComponentId() - this.hardwareNumber);
				processSchedulability.add(0);
				processSchedulability.add(Integer.valueOf(com.getProperty("runtime")));
				processSchedulability.add(Integer.valueOf(com.getProperty("ProcessPeriod")));
				this.schedulabilityList.add(processSchedulability);
			}
			//提取可调度性处理器信息
			if (com.getCategory().equals("PROCESSOR")) {
				this.processorInfoList.add(Integer.valueOf(com.getProperty("PartTimeChipSize")));
			}
		}
		processorNumber = processorIdList.size();
		//软硬件数量之和等于componentList的大小
		assert(this.softwareNumber + this.hardwareNumber == componentList.size());
		//对连接件的遍历和信息提取
		for (Connection con : connectionList) {			
			int fromNode = con.getSourceComponent().getComponentId();
			int toNode = con.getTargetComponent().getComponentId();
			this.addEdge(fromNode, toNode);
			//添加连接的时间
			

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
	 * 添加节点信息
	 * 	可靠度
	 * 	运行时间
	 * 到节点列表
	 * @param com
	 */
	private void addNodeInfo(Component com) {
		Node node = new Node(com.getComponentId(), com.getName());
		//节点添加可靠度信息
		node.setReliability(Float.valueOf(com.getProperty("reliability")));
		//节点添加运行时间信息
		node.setRunTime(Integer.valueOf(com.getProperty("runtime")));
		//将添加完各类信息的节点加入到图的nodeList中
		this.nodeList.add(node);
		
		//将节点Id和节点的可靠度,运行时间信息存入nodeReliabilityList和nodeRuntimeList
		this.nodeReliabilityList.add(Float.valueOf(com.getProperty("reliability")));
		this.nodeRuntimeList.add(Integer.valueOf(com.getProperty("runtime")));	
		
		//20171203 添加可调度性属性
		this.processorPolicy.add(String.valueOf(com.getProperty("ProcessorPolicy")));
		this.partTimeChipSize.add(Integer.valueOf(com.getProperty("PartTimeChipSize")));
		this.processPeriod.add(Integer.valueOf(com.getProperty("ProcessPeriod")));
		this.processDeadlineTime.add(Integer.valueOf(com.getProperty("ProcessDeadlineTime")));
		
		//加图节点
		addVertex(com.componentId);
		//java.lang.System.out.println("");
		this.ConnectionNumberOfNode.add(0);
	}


	/***
	 * 提取构建中相关系统参数
	 * @param com 模型构建
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

