package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 该类主要处理配置数字转配置策略，以及各类约束的处理方法，包括基本约束、共存约束、互斥约束的工具类。
 * 
 * @author 佳盼
 *
 */
public class Integer2Status {
	/**
	 * 
	 * the digit represents the module, the position of the digit means the
	 * software
	 * 
	 * @param num
	 * @param softwareNumber
	 * @param processorNumber
	 * @return configurationList 是软件按顺序绑定在对应的硬件处理器上的绑定关系的列表；
	 *         此时的configurationList是未处理约束条件的，和软硬件编号的； 交互复杂度可以直接使用。
	 */
	public static LinkedList<Integer> number2Status(long num, int softwareNumber, int processorNumber) {
		LinkedList<Integer> configurationList = new LinkedList<Integer>();
		String hardwareRadix = Long.toUnsignedString(num, processorNumber);
		//java.lang.System.out.println("hardwareRadix: " + hardwareRadix);
		for (char c : hardwareRadix.toCharArray()) {
			if (Character.isLetter(c)) {
				int n = c - 'a';
				configurationList.add(n + 10);
			} else {
				configurationList.add(Character.getNumericValue(c));
			}
		}
		for (int i = configurationList.size(); i < softwareNumber; i++) {
			configurationList.addFirst(0);
		}
		//java.lang.System.out.println("configurationList: " + configurationList);
		return configurationList;
	}

	/**
	 * 处理基本约束，使得每个处理器上都有软件相连
	 * 
	 * @param configList
	 * @param processorNumber
	 * @return false/true 满足基本约束，返回true,否则返回false
	 */
	public static boolean baseConstraintHandle(LinkedList<Integer> configList, int processorNumber) {
		Set<Integer> set = new HashSet<Integer>();
		for (Integer integer : configList) {
			set.add(integer);
		}
		return set.size() == processorNumber;
	}

	/**
	 * 处理软硬件配置的level级的共存约束信息
	 * 
	 * @param configList
	 * @param HashMap<String,
	 *            ArrayList<Tuple>> onstraints 包含level级的共存约束信息
	 * @param hardwareNumber
	 *            是指硬件数量，将tuple中存储的软件id减去硬件数量，
	 * @return false/true 满足共存约束，返回true,否则返回false
	 */
	public static boolean partitionCoexistConstraintHandle(LinkedList<Integer> configList,
			HashMap<String, ArrayList<Tuple>> constraints, int hardwareNumber) {
		boolean res = true;
		if(constraints == null || constraints.get("Coexist") == null){
			return res;
		}
		for (Tuple tuple : constraints.get("Coexist")) {
			res = res
					&& (configList.get(tuple.first - hardwareNumber) == configList.get(tuple.second - hardwareNumber));
			if (false == res) {
				return res;
			}
		}
		return res;
	}

	/**
	 * 处理软硬件配置的分区级互斥约束信息
	 * 
	 * @param configList
	 * @param HashMap<String,
	 *            ArrayList<Tuple>> onstraints 包含level级的互斥约束信息
	 * @param hardwareNumber
	 *            是指硬件数量，将tuple中存储的软件id减去硬件数量，
	 * @return false/true 满足互斥约束，返回true,否则返回false
	 */
	public static boolean partitionExclusiveConstraintHandle(LinkedList<Integer> configList,
			HashMap<String, ArrayList<Tuple>> onstraints, int hardwareNumber) {
		boolean res = true;
		if(onstraints == null || onstraints.get("Repulsive") == null){
			return res;
		}
		for (Tuple tuple : onstraints.get("Repulsive")) {
			res = res
					&& (configList.get(tuple.first - hardwareNumber) != configList.get(tuple.second - hardwareNumber));
			if (false == res) {
				return res;
			}
		}
		return res;
	}

	/**
	 * 处理软硬件配置的模块级共存约束信息
	 * 
	 * @param configList
	 * @param moduleCoexistConstraints
	 * @return boolean TO DO
	 */
	public static boolean moduleCoexistConstraintHandle(LinkedList<Integer> configList,
			HashMap<Integer, Integer> moduleCoexistConstraints) {
		boolean res = true;

		return res;
	}

	/**
	 * 处理软硬件配置的模块级互斥约束信息
	 * 
	 * @param configList
	 * @param moduleExclusiveConstraints
	 * @return boolean TO DO
	 */
	public static boolean moduleExclusiveConstraintHandle(LinkedList<Integer> configList,
			HashMap<Integer, Integer> moduleExclusiveConstraints) {
		boolean res = true;

		return res;
	}

}
