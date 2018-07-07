package edu.buaa.rse.analysis;



import edu.buaa.rse.dotx.model.Base;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.SystemModel;
/**
 * ����һ�����ڴ洢Լ����Ԫ����
 * @author ����
 *
 */
public class Tuple extends Base{
	public int first;
	public int second;
	public Tuple(int a, int b){
		this.first = a;
		this.second = b;
	}
	public void setTuple(int a, int b){
		this.first = a;
		this.second = b;
	}
	@Override
	public boolean equals(Object obj) {
		Tuple tuple = (Tuple)obj;
		if (this.first == tuple.first && this.second == tuple.second) {
			return true;
		}else {
			return false;
		}
	}
}
