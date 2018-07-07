package ima_intercomplexity;

import java.util.ArrayList;

import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.SystemModel;

public class SoftConnection {


	public ArrayList<ArrayList<Integer>> SoftConnection=new ArrayList<ArrayList<Integer>>();
	
	public void SoftConnectAssignment (SystemModel softwareModel)//读取软件连接关系xml文件，复制到连接关系数组
	{

		int softNum =softwareModel.getComponents().size();
		for(int i=0;i<softNum;i++){
			ArrayList<Integer> OutSoftID=new ArrayList<Integer>();
			for(int j=0;j<softNum;j++){
				OutSoftID.add(0);
			}
			SoftConnection.add(OutSoftID);
		}
		
		for(int i=0;i<softwareModel.getConnections().size();i++){

			Component Outcomponent = (Component)softwareModel.getConnections().get(i).getDestination().getParent();
			Component Incomponent = (Component)softwareModel.getConnections().get(i).getSource().getParent();
			int InsoftID = Incomponent.getComponentId();	
			ArrayList<Integer> OutSoftIDs = new ArrayList<Integer>();
			OutSoftIDs = SoftConnection.get(InsoftID);
			OutSoftIDs.set(Outcomponent.getComponentId(),1);
			SoftConnection.set(InsoftID,OutSoftIDs);
		}
	}

	/**
	 * 计算列表中软件(单个硬件上软件)到所有不在该列表中软件的出度和
	 * @param softIDs
	 * @return
	 */
	public int getOneHardComOut (ArrayList<Integer> softIDs)
	{
		int hardcomout =0;
		int softnumfromhard=softIDs.size();
		int softnum=SoftConnection.size();
		for(int i=0;i<softnumfromhard;i++){
			for(int j=0;j<softnum;j++){
				if(SoftConnection.get(softIDs.get(i)).get(j)!=0 && 
						!(softIDs.contains(j))){
					hardcomout++;
				}
			}
		}
		return hardcomout;
	}
	
	
	/**
	 * 计算列表中软件(单个硬件上软件)到所有不在该列表中软件的入度和
	 * @param softIDs 硬件上所有软件列表
	 * @return 硬件的入度和
	 */
	public int getOneHardComIn (ArrayList<Integer> softIDs)
	{
		int hardcomin =0;
		int softnumfromhard=softIDs.size();
		int softnum=SoftConnection.size();
		for(int i=0;i<softnumfromhard;i++){
			for(int j=0;j<softnum;j++){
				if(SoftConnection.get(j).get(softIDs.get(i))!=0 && !(softIDs.contains(j)))
				{
					hardcomin++;
				}
			}
		}
		return hardcomin;
	}
}
