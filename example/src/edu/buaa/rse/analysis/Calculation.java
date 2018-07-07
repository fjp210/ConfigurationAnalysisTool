package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;


public class Calculation {
		/**
	 * ����ϵͳ�ɿ���
	 * @param g ��ͼ������������ò��Ե�ͼ
	 */
	private static float calReliability(Graph g) {
		//ϵͳ�ɿ���
		float systemReliability;
		//����dfs�㷨����
		DepthFirstSearchAlgorithm dfs = new DepthFirstSearchAlgorithm();
		//���ͼ�����㷨
		g.setAlgorithm(dfs);
		//ִ��������ȱ����㷨
		g.done();
		//·���б�
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
	 * ����ϵͳ��������ʱ��
	 * 2018/5/13/�޸ģ������������ִ��ʱ�䣬���ܼ���ʱ���ܣ�Ӧ�ü�ÿ�������ִ�����ڡ�
	 */
	public static ArrayList<Integer> calTaskRuntime(Graph g, ArrayList<LinkedList<Integer>> taskList) {
		//ϵͳ��������ʱ��
		ArrayList<Integer> taskRunTimeList = new ArrayList<Integer>();
		
		//����bfs�㷨
		BroderFirstSearchAlgorithm bfs = new BroderFirstSearchAlgorithm();
		//���ͼ�����㷨
		g.setAlgorithm(bfs);
		//ִ��������ȱ����㷨
		g.done();
		
		//·���б�
		ArrayList<LinkedList<Integer>> pathList = bfs.getPathList(taskList);
		for (LinkedList<Integer> list : pathList) {
			int pathRuntime = 0;
			for (int i = 0; i < list.size(); i++) {
				pathRuntime += (g.nodeRuntimeList.get(list.get(i)) + g.processPeriod.get(list.get(i)));
				
				//�������ӵ�ʱ��
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
				//������ʱ����
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
