package edu.buaa.rse.dotx.worker.digester.util;

import java.util.ArrayList;
import java.util.List;

import edu.buaa.rse.dotx.worker.digester.Digester;
import edu.buaa.rse.dotx.worker.digester.impl.BaseDigester;

public class DigesterFactory {

	public static List<Digester> getAllDigesters(){
		List<Digester> digesterList = new ArrayList<Digester>();
		try {
			Class digester = Class.forName("edu.buaa.rse.dotx.digester.RTCDigester");
			Digester obj = (Digester) digester.newInstance();
			digesterList.add(obj);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return digesterList;
	}
}
