package edu.buaa.rse.result;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.Configuration;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.modelCombination.TransformUtility;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public class XmlGenerate{

	public static void main(String[] args) {
		
    	//System.out.println("HelloWorld!");

		
    	
    	String xmlPath = "E:\\615software\\temp\\param_input\\systemModel.xml";
		LinkedList<Integer> configList = new LinkedList<Integer>();
	
		configList.add(new Integer(0));
    	configList.add(new Integer(0));
    	configList.add(new Integer(1));
    	configList.add(new Integer(1));
    	configList.add(new Integer(0));
		
		SystemModel systemModel = TransformUtility.xML2SystemModel(xmlPath);
		XmlGenerate.createDocument(systemModel, configList);	
   	}	

	/**
	 * @param input All components
	 * @param category
	 * @param output components of same category
	 */
	public static List<Component> getSameCategory(String category, SystemModel model) {
		List<Component> components = model.getComponents();
		List<Component> result = new ArrayList<Component>();
		for(int i=0; i < components.size(); i++){
			Component component = components.get(i);
			if(component.getCategory().equals(category)){
				result.add(component);
			}else{
				continue;
				}
		}
		return result;
	}
	
	/**
	 * 
	 * the digit represents the module, the position of the digit means the
	 * software
	 * 
	 * @param num
	 * @param softwareNumber
	 * @param processorNumber
	 * @return configurationList 閺勵垵钂嬫禒鑸靛瘻妞ゅ搫绨紒鎴濈暰閸︺劌顕惔鏃傛畱绾兛娆㈡径鍕倞閸ｃ劋绗傞惃鍕拨鐎规艾鍙х化鑽ゆ畱閸掓銆冮敍锟�
	 *         濮濄倖妞傞惃鍒nfigurationList閺勵垱婀径鍕倞缁撅附娼弶鈥叉閻ㄥ嫸绱濋崪宀冭拫绾兛娆㈢紓鏍у娇閻ㄥ嫸绱� 娴溿倓绨版径宥嗘絽鎼达箑褰叉禒銉ф纯閹恒儰濞囬悽銊ｏ拷锟�
	 */
  
    public static Document createDocument(SystemModel model, LinkedList<Integer> configList) {
    	Document document = DocumentHelper.createDocument();
    	//鐠佲剝鏆�
    	int number1 = XmlGenerate.getSameCategory("PROCESSOR",model).size();
    	int number2 = XmlGenerate.getSameCategory("PROCESS",model).size();
    	//閻㈢喐鍨氬ù瀣槸鏉烆垳鈥栨禒璺侯嚠鎼存柨鍙х化鑽ゆ畱list
//    	List<Integer> integers = new ArrayList<Integer>();
//    	integers.add(new Integer(0));
//    	integers.add(new Integer(0));
//    	integers.add(new Integer(1));
//    	integers.add(new Integer(1));
//    	integers.add(new Integer(0));
    	List<Integer> integers1 = new ArrayList<Integer>();
    	List<Integer> integers2 = new ArrayList<Integer>();
    	for (int i = 0; i < configList.size(); i++) {
    		if (configList.get(i) == 0) {
				int idx = i;
				integers1.add(new Integer(idx));
			} else if(configList.get(i) == 1){
				int idx1 = i;
				integers2.add(new Integer(idx1));
			}
		}
    	List<List<Integer>> allIntegersLists = new ArrayList<List<Integer>>();
    	allIntegersLists.add(new ArrayList<Integer>(integers1));
    	allIntegersLists.add(new ArrayList<Integer>(integers2));
    	//鐠佹崘顓竫ml閺嶇厧绱�
    	Element schema = document.addElement( "schema" );
    	for (int j = 0; j < number1; j++) {
    		String moduleId = String.valueOf(XmlGenerate.getSameCategory("PROCESSOR",model).get(j).getName());
    		List<Integer> partitionList = allIntegersLists.get(j);

            Element element1= schema.addElement("element1").addAttribute("name", moduleId);
            Element annotation1 = element1.addElement("annotation1");
            annotation1.addElement("documentation1").setText("Module Configuration");
          	Element complexType1 = element1.addElement( "complexType" );
            Element sequence1 = complexType1.addElement("sequence1");
            
            for (int k = 0; k < partitionList.size(); k++) {
            	String partitionId = String.valueOf(XmlGenerate.getSameCategory("PROCESS",model).get(k).getName());
    			
	            Element element2 = sequence1.addElement("element2").addAttribute("name", partitionId).addAttribute("type", "PartitionsType");
	            Element annotation2 = element2.addElement("annotation2");
	            annotation2.addElement("documentation2").setText("Partition Executed on the Module");
    		}
    		
	        Element element3 = sequence1.addElement("element3").addAttribute("name", "Schedules").addAttribute("type", "SchedulesType");
	        Element annotation3 = element3.addElement("annotation3");
	        annotation3.addElement("documentation3").setText("Schedule for the Module");
	        Element attribute1 = complexType1.addElement("attribute1").addAttribute("name", "Name").addAttribute("type", "NameType").addAttribute("use", "required");
	        Element annotation4 = element3.addElement("annotation4");
	        annotation4.addElement("documentation4").setText("Name of the Module");
	    	
		}
    	OutputFormat format = OutputFormat.createPrettyPrint();
        //鐠佸墽鐤唜ml閺傚洦銆傞惃鍕椽閻椒璐焨tf-8
        format.setEncoding("utf-8");
        Writer out = null;
        try {
        	//閸掓稑缂撴稉锟芥稉顏囩翻閸戠儤绁︾�电钖�
        	out = new FileWriter("E:\\615software\\temp\\new.xml");
        	//閸掓稑缂撴稉锟芥稉鐚無m4j閸掓稑缂搙ml閻ㄥ嫬顕挒锟�
        	XMLWriter writer = new XMLWriter(out, format);
        	//鐠嬪啰鏁rite閺傝纭剁亸鍝緊c閺傚洦銆傞崘娆忓煂閹稿洤鐣剧捄顖氱窞
        	writer.write(document);
        	writer.close();
        	System.out.print("end!");
       		} catch (IOException e) {
       			System.out.print("failure");
       			e.printStackTrace();
        	}finally{
              if (out!=null) {
              try {
            	  out.close();
              } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
          }
      }       	
	return document;
    }
    
}


