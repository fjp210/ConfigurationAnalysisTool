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
	
		//test ״̬��ת��
		//int softwareNumber = 15;
		//int processorNumber = 8;
		//long num = 15617; 
		//LinkedList<Integer> test = new LinkedList<Integer>();
		//test = Integer2Status.number2Status(num, softwareNumber, processorNumber);
		//System.out.println("test: " + test);
	}



	
	
	
	/***
	 * ���� ϵͳ �������Ӷ�
	 * 
	 * @param graph
	 *            ͼ
	 * @param softwareModel
	 *            ���model
	 * @param configurationList
	 *            ��Ӳ��ƥ���б�
	 */
	private static void testInterComplexity(Graph graph, edu.buaa.rse.dotx.model.SystemModel softwareModel,
			LinkedList<Integer> configurationList) {
		ArrayList<Integer> configurationArrayList = new ArrayList<Integer>(configurationList);
		int processorNumber = graph.getProcessorNumber();

		Complexity complex = new Complexity();
		complex.connection.SoftConnectAssignment(softwareModel);// ��������ӹ�ϵxml����������ӹ�ϵ����
		complex.configuration.Assignment(configurationArrayList, processorNumber);// ������Ӳ��ƥ���ϵ�б��Ӳ������ת������Ӳ��ƥ���ϵ����
		double syscomplexnum = complex.systemComplexity(processorNumber);// �������Ӷȼ���

		//int softNum = softwareModel.getComponents().size();// ������ӹ�ϵ�����������
		// for(int i=0;i<softNum;i++){
		// System.out.print(complex.connection.SoftConnection.get(i));
		// System.out.println("\n");
		// }
		// System.out.println("\n");

		// for(int i=0;i<processorNumber;i++){//��Ӳ��ƥ���ϵ�����������
		// System.out.print(complex.configuration.Configuration.get(i));
		// System.out.println("\n");
		// }

		//System.out.println("============================");
		//System.out.printf("%.5f", syscomplexnum);// �������ӶȽ�����
	}

}
