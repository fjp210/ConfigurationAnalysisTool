package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class SchedulabilityMethods {
/**
 * ���ж�����������ǰ���ȷ����Ƿ�����Ҫ�󣬲�����ֵ����
 * @param former
 * @param latter
 * @return
 */
	public static boolean isValid(ArrayList<Integer> former, ArrayList<Integer> latter) {
		int startOfFormer = former.get(1);
		int durationOfFormer = former.get(2);
		int periodOfFormer = former.get(3);
		int startOfLatter = latter.get(1);
		int durationOfLatter= latter.get(2);
		int periodOfLatter = latter.get(3);
		int commonMultiple = LeastCommonMultiple.lcm(periodOfFormer, periodOfLatter);
		//�ж�������������ʱ����Ƿ������ڽ�С�����������ڣ������߷���������ʱ���㹻��
		for (int i=0; i <= commonMultiple/periodOfLatter; i++) {
			int timeDifference = (startOfLatter - startOfFormer + i * (periodOfLatter - periodOfFormer) % periodOfFormer) % periodOfFormer;
			if (startOfFormer + durationOfFormer <= startOfLatter && durationOfFormer <=  timeDifference && timeDifference <= periodOfFormer - durationOfLatter) {
				continue;
			}else {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * @param list �Ѱ�����������С��������ķ��������б����һλΪ���ȷ����Ƿ�����Ҫ��ı�־λ
	 * @param step ��Сʱ��Ƭ����Ϊ�����Ĳ���
	 * @param cur ��ǰ��cur��������
	 * @param limit ��һ������������С���ڵķ������ڳ��ȣ�����������������ʼ����Ա����ķ�Χ
	 */
	public static void backTrack(ArrayList<ArrayList<Integer>> list, int step, int cur) {
		//sign��Ϊÿ������У�����i�ڵ�һ�����������ڷ�Χ�ڣ����������п��ܵ���ʼʱ�䣬�Բ���������ʱ���趨signΪfalse,��ʾ�ò���������ݵ���һ�㡣
		boolean sign = true;
		//�ݹ��˳�����
		if (cur >= list.size()-1) {
			return;
		}
		//latter�ǽϴ����ڵķ���
		ArrayList<Integer> latter = list.get(cur);		
			for (int j = 0;  j < cur; j++) {
				//former�ǽ�С���ڵķ���
				ArrayList<Integer> former = list.get(j);
				//��������������ʱ�䲻����ʱ������ѭ����������ʼ������step
				while (isValid(former, latter) == false) {
					//�������֮�󳬳���Χ����sign��Ϊfalse���˳�ѭ��
					if(latter.get(1) + step >= latter.get(3)) {
						sign = false;
						break;
					}
					//����latter��ʼʱ������step������ʼ��j=-1���˳�whileѭ���������forѭ����0��ʼ���±�������
					else{
						latter.set(1, latter.get(1) + step);
						j = -1;
						break;
					}
				}
				//ֱ���˳����forѭ��
				if (sign == false) {
					break;
				}
			}
			//��ǰ�ڵ��������һ������λ�ã����ҵ��ȷ�������Ҫ������Ϊlist�����һλΪ1���ô���Ϊ�������ݹ�ĺ���
			if (cur == list.size()-2 && sign == true) {
				list.get(list.size()-1).set(0, 1);
				return;
			}
			//��ǰ�ڵ�ĵ��ȷ������㣬��list�����һλΪ0����ʾδ����жϣ���ǰ�ڵ�λ����1λ���ݹ�
			if (sign == true && list.get(list.size()-1).get(0) == 0) {
				backTrack(list, step, cur + 1);
			}
			//����һ�еݹ麯������������ִ�����´��룬��������ȫ�ֱ���list�����һλ��Ϊ���λ������return
			if (list.get(list.size()-1).get(0) == 1) {
				return; 
			}
			//����3��������1��list���λ������1��2����ǰ�������������ж�λsignλtrue��3��latter����step��������Χ
			//�ô��ݹ���ָ�²�ݹ���������ǲ��������������˳�Ҫ����Ҫ�ڵ�ǰ�ڵ㴦latter�Ĳ�������step���Ա�����ò�ʱ����step
			else {
				while (list.get(list.size()-1).get(0) != 1 && sign == true && latter.get(1) + step < latter.get(3)) {
					//��ԭ����һ��������޸Ĺ��ĵ�����ʼʱ����0
					if (cur < list.size()-1) {
						list.get(cur + 1).set(1, 0);
					}
					latter.set(1, latter.get(1) + step);
					backTrack(list, step, cur);
				}					
			}	
		return;
	}
	/**
	 * Ԥ����������Ŀɵ�������Ϣ�������÷������ֵ���ͬģ���ϣ����ں�����������
	 * @param calGraph
	 * @param configurationList
	 */
	public static HashMap<Integer, ArrayList<ArrayList<Integer>>> preprocessing(Graph calGraph, LinkedList<Integer> configurationList) {
		//
		//��HashMap�洢�ɵ����Է���������Ϣ�����Ǵ�����Id,ֵ�Ǹô���������Ӧ���������Ϣ�б�
		HashMap<Integer, ArrayList<ArrayList<Integer>>> schedulabilityInfoMap = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();		
		ArrayList<ArrayList<Integer>> processInfoList = calGraph.schedulabilityList;
		for (int processId = 0; processId < configurationList.size(); processId++) {
			int processorId = configurationList.get(processId);
			if(!schedulabilityInfoMap.containsKey(processorId)){
				ArrayList<ArrayList<Integer>> tempList = new ArrayList<ArrayList<Integer>>();
				tempList.add(processInfoList.get(processId));
				schedulabilityInfoMap.put(processorId, tempList);
			}else {
				schedulabilityInfoMap.get(processorId).add(processInfoList.get(processId));
			}						
		}
		return schedulabilityInfoMap;
	}
	/*
	 * 
	 */
	public static boolean calSchedulability(Graph calGraph, HashMap<Integer, ArrayList<ArrayList<Integer>>> infoMap) {
		Iterator iter = infoMap.entrySet().iterator();
		boolean schedulabilitySign = true;
		while(iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			Integer key = (Integer) entry.getKey();
			ArrayList<ArrayList<Integer>> val = (ArrayList<ArrayList<Integer>>) entry.getValue();
			//�򵥼����ж���ʱ�����ڣ����з�����ִ��ʱ���Ƿ�����㹻
			float probabilityTime = 0;
			for(ArrayList<Integer> l : val) {
				probabilityTime += (float)l.get(2)/(float)l.get(3);
			}
			if (probabilityTime > 1) {
				return false;
			}
			//�����Զ����������С�������򣬲��������һλ���ϱ��λ��
			Collections.sort(val, new MyComparator());
			ArrayList<Integer> sign = new ArrayList<Integer>(); 
			sign.add(0);
			val.add(sign);
			//1����ȡ��ͬ��������Ӧ�ı�����������Сʱ��Ƭ
			//2����ȡ�������Ʒ�Χ����Ϊ��С����
			int step = calGraph.processorInfoList.get(key);
			//int limit = val.get(0).get(3);
			backTrack(val, step, 0);
			
			
			
			//�ж��Ƿ�����ģ���ڶ����������Ҫ�󣬵���һģ���������������ʱֱ���˳�ѭ��
			if (val.get(val.size()-1).get(0) == 1) {
				//ɾ�����һλ���λ���ٽ����ȷ�������������infoMap��
				val.remove(val.size()-1);
				infoMap.replace(key, val);
				schedulabilitySign = schedulabilitySign && true;
			}else {
				schedulabilitySign = false;
				break;
			}
		}		
		return schedulabilitySign;
		
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> l1 = new ArrayList<Integer>();				
		l1.add(0);l1.add(0);l1.add(5);l1.add(50);
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		l2.add(1);l2.add(0);l2.add(5);l2.add(100);
		ArrayList<Integer> l3 = new ArrayList<Integer>();
		l3.add(2);l3.add(0);l3.add(10);l3.add(100);
		ArrayList<Integer> l4 = new ArrayList<Integer>();
		l4.add(6);l4.add(0);l4.add(10);l4.add(100);
		ArrayList<Integer> l5 = new ArrayList<Integer>();
		l5.add(3);l5.add(0);l5.add(10);l5.add(250);
		ArrayList<Integer> l6 = new ArrayList<Integer>();
		l6.add(4);l6.add(0);l6.add(20);l6.add(250);
		ArrayList<Integer> l = new ArrayList<Integer>();
		l.add(0);
		
	
		ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
		list.add(l1);
		list.add(l2);
		list.add(l3);
		list.add(l4);
		list.add(l5);
		list.add(l6);
		list.add(l);
	
		
		long beginTime = System.nanoTime();
		ArrayList<Integer> t1 = new ArrayList<Integer>();
		t1.add(0);t1.add(0);t1.add(10);t1.add(40);
		ArrayList<Integer> t2 = new ArrayList<Integer>();
		t2.add(0);t2.add(30);t2.add(10);t2.add(100);
		int limit = list.get(0).get(3);
		SchedulabilityMethods.backTrack(list, 5, 1);
		long endTime = System.nanoTime();
		//SchedulabilityMethods.isValid(t1, t2);
		//SchedulabilityMethods.sortByPeriod(list);
		System.out.println(list);
		System.out.println("Time: " + (endTime - beginTime));
	}
}
