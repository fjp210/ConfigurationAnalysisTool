package edu.buaa.rse.dotx.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("component")
public class Component extends Element{
	@XStreamAsAttribute
	public String category;
	int cseq;
	public List<Component> subcomponents = new ArrayList<Component>();
	public List<Feature> features = new ArrayList<Feature>();
	/**
	 * @author ╪яен
	 */
	public int componentId;
	//public List<Property> properties = new ArrayList<Property>();
	
	public Component(String name) {
		super(name);
	}

	public int getCseq() {
		return cseq;
	}

	public void setCseq(int cseq) {
		this.cseq = cseq;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category.getName();
	}
	
	public Component addSubcomponent(Component subcomponent){
		super.addElement(subcomponent);
		this.subcomponents.add(subcomponent);
		return this;
	}
	
	public Component addFeature(Feature feature){
		super.addElement(feature);
		this.features.add(feature);
		return this;
	}
	
	public static class ComponentCategory{
		public static String PARTION = "partion";
		public static String DEVICE = "device";
		public static String BUS = "bus";
		public static String PROCESS = "process";
		public static String PROCESSOR = "processor";
	}
	/**
	 * @author ╪яен
	 */
	public Component addProperty(Property property){
		this.properties.add(property);
		return this;
	}
	public void setComponentCategory(String cat) {
		this.category = cat;
	}
	public String getComponentCategory() {
		return this.category;
	}
	public void setComponentId(int id) {
		this.componentId = id;
	}
	public int getComponentId() {
		return this.componentId;
	}
	public void setName(String na) {
		this.name = na;
	}
	public String getName() {
		return this.name;
	}
	
	public static enum Category{
		PARTION("PROCESSOR"),
		DEVICE("device"),
		BUS("bus"),
		APP("PROCESS");
		private String name;
		Category(String name){
			this.name = name;
		}
		public String getName(){
			return this.name;
		}
		public static Category getCategoryByName(String name){
			Category[] values = Category.values();
			for(int i=0; i<values.length; i++){
				if(values[i].getName().equals(name)){
					return values[i];
				}
			}
			return null;
		}
	}
	
}
