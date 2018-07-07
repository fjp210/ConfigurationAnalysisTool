package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class SchedulabilityMethods {
/**
 * 仅判断两个分区当前调度方案是否满足要求，不发生值更新
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
		//判断两个分区调度时间差是否满足在较小分区的周期内，该两者分区的运行时间足够。
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
	 * @param list 已按分区周期由小到大排序的分区调度列表，最后一位为调度方案是否满足要求的标志位
	 * @param step 最小时间片，作为遍历的步长
	 * @param cur 当前第cur个分区，
	 * @param limit 第一个分区，即最小周期的分区周期长度，是其他分区调度起始点可以遍历的范围
	 */
	public static void backTrack(ArrayList<ArrayList<Integer>> list, int step, int cur) {
		//sign作为每层迭代中，分区i在第一个分区的周期范围内，遍历完所有可能的起始时间，仍不存在满足时，设定sign为false,表示该层结束，回溯到上一层。
		boolean sign = true;
		//递归退出条件
		if (cur >= list.size()-1) {
			return;
		}
		//latter是较大周期的分区
		ArrayList<Integer> latter = list.get(cur);		
			for (int j = 0;  j < cur; j++) {
				//former是较小周期的分区
				ArrayList<Integer> former = list.get(j);
				//当两个分区调度时间不满足时，进入循环，并且起始点增长step
				while (isValid(former, latter) == false) {
					//如果增长之后超出范围，则sign设为false，退出循环
					if(latter.get(1) + step >= latter.get(3)) {
						sign = false;
						break;
					}
					//否则，latter起始时间增加step，并初始化j=-1并退出while循环，则外层for循环从0开始重新遍历检验
					else{
						latter.set(1, latter.get(1) + step);
						j = -1;
						break;
					}
				}
				//直接退出外层for循环
				if (sign == false) {
					break;
				}
			}
			//当前节点已是最后一个分区位置，并且调度方案满足要求，则标记为list中最后一位为1，该处是为了跳出递归的函数
			if (cur == list.size()-2 && sign == true) {
				list.get(list.size()-1).set(0, 1);
				return;
			}
			//当前节点的调度方案满足，但list中最后一位为0，表示未完成判断，当前节点位后移1位，递归
			if (sign == true && list.get(list.size()-1).get(0) == 0) {
				backTrack(list, step, cur + 1);
			}
			//在上一行递归函数跳出后会继续执行以下代码，所以利用全局变量list的最后一位作为标记位，继续return
			if (list.get(list.size()-1).get(0) == 1) {
				return; 
			}
			//满足3个条件：1、list标记位不等于1，2、当前两个分区调度判断位sign位true，3、latter增长step不超过范围
			//该处递归是指下层递归结束，但是不满足整个函数退出要求，需要在当前节点处latter的步长增长step，以便遍历该层时间点加step
			else {
				while (list.get(list.size()-1).get(0) != 1 && sign == true && latter.get(1) + step < latter.get(3)) {
					//对原本下一层操作，修改过的调度起始时间置0
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
	 * 预处理，将软件的可调度性信息按照配置方案划分到不同模块上，便于后续分析计算
	 * @param calGraph
	 * @param configurationList
	 */
	public static HashMap<Integer, ArrayList<ArrayList<Integer>>> preprocessing(Graph calGraph, LinkedList<Integer> configurationList) {
		//
		//该HashMap存储可调度性分析所需信息，键是处理器Id,值是该处理器所对应的软件的信息列表
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
			//简单计算判断主时间框架内，所有分区的执行时间是否可能足够
			float probabilityTime = 0;
			for(ArrayList<Integer> l : val) {
				probabilityTime += (float)l.get(2)/(float)l.get(3);
			}
			if (probabilityTime > 1) {
				return false;
			}
			//按照自定义的周期由小到大排序，并且在最后一位插上标记位。
			Collections.sort(val, new MyComparator());
			ArrayList<Integer> sign = new ArrayList<Integer>(); 
			sign.add(0);
			val.add(sign);
			//1、获取不同处理器对应的遍历步长，最小时间片
			//2、获取遍历限制范围，即为最小周期
			int step = calGraph.processorInfoList.get(key);
			//int limit = val.get(0).get(3);
			backTrack(val, step, 0);
			
			
			
			//判断是否所有模块内都满足调度性要求，当任一模块遇到不满足情况时直接退出循环
			if (val.get(val.size()-1).get(0) == 1) {
				//删除最后一位标记位，再将调度分析结果存入参数infoMap中
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
