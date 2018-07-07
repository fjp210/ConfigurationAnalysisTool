package edu.buaa.rse.dotx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("feature")
public class Feature extends Element{
	@XStreamAsAttribute
	String category;

	public Feature(String name) {
		super(name);
	}

	
	public static class FeatureCategory{
		public static String DATA = "data";
		public static String EVENT = "event";
	}
}
