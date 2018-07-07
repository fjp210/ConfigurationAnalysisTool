package ima_intercomplexity;

import java.io.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.buaa.rse.dotx.model.*;
//import edu.buaa.rse.dotx.agent.util.RuntimeConstants;

public class ReadXML {

	/**
	 * 读取XML文件
	 * @param softwareXml  
	 * @return
	 */
	public static SystemModel readXML(String softwareXml){

		File softwareFile = new File(softwareXml);
		SystemModel softwareModel = new SystemModel("softwareSystem");
		try{
			//读取软件模型的xml文件，生成软件systemModel
			FileInputStream softwareStream = new FileInputStream(softwareFile);
			XStream softwareXStream = new XStream(new DomDriver());
			XStream.setupDefaultSecurity(softwareXStream);
			Class<?>[] classes2 = new Class[] { SystemModel.class, Property.class, Feature.class, Element.class, Connection.class, Component.class};
			softwareXStream.allowTypes(classes2);
			softwareXStream.autodetectAnnotations(true);
			softwareXStream.alias("system", SystemModel.class);
			softwareModel = (SystemModel) softwareXStream.fromXML(softwareStream);
			softwareStream.close();			
		} catch (Exception e){
			e.printStackTrace();
		}	
		return softwareModel;
	}
	
/*	public static void main(String args[]){
		String softwareXml = "C:/Users/cuanmiaomiao/Desktop/softwareModel.xml";
		SystemModel softwareModel = ReadXML.readXML(softwareXml);
		Component component = (Component)softwareModel.getConnections().get(0).getSource().getParent();
		component.getComponentId();
	}*/
}
