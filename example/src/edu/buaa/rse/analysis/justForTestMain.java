package edu.buaa.rse.analysis;

import java.io.IOException;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import edu.buaa.rse.dotx.model.util.RuntimeConstants;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.Connection;
import edu.buaa.rse.dotx.model.Feature;
import edu.buaa.rse.dotx.model.Property;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.modelCombination.Constraint;
import edu.buaa.rse.modelCombination.TransformUtility;
import edu.buaa.rse.modelCombination.ParameterModifier;
import edu.buaa.rse.modelCombination.TaskSequence;
import ima_intercomplexity.Complexity;

public class justForTestMain {
	public static void main(String[] args) {
		String deployURL = "";
		String modelURL = "C:\\Users\\fjp21\\Documents\\parameterInput-small";
		DegestInstance.calculation(deployURL, modelURL);
		System.out.println("end");
	
		//test 状态码转换
		//int softwareNumber = 15;
		//int processorNumber = 8;
		//long num = 15617; 
		//LinkedList<Integer> test = new LinkedList<Integer>();
		//test = Integer2Status.number2Status(num, softwareNumber, processorNumber);
		//System.out.println("test: " + test);
	}



	
	
	
	/***
	 * 测试 系统 交互复杂度
	 * 
	 * @param graph
	 *            图
	 * @param softwareModel
	 *            软件model
	 * @param configurationList
	 *            软硬件匹配列表
	 */
	private static void testInterComplexity(Graph graph, edu.buaa.rse.dotx.model.SystemModel softwareModel,
			LinkedList<Integer> configurationList) {
		ArrayList<Integer> configurationArrayList = new ArrayList<Integer>(configurationList);
		int processorNumber = graph.getProcessorNumber();

		Complexity complex = new Complexity();
		complex.connection.SoftConnectAssignment(softwareModel);// 将软件连接关系xml读入软件连接关系矩阵
		complex.configuration.Assignment(configurationArrayList, processorNumber);// 根据软硬件匹配关系列表和硬件数量转化出软硬件匹配关系矩阵
		double syscomplexnum = complex.systemComplexity(processorNumber);// 交互复杂度计算

		//int softNum = softwareModel.getComponents().size();// 软件连接关系矩阵输出测试
		// for(int i=0;i<softNum;i++){
		// System.out.print(complex.connection.SoftConnection.get(i));
		// System.out.println("\n");
		// }
		// System.out.println("\n");

		// for(int i=0;i<processorNumber;i++){//软硬件匹配关系矩阵输出测试
		// System.out.print(complex.configuration.Configuration.get(i));
		// System.out.println("\n");
		// }

		//System.out.println("============================");
		//System.out.printf("%.5f", syscomplexnum);// 交互复杂度结果输出
	}

}
