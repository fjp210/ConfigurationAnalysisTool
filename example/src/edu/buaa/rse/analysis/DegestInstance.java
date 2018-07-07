package edu.buaa.rse.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.model.deploy.Deploy;
import edu.buaa.rse.dotx.model.util.RuntimeConstants;
import edu.buaa.rse.modelCombination.AnalyzingContentReader;
import edu.buaa.rse.modelCombination.Constraint;
import edu.buaa.rse.modelCombination.ModelCombination;
import edu.buaa.rse.modelCombination.ParameterModifier;
import edu.buaa.rse.modelCombination.TransformUtility;
import edu.buaa.rse.util.BetterMap;
import edu.buaa.rse.util.ReliabilitySortKey;
import edu.buaa.rse.modelCombination.TaskSequence;
import ima_intercomplexity.Complexity;
/**
 * �����Ǽ�����õĺ�����
 * @author ����
 *
 */
public class DegestInstance {
	/**
	 * 
	 * @param jobDir ���ò��Ե������ַ
	 * @param modelURL ��������xml�ļ���Դ�ĵ�ַ
	 * @return
	 * TODO 1�����Ӽ��������޸ģ� 2����������
	 */
	public static JsonElement calculation(String jobDir, String modelURL) {
		
		// �½�Graph
		Graph graph = new Graph();
		// ��ȡӲ��ģ��
		//SystemModel hardwareModel = TransformUtility.xML2SystemModel(modelURL + "//hardwareModel.xml");
				
		String hardwareXml = modelURL + "//hardwareModel.xml";
		String softwareXml = modelURL + "//softwareModel.xml";
		String systemXml = modelURL + "//systemModel.xml";
		String aadlComponentMessageXml = modelURL + "//AADLComponentMessage.xml";
		String connectionXml = modelURL + "//AADLLinkMessage.xml";
		String calutationXml = modelURL + "//AnalyzingContentInfo.xml";
		
		// ��ȡ���ģ��
		SystemModel softwareModel = TransformUtility.xML2SystemModel(softwareXml);
		//��Ӳ��ģ�ͺϲ�
		ModelCombination.generateSystem(hardwareXml, softwareXml, modelURL);
				
		//�����Ĳ����޸�
		ParameterModifier modifier = new ParameterModifier(systemXml, aadlComponentMessageXml, connectionXml);
		modifier.modifyComponent();
		//���Ӽ��Ĳ����޸��ݲ�������
		modifier.modifyConnection();
				
		//����systemModel����
		SystemModel systemModel = TransformUtility.xML2SystemModel(systemXml);
		
		//����
		// ����Graph����
		graph.getSystemNode(systemModel);
		// ��ȡԼ��xml�ļ�
		HashMap<String, ArrayList<Tuple>> cnstr = Constraint.allKindsOfConstraintMap(
				Constraint.constraintReader(modelURL + "//AADLLinkconstraint.xml", systemModel), "Partition");
		//System.out.println("test: " + modelURL + "//AADLLinkconstraint.xml");
		
		// ��ȡ������xml�ļ�,��ȡ������taskArrayList,����������ֵruntimeThresholdValue
		ArrayList<TaskSequence> alts = TransformUtility.taskChainReader(modelURL + "//AADLTaskSequence.xml");
		ArrayList<LinkedList<Integer>> taskArrayList = TransformUtility.getTaskSequence(alts, systemModel);
		ArrayList<Integer> runtimeThresholdValue = TransformUtility.getRuntimeThresholdValue(alts);
		
		// ��ȡ����ѡ���XML�ļ�����ȡ��Ҫ���������list
		ArrayList<Integer> needCalculationList = new ArrayList<Integer>();
		needCalculationList = AnalyzingContentReader.read(calutationXml);
		
		System.out.println("needCalculationList: " + needCalculationList);
		
//		 ��ȡdeploy
		//Deploy deploy = TransformUtility.xml2Deploy(jobDir+"//deploy.xml");
		// �������÷������γ�ѭ��
		//long begin = deploy.getAppToPartion().getBegin();
		//long end = deploy.getAppToPartion().getEnd();
		//long maxResult = deploy.getAppToPartion().getMaxresult();

		
		
		
//		//����
//		
//		int aSize = 5;
//		int pSize = 2;
//		int zhishu = aSize-1;
//		long totalDeloySize = 0;
//		if (pSize > 2) {
//			for(int xishu=1; xishu < pSize-1 && zhishu >= 0; xishu++) {
//				if (xishu == pSize-2) {
//					xishu++;
//				}
//				long tempDeploySize = xishu * (long)Math.pow(pSize, zhishu);
//				System.out.println("test1: " + tempDeploySize);
//				totalDeloySize += tempDeploySize;
//				zhishu--;			
//			}
//		}else {
//			totalDeloySize = (long) Math.pow(pSize,aSize);
//		}
//		
//		System.out.println("test: " + totalDeloySize);
		long begin = 0;
		long end = 8*8*8*8;
		long maxResult = 1000;
	
		
		long num = end - begin;
		//long beginTime = System.currentTimeMillis();
		
		BetterMap bm = new BetterMap(maxResult);
		//int goodDeplyNum = 0;
		int hardwareNumber = graph.getHardwareNumber();
		int processorNumber = graph.getProcessorNumber();
		int softwareNumber = graph.getSoftwareNumber();
		
		long start_time = System.currentTimeMillis();
		int count=0;
		for (long i = begin; i <= end; i++) {					

			JsonObject jsonObject = new JsonObject();
			JsonObject eachJsonObject = new JsonObject();
			
			LinkedList<Integer> configurationList = Integer2Status.number2Status(i, graph.getSoftwareNumber(),
					graph.getProcessorNumber());
//			boolean coexistResult = Integer2Status.partitionCoexistConstraintHandle(configurationList, cnstr,
//					graph.getHardwareNumber());
			/**
			 * @author Li Yaonan
			 * @description �����Ϲ���Լ����״̬����
			 */
		
			long old_i = i;
			//long temp_i = i;
			boolean no_change_Coexist = false;
			boolean no_change_Repulsive = false;
			while(true)
			{
				if(cnstr != null && cnstr.get("Coexist") != null) {
					for (Tuple tuple : cnstr.get("Coexist")) {
						//��Ӧ���λ���ϵ�λ�ñ����ͬ�����������ͬһ��λ��
						int firstSoftwarePosition = tuple.first - hardwareNumber;
						int secondSoftwarePosition = tuple.second - hardwareNumber;
						if(configurationList.get(firstSoftwarePosition) != 
							configurationList.get(secondSoftwarePosition)){
								long delatime = firstSoftwarePosition > secondSoftwarePosition ? firstSoftwarePosition:secondSoftwarePosition;
								//i+=(int)Math.pow(processorNumber, softwareNumber-1-delatime);
								int neededToAdd = (int)Math.pow(processorNumber, softwareNumber-1-delatime);
								i = (i/neededToAdd+1)*neededToAdd;
//								i += 1;
								configurationList = Integer2Status.number2Status(i, graph.getSoftwareNumber(),
										graph.getProcessorNumber());
						}
					}
				}
				
				if(old_i != i) {
					no_change_Coexist = false;
				}
				else if (i >= end) {
					break;
				}
				else {
					no_change_Coexist = true;
				}
				
				old_i = i;
		
			
			/**
			 * @author Li Yaonan
			 * @description �����ϻ���Լ����״̬����
			 */
				if(cnstr != null && cnstr.get("Repulsive") != null) {
					for (Tuple tuple : cnstr.get("Repulsive")) {
						//��Ӧ���λ���ϵ�λ�ñ����ͬ�����������ͬһ��λ��
						int firstSoftwarePosition = tuple.first - hardwareNumber;
						int secondSoftwarePosition = tuple.second - hardwareNumber;
						if(configurationList.get(firstSoftwarePosition) == 
								configurationList.get(secondSoftwarePosition)) {
						//i+=(int)Math.pow(processorNumber, Math.min(softwareNumber-1-firstSoftwarePosition, softwareNumber-1-secondSoftwarePosition));
							int neededToAdd =(int)Math.pow(processorNumber, Math.min(softwareNumber-1-firstSoftwarePosition, softwareNumber-1-secondSoftwarePosition));
							i = (i/neededToAdd+1)*neededToAdd;
//							i += 1;
							configurationList = Integer2Status.number2Status(i, graph.getSoftwareNumber(),
									graph.getProcessorNumber());
						}
					}
				}
				if(old_i != i) {
					no_change_Repulsive = false;
				}
				else if (i >= end) {
					break;
				}
				else {
					no_change_Repulsive = true;
				}
				
				old_i = i;
				
				if(no_change_Coexist && no_change_Repulsive)
				{
					break;
				}
			}		
			if(i >= end) {
				break;
			}
			
			//System.out.println("======Begin One Deploy:[" + i + "]=========");
			//System.out.println("[[[[[[[[[[[[[[[Job process:" + i+"/"+ num + "]]]]]]]]]]]]]]");
			//System.out.println("[[[[[[[[[[[[[[[Job consumered:" + duration/60/1000+"min" + "]]]]]]]]]]]]]]");
			//Լ���ж�
//			System.out.println("======Deplay:[" + i + "] coexistResult is ["+ coexistResult+"]=========");
//			if (coexistResult && exclustionResult) {
				//goodDeplyNum++;
				Graph calGraph = (Graph) graph.clone();
				calGraph.addSoftToHardEdge(calGraph.getProcessorIdList(), configurationList);
	
				//����������
				float reliability = 0;
				float interactionComplexity = 0;
				JsonObject schedulability = new JsonObject();
				
				//float schedulability = 0;
				//HashMap<String, Integer> schedulabilityMap = new HashMap<String, Integer>();
				//System.out.println(Integer2Status.number2Status(i, softwareNumber, processorNumber));
				
				JsonObject runtime = new JsonObject();
				
				//�ɵ����Լ��㣬������ɵ�����ֱ��breakѭ��
				if(needCalculationList.get(3) == 1) {
					HashMap<Integer, ArrayList<ArrayList<Integer>>> infoMap = SchedulabilityMethods.preprocessing(calGraph, configurationList);
					boolean schedulabilitySign = SchedulabilityMethods.calSchedulability(calGraph, infoMap);
					if (schedulabilitySign) {
						Iterator iter = infoMap.entrySet().iterator();
						while(iter.hasNext()) {
							Map.Entry entry = (Map.Entry)iter.next();
							Integer key = (Integer) entry.getKey();
							ArrayList<ArrayList<Integer>> val = (ArrayList<ArrayList<Integer>>) entry.getValue();
														
							JsonObject processorObject = new JsonObject();
							//processorObject.addProperty("processorId", key);
							for (int proc=0; proc < val.size(); proc++) {
								JsonObject processObject = new JsonObject();
								//processObject.addProperty("processId", val.get(proc).get(0));
								processObject.addProperty("startingTime", val.get(proc).get(1));
								processObject.addProperty("basicExecutingTime", val.get(proc).get(2));
								processObject.addProperty("ProcessPeriod", val.get(proc).get(3));
								processorObject.add(val.get(proc).get(0).toString(), processObject);
							}	
							schedulability.add(key.toString(), processorObject);
						}
						System.out.println("reliability: " + Integer2Status.number2Status(i, softwareNumber, processorNumber));
						System.out.println("���ȷ����� "+ schedulability.toString());
						
					}else {									
						continue;
					}
				}
				eachJsonObject.add("schedulability", schedulability);				
				count++;
				
				
				// �ɿ��Լ���
				if (needCalculationList.get(0) == 1) {
					reliability = Calculation.calReliabilityAll(calGraph);
				}else {
					reliability = -1;
				}
				eachJsonObject.addProperty("reliability", reliability);

				
				// �������Ӷȼ���
				if(needCalculationList.get(2) == 1) {
					Complexity complex = new Complexity();
					ArrayList<Integer> conf = new ArrayList<Integer>(configurationList);
					// ��������ӹ�ϵxml����������ӹ�ϵ����
					complex.connection.SoftConnectAssignment(softwareModel);
					// ������Ӳ��ƥ���ϵ�б��Ӳ������ת������Ӳ��ƥ���ϵ����
					complex.configuration.Assignment(conf, calGraph.getProcessorNumber());
					// �������Ӷȼ���
					float syscomplexnum = complex.systemComplexity(calGraph.getProcessorNumber());
					interactionComplexity = syscomplexnum;
				}else {
					interactionComplexity = -1;
				}
				eachJsonObject.addProperty("interactionComplexity", interactionComplexity);
				
	
				// ����ʵʱ�Լ���
				if (needCalculationList.get(1) == 1) {				
					ArrayList<Integer> runtimeResult = new ArrayList<Integer>();
					
					ArrayList<LinkedList<Integer>> taskArrayListClone = new ArrayList<LinkedList<Integer>>();
					for (LinkedList<Integer> linkedList : taskArrayList) {
						LinkedList<Integer> linkedListClone = new LinkedList<Integer>();
						for (Integer integer : linkedList) {
							Integer integerClone = new Integer(integer.intValue());
							linkedListClone.add(integerClone);
						}
						taskArrayListClone.add(linkedListClone);
					}
					runtimeResult = Calculation.calTaskRuntime(calGraph, taskArrayListClone);
					// ����ʱ��������Ҫ���ʱ��ıȽ�
					boolean pass = true;
					for (int j = 0; j < runtimeThresholdValue.size(); j++) {
						
						pass = runtimeResult.get(j) <= runtimeThresholdValue.get(j);
						
						JsonObject tempObject = new JsonObject();
						tempObject.addProperty("runtimeThresholdValue", runtimeThresholdValue.get(j));
						tempObject.addProperty("runtimeResult", runtimeResult.get(j));
						tempObject.addProperty("pass", pass?1:0);
						
						// ��������ʱ����ֵ�Ͳ�������ֵ�����
						if(!pass) {
							runtime.addProperty("totalPass", 0);
						}
						
						runtime.add("" + j, tempObject);
					}
					if(!runtime.has("totalPass"))
					{
						runtime.addProperty("totalPass", 1);
					}
				}else {
					runtime.addProperty("totalPass", -1);
				}
				
				eachJsonObject.add("runtime", runtime);
				System.out.println("����ִ��ʱ���������� " + runtime);
				//���������
								
				jsonObject.add(String.valueOf(i), eachJsonObject);
				//System.out.println("eachJsonObject: " + eachJsonObject.toString());
				ReliabilitySortKey key = new ReliabilitySortKey();
				key.num = i;
				key.reliability = reliability;
				
				bm.put(key, jsonObject);

				//jsonArray.add(jsonObject);
				//System.out.println("======End Good Deploy:[" + goodDeplyNum + "]=========");
				//System.out.println(Integer2Status.number2Status(i, softwareNumber, processorNumber));

//			}			
			//System.out.println("======End One Deploy:[" + i + "]=========");
		
				System.out.println("count: " + count);
		}
		
		long end_time = System.currentTimeMillis();
		System.out.print("time: ");
		System.out.println(end_time - start_time);
		
		Object[] values = bm.values();
		JsonArray jsonArray = new JsonArray();
		for(int i=0; i<values.length; i++){
			jsonArray.add((JsonObject)values[i]);
		}
		return jsonArray;
	}
}
