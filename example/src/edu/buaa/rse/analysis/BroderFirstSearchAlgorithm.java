package edu.buaa.rse.analysis;

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import edu.buaa.rse.dotx.model.Base;

public class BroderFirstSearchAlgorithm extends Base implements Algorithm {
	//保存已访问过的节点
	private ArrayList<Integer> visitedNode = new ArrayList<Integer>();
	//保存最短路径
	private HashMap<Integer, Integer> path;
	private Graph g;
	//任务序列的路径列表
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
	 * 广度优先遍历
	 * 遍历全部数据
	 * @param g 图
	 * @param sourceNode 起始点
	 */
	private void BFS(Graph g, Integer sourceNode) {
		BFS(g,sourceNode,-1);
	}
	
	/**
	 * 广度优先遍历 带结束点。
	 * 通过结束点结束path
	 * @param g 图
	 * @param sourceNode 起始点
	 * @param endNode 结束点
	 */
	private void BFS(Graph g, Integer sourceNode, int endNode) {
		// 标记已经找到end点
		boolean flag = true;
		//初始数据清理
		this.visitedNode.clear();
		this.path.clear();
		//存储待遍历的节点
		Queue<Integer> queue = new LinkedList<>();
		//标记起点
		visitedNode.add(sourceNode);
		//起始节点入列
		queue.add(sourceNode);
		
		while (!queue.isEmpty() && flag) {
			int ver = queue.poll();
			LinkedList<Integer> toBeVisitedNode = g.getAdj().get(ver);
			for (int v : toBeVisitedNode) {
				if (!visitedNode.contains(v)) {
					visitedNode.add(v);
					//关键：存储路径对时将终点为键，起始点为值，使得求最短路径时从终点开始，倒序
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
	 * 生成任务链对应的执行路径 
	 * taskPathList是用于存储所有任务的最终遍历路径
	 * taskList是用于存储所有的任务序列
	 * tempPath是用于存储每两个路径之间的最短路径的列表
	 * path是用于存储单个整条任务的遍历路径
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
	 * 输出任务路径列表
	 */
	public ArrayList<LinkedList<Integer>> getPathList(ArrayList<LinkedList<Integer>> tList){
		bfsPathList = generateTaskPath(tList);
		//System.out.println("the Size of TaskPathList: " + this.bfsPathList.size());
		return bfsPathList;
	}
	
}
