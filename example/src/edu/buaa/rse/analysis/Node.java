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
	
	//�����ڵ����ÿɿ��ȣ����Ӽ��ڵ�ɿ���Ĭ��Ϊ1
	public void setReliability(float rel) {
		this.reliability = rel;
	}
	public float getReliability() {
		return this.reliability;
	}
	//�����ڵ���������ʱ�䣬���Ӽ��ڵ�����ʱ��Ĭ��Ϊ0
	public void setRunTime(int time) {
		this.runTime = time;
	}
	public int getRunTime() {
		return this.runTime;
	}
}
