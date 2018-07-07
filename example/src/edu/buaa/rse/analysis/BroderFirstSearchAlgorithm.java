package edu.buaa.rse.analysis;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import edu.buaa.rse.dotx.model.Base;

public class BroderFirstSearchAlgorithm extends Base implements Algorithm {
	//�����ѷ��ʹ��Ľڵ�
	private ArrayList<Integer> visitedNode = new ArrayList<Integer>();
	//�������·��
	private HashMap<Integer, Integer> path;
	private Graph g;
	//�������е�·���б�
	ArrayList<LinkedList<Integer>> bfsPathList = new ArrayList<LinkedList<Integer>>();
	
	
	@Override
	public void perform(Graph g) {
		if (path == null) {
			path = new HashMap<Integer, Integer>();
		}
		
		this.g = g;
		int sourceNode = 0;
		BFS(this.g, sourceNode);
	}
	
	public HashMap<Integer, Integer> getPath() {
		return getPath(0);
	}

	
	public HashMap<Integer, Integer> getPath(int sourceNode) {
		BFS(this.g, sourceNode);
		return this.path;
	}
	
	/**
	 * ������ȱ���
	 * ����ȫ������
	 * @param g ͼ
	 * @param sourceNode ��ʼ��
	 */
	private void BFS(Graph g, Integer sourceNode) {
		BFS(g,sourceNode,-1);
	}
	
	/**
	 * ������ȱ��� �������㡣
	 * ͨ�����������path
	 * @param g ͼ
	 * @param sourceNode ��ʼ��
	 * @param endNode ������
	 */
	private void BFS(Graph g, Integer sourceNode, int endNode) {
		// ����Ѿ��ҵ�end��
		boolean flag = true;
		//��ʼ��������
		this.visitedNode.clear();
		this.path.clear();
		//�洢�������Ľڵ�
		Queue<Integer> queue = new LinkedList<>();
		//������
		visitedNode.add(sourceNode);
		//��ʼ�ڵ�����
		queue.add(sourceNode);
		
		while (!queue.isEmpty() && flag) {
			int ver = queue.poll();
			LinkedList<Integer> toBeVisitedNode = g.getAdj().get(ver);
			for (int v : toBeVisitedNode) {
				if (!visitedNode.contains(v)) {
					visitedNode.add(v);
					//�ؼ����洢·����ʱ���յ�Ϊ������ʼ��Ϊֵ��ʹ�������·��ʱ���յ㿪ʼ������
					this.path.put(v, ver);
					queue.add(v);
					if (v == endNode)
					{
						flag = false;
					}
				}
			}
		}
	}
	
	public LinkedList<Integer> findPathTo(int sourceNode, int endNode) {
	    LinkedList<Integer> list = new LinkedList<Integer>();
	    list.add(endNode);
	    HashMap<Integer, Integer> path = getPath(sourceNode);
	    //System.out.println("Path: " + path.toString());
	    //System.out.println("Here " + sourceNode + "  " + endNode);
	    for (Integer location = path.get(endNode) ; 
	    		false == location.equals(sourceNode) ; 
	    			location = path.get(location)) {
	    	//System.out.println("location: " + location);
	    	list.addFirst(location);
	    }
	    
	    list.addFirst(sourceNode);
	    return list;
	  }
	
	/**
	 * ������������Ӧ��ִ��·�� 
	 * taskPathList�����ڴ洢������������ձ���·��
	 * taskList�����ڴ洢���е���������
	 * tempPath�����ڴ洢ÿ����·��֮������·�����б�
	 * path�����ڴ洢������������ı���·��
	 */
	public ArrayList<LinkedList<Integer>> generateTaskPath(ArrayList<LinkedList<Integer>> taskList){		
		for (LinkedList<Integer> tlist : taskList) {
			//System.out.println("taskList: " + taskList.toString());
			if (tlist.isEmpty()) break;
			int sourceNode = tlist.pollFirst();
			LinkedList<Integer> path = new LinkedList<Integer>();
			while (!tlist.isEmpty()) {
				int endNode = tlist.pollFirst();
				//System.out.println("sourceNode: " + sourceNode);
				//System.out.println("endNode: " + endNode);
				LinkedList<Integer> tempPath = this.findPathTo(sourceNode, endNode);
				
				sourceNode = endNode;
				//System.out.println("tempPath: " + tempPath.toString());
				if (!path.isEmpty()) {
					tempPath.pollFirst();
				}
				path.addAll(tempPath);
			}	
			//System.out.println("---");
			bfsPathList.add(path);			
		}
		return bfsPathList;
	}
	
	/**
	 * �������·���б�
	 */
	public ArrayList<LinkedList<Integer>> getPathList(ArrayList<LinkedList<Integer>> tList){
		bfsPathList = generateTaskPath(tList);
		//System.out.println("the Size of TaskPathList: " + this.bfsPathList.size());
		return bfsPathList;
	}
	
}
