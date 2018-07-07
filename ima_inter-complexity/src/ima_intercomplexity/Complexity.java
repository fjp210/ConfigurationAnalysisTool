package ima_intercomplexity;

import java.util.ArrayList;

//import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.SystemModel;



public class Complexity{
	
	
	public HardSoftConfiguration configuration = new HardSoftConfiguration();	//软硬件匹配关系，读取硬件上部署的软件；
	public SoftConnection connection = new SoftConnection();					//软件间的连接关系
	public Standard_Deviation SD = new Standard_Deviation();					//标准差计算

	/**
	 * 系统交互复杂性计算函数
	 * @param hardnum 硬件个数
	 * @return
	 */
	public float systemComplexity(int hardnum){	
		float  systemcomOut = 0;
		float  systemcomIn = 0;
		ArrayList<Integer> hardcomout = new ArrayList<>();
		ArrayList<Integer> hardcomin = new ArrayList<>();
		for(int i=0;i<hardnum;i++)
		{
			ArrayList<Integer> softID =new ArrayList<>();
			softID =configuration.getSoftID(i);
			hardcomout.add(connection.getOneHardComOut(softID));
			hardcomin.add(connection.getOneHardComIn(softID));	
		}
		systemcomOut = (float)SD.varianceValue(hardcomout);
		systemcomIn = (float)SD.varianceValue(hardcomin);
		return systemcomOut+systemcomIn;
	}

	public static void main(String[] args) {
		
		
		ArrayList<Integer>hscode = new ArrayList<Integer>();//手写的一个软硬件匹配关系列表输入
		hscode.add(2);
		hscode.add(2);
		hscode.add(1);
		hscode.add(0);
		hscode.add(0);
		int hardnum = 3;
/*		for(int i=0;i<5;i++){
			System.out.print(hscode.get(i));
		}
		System.out.println("\n");
		
		
		
		
*/		Complexity complex=new Complexity();
		String softwareXml = "C:/Users/cuanmiaomiao/Desktop/softwareModel.xml";//读入软件连接关系xml
		SystemModel softwareModel = ReadXML.readXML(softwareXml);//将xml转换成类
		
		complex.connection.SoftConnectAssignment(softwareModel);//将软件连接关系xml读入软件连接关系矩阵
		complex.configuration.Assignment(hscode,hardnum);//根据软硬件匹配关系列表和硬件数量转化出软硬件匹配关系矩阵
		double syscomplexnum = complex.systemComplexity(hardnum);//交互复杂度计算
		
		
		int softNum= softwareModel.getComponents().size();//软件连接关系矩阵输出测试
		for(int i=0;i<softNum;i++){
			System.out.print(complex.connection.SoftConnection.get(i));
			System.out.println("\n");
		}
		System.out.println("\n");
		
		
		for(int i=0;i<hardnum;i++){//软硬件匹配关系矩阵输出测试
			System.out.print(complex.configuration.Configuration.get(i));
			System.out.println("\n");
		}
		
		System.out.println("============================");
		System.out.printf("%.5f", syscomplexnum);//交互复杂度结果输出
	}
}
