package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * ������Ҫ������������ת���ò��ԣ��Լ�����Լ���Ĵ���������������Լ��������Լ��������Լ���Ĺ����ࡣ
 * 
 * @author ����
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
	 * @return configurationList �������˳����ڶ�Ӧ��Ӳ���������ϵİ󶨹�ϵ���б�
	 *         ��ʱ��configurationList��δ����Լ�������ģ�����Ӳ����ŵģ� �������Ӷȿ���ֱ��ʹ�á�
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
	 * �������Լ����ʹ��ÿ���������϶����������
	 * 
	 * @param configList
	 * @param processorNumber
	 * @return false/true �������Լ��������true,���򷵻�false
	 */
	public static boolean baseConstraintHandle(LinkedList<Integer> configList, int processorNumber) {
		Set<Integer> set = new HashSet<Integer>();
		for (Integer integer : configList) {
			set.add(integer);
		}
		return set.size() == processorNumber;
	}

	/**
	 * ������Ӳ�����õ�level���Ĺ���Լ����Ϣ
	 * 
	 * @param configList
	 * @param HashMap<String,
	 *            ArrayList<Tuple>> onstraints ����level���Ĺ���Լ����Ϣ
	 * @param hardwareNumber
	 *            ��ָӲ����������tuple�д洢�����id��ȥӲ��������
	 * @return false/true ���㹲��Լ��������true,���򷵻�false
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
	 * ������Ӳ�����õķ���������Լ����Ϣ
	 * 
	 * @param configList
	 * @param HashMap<String,
	 *            ArrayList<Tuple>> onstraints ����level���Ļ���Լ����Ϣ
	 * @param hardwareNumber
	 *            ��ָӲ����������tuple�д洢�����id��ȥӲ��������
	 * @return false/true ���㻥��Լ��������true,���򷵻�false
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
	 * ������Ӳ�����õ�ģ�鼶����Լ����Ϣ
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
	 * ������Ӳ�����õ�ģ�鼶����Լ����Ϣ
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
