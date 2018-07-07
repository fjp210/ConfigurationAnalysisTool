package edu.buaa.rse.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import com.google.gson.JsonObject;

public class BetterMap{
	private long maxNum;
	private TreeMap<ReliabilitySortKey, JsonObject> treeMap;
	
	public BetterMap(long maxNum){
		this.treeMap = new TreeMap<ReliabilitySortKey, JsonObject>(new Comparator<ReliabilitySortKey>() {
			 public int compare(ReliabilitySortKey o1,ReliabilitySortKey o2){
				 if(Math.abs(o1.reliability-o2.reliability)<=0) {
					 return (int)(o1.num - o2.num);
				 }
				 else {
					 return (int)(o1.reliability<o2.reliability?1:-1);
				 }
			 }
		});
		this.maxNum = maxNum;
	}
	
	public Object put(ReliabilitySortKey key, JsonObject value){
		this.treeMap.put(key, value);
		int size = this.treeMap.size();
		long longsize = new Integer(size).longValue(); 
		if(longsize > this.maxNum){
			this.treeMap.pollFirstEntry();
		}
		return value;
	}
	
	public JsonObject get(ReliabilitySortKey key){
		return this.get(key);
	}
	
	public Object[] values(){
		Collection<JsonObject> col = this.treeMap.values();
		return col.toArray();
	}
	

	
}
