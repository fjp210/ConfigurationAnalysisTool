package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import edu.buaa.rse.dotx.model.Base;

public class DepthFirstSearchAlgorithm extends Base implements Algorithm{
	//�����ѷ��ʹ��Ľڵ�
	private ArrayList<Integer> visitedNode = new ArrayList<Integer>();
	//dfs�õ���·���б���
	private HashSet<LinkedList<Integer>> dfsPathList = new HashSet<LinkedList<Integer>>();
	@Override
	public void perform(Graph g) {
		//java.lang.System.out.println("g.firstNode: " + g.getFirstNode());
		LinkedList<Integer> list = new LinkedList<Integer>();
		DFS(g, g.getFirstNode(), list, 0);		
		//java.lang.System.out.println("dfsPathList " + dfsPathList);
	}

	public HashMap<Integer, Integer> getPath() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * ������ȱ������������õ���·������dfsPathList��
	 * @param g  ͼ
	 * @param sourceNode ����ĵ�
	 * @param list  �����б�
	 * @param type
	 * 
	 * @author ����  yss
	 */
	private void DFS(Graph g, int sourceNode, LinkedList<Integer> list, int type) {
		list.add(sourceNode); // 
		int ver = list.peekLast();
		LinkedList<Integer> toBeVisitedNode = g.getAdj().get(ver);
		//System.out.println("source : "+ sourceNode);
		//System.out.println("LinkedList : "+ toBeVisitedNode.size());

		switch (type) {
		case 0:
			DFS_Process(g, list, type, toBeVisitedNode);
			break;
			
		case 1:
			DFS_NoPocess(g, list, type, toBeVisitedNode);
			break;

		default:
			break;
		}
		list.pollLast();
	}
	private void DFS_NoPocess(Graph g, LinkedList<Integer> list, int type, LinkedList<Integer> toBeVisitedNode) {
		LinkedList<Integer> notInList = new LinkedList<Integer>();
		for (Integer integer : toBeVisitedNode) {
			if (!list.contains(integer)) {
				notInList.add(integer);
			}
		}
		
		if(notInList.size() == 0)
		{
				//java.lang.System.out.println("dfsPath: " + list.toString());
				LinkedList<Integer> templist = new LinkedList<Integer>(list);
				dfsPathList.add(templist);
			}
		else 
			{
				for (Integer integer : notInList) {
					DFS(g, integer, list, type);
				}
			}
	}
	private void DFS_Process(Graph g, LinkedList<Integer> list, int type, LinkedList<Integer> toBeVisitedNode) {
		for (Integer integer : toBeVisitedNode) {
			if (!list.contains(integer)) {
				DFS(g, integer, list, type);
			}
			else {
				//java.lang.System.out.println("dfsPath: " + list.toString());
				LinkedList<Integer> templist = new LinkedList<Integer>(list);
				dfsPathList.add(templist);
			}
		}
	}
	/**
	 * ����������ȱ����õ���·���б�
	 */
	public HashSet<LinkedList<Integer>> getPathList(){
		//java.lang.System.out.println("the Size of ReliabilityPathList: " + dfsPathList.size());
		return this.dfsPathList;
	}
	
}
