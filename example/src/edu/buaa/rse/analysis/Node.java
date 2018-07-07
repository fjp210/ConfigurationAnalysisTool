package edu.buaa.rse.analysis;

import edu.buaa.rse.dotx.model.Base;

public class Node extends Base{
	private int nodeId;
	private String nodeName;
	private float reliability = 1;
	private int runTime = 0;
	
	
	public Node(int id, String name) {
		nodeId = id;
		nodeName = name;
	}
	
	//构件节点设置可靠度，连接件节点可靠度默认为1
	public void setReliability(float rel) {
		this.reliability = rel;
	}
	public float getReliability() {
		return this.reliability;
	}
	//构件节点设置运行时间，连接件节点运行时间默认为0
	public void setRunTime(int time) {
		this.runTime = time;
	}
	public int getRunTime() {
		return this.runTime;
	}
}
