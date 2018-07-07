package ima_intercomplexity;

import java.util.ArrayList;

public class Standard_Deviation {
	
	ArrayList<Integer> allNumber = new ArrayList<>();
	
	/**
	 * 计算一个列表的平均值
	 * @param allNumber 一个列表
	 * @return 平均值
	 */
	public double meanValue(ArrayList<Integer>  allNumber) {
	    double value = 0;
	    for (int i = 0; i < allNumber.size(); i++) {
	        value = value + allNumber.get(i);
	    }
	    value /= allNumber.size();
	    return value;
	}
	
	/**
	 * 计算一个列表的标准差
	 * @param allNumber 一个列表
	 * @return 标准差
	 */
	public double varianceValue(ArrayList<Integer>  allNumber) {
	    double value = 0;
	    double variance = meanValue(allNumber);
	    for (int i = 0; i < allNumber.size();i++)
	    {
	        double x = (allNumber.get(i) - variance) * (allNumber.get(i) - variance);
	        value += x;
	    }
	    value /= allNumber.size();
	    value= Math.sqrt(value);;
	    return value;
	}
}
