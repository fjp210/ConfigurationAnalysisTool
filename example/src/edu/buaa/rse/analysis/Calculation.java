package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;


public class Calculation {
		/**
	 * 计算系统可靠度
	 * @param g 该图已是添加完配置策略的图
	 */
	private static float calReliability(Graph g) {
		//系统可靠度
		float systemReliability;
		//构造dfs算法对象
		DepthFirstSearchAlgorithm dfs = new DepthFirstSearchAlgorithm();
		//添加图遍历算法
		g.setAlgorithm(dfs);
		//执行深度优先遍历算法
		g.done();
		//路径列表
		HashSet<LinkedList<Integer>> pathList = dfs.getPathList();
		float allPathReliability = 0; 
		float allPathProbability = 0;
		for (LinkedList<Integer> list : pathList) {			
			float pathReliability = 1;
			float pathProbability = 1;
			//System.out.println("\n"+ "path: " + list.toString());
			for (int i : list) {
				float probability = 1 / (float)g.ConnectionNumberOfNode.get(i);
				//System.out.println("connectionNumber: " + g.ConnectionNumberOfNode.get(i));
				
				//System.out.println("probability: " + probability);
				pathReliability *= (g.nodeReliabilityList.get(i) * probability);
				//System.out.println("nodeReliability: " + g.nodeReliabilityList.get(i));
				pathProbability *= probability;				
			} 
			//System.out.println("pathReliability: " + pathReliability);
			//System.out.println("pathProbability: " + pathProbability);
			allPathReliability += pathReliability;
			allPathProbability += pathProbability;
		}
		//System.out.println("allPathReliability: " + allPathReliability);
		//System.out.println("allPathProbability: " + allPathProbability);
		systemReliability = allPathReliability / allPathProbability;
		//java.lang.System.out.println("\n"+  "SystemReliability: " + systemReliability);
		//System.out.println("");
		//System.out.println("-------------------------------------------------");
		//System.out.println("");
		return systemReliability;		
	}
	
	public static float calReliabilityAll(Graph g) {
		//System.out.println("g.firstNodeNumber: "+ g.firstNodeList.size());
		float allSystemReliability = 0;
		while (!g.firstNodeList.isEmpty()) {
			float tempReliability = 0;
			int sourceNode = g.firstNodeList.pollFirst();
			//System.out.println("sourceNode: " + sourceNode);
			g.setFirstNode(sourceNode);
			tempReliability = calReliability(g);
			allSystemReliability += tempReliability;
		}
		allSystemReliability = allSystemReliability / g.getFirstNodeNumber();
		//System.out.println("allSystemReliability: " + allSystemReliability);
		return allSystemReliability;
	}
	
	/**
	 * 计算系统任务运行时间
	 * 2018/5/13/修改：计算任务最差执行时间，不能加主时间框架，应该加每个软件的执行周期。
	 */
	public static ArrayList<Integer> calTaskRuntime(Graph g, ArrayList<LinkedList<Integer>> taskList) {
		//系统任务运行时间
		ArrayList<Integer> taskRunTimeList = new ArrayList<Integer>();
		
		//构造bfs算法
		BroderFirstSearchAlgorithm bfs = new BroderFirstSearchAlgorithm();
		//添加图遍历算法
		g.setAlgorithm(bfs);
		//执行深度优先遍历算法
		g.done();
		
		//路径列表
		ArrayList<LinkedList<Integer>> pathList = bfs.getPathList(taskList);
		for (LinkedList<Integer> list : pathList) {
			int pathRuntime = 0;
			for (int i = 0; i < list.size(); i++) {
				pathRuntime += (g.nodeRuntimeList.get(list.get(i)) + g.processPeriod.get(list.get(i)));
				
				//加上连接的时间
				if (i < list.size()-1) {
					int a = list.get(i);
					int b = list.get(i+1);
					if (g.connectionTimeMap.containsKey(a)) {
						if (g.connectionTimeMap.get(a).containsKey(b)) {
							pathRuntime += g.connectionTimeMap.get(a).get(b);
							//System.out.println("fjp3: " + g.connectionTimeMap.get(g.connectionTimeMap.get(a).get(b)));

						}
					}
				}
				//加上主时间框架
				//if(g.mainTimeMap.containsKey(list.get(i))) {
					//pathRuntime += g.mainTimeMap.get(list.get(i));
				//}
				
				
			}
			//System.out.println("path: " + list.toString() + "  runtime: " + pathRuntime);
			taskRunTimeList.add(pathRuntime);
		}
		//java.lang.System.out.println("taskRunTimeList: " + taskRunTimeList);
		return taskRunTimeList;
	}
}
