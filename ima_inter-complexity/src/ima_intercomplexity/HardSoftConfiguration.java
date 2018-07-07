package ima_intercomplexity;

import java.util.ArrayList;


public class HardSoftConfiguration {

	public ArrayList<ArrayList<Integer>> Configuration=new ArrayList<ArrayList<Integer>>();
	
	/**
	 * 软硬件连接关系数组赋值
	 * @param hscode 软件对应硬件列表
	 * @param hardnum 硬件个数
	 */
	public void Assignment (ArrayList<Integer>hscode,int hardnum)//
	{
		
		for ( int i = 0 ; i < hardnum; i++ ) 
		{  
			ArrayList<Integer> onehardlist =new ArrayList<Integer> ();
			for( int j=0 ; j <hscode.size(); j++) 
			{  
				if(hscode.get(j)==i)
					onehardlist.add(1);
				else
					onehardlist.add(0);
			}  
			Configuration.add(onehardlist);
		}
	}
	
	
	
	/**
	 * 搜索编号为hardname的硬件上的全部软件，输出软件编号列表
	 * @param hardname 
	 * @return
	 */
	public ArrayList<Integer> getSoftID (int hardname)
	{
		ArrayList<Integer> softID = new ArrayList<>();
		int softnum=Configuration.get(hardname).size();
		for(int i=0;i<softnum;i++){
			if(Configuration.get(hardname).get(i)!=0){
				softID.add(i);
			}
		}
		return softID;
	}
}
