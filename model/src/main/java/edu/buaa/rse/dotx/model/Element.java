package edu.buaa.rse.dotx.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class Element extends Base{
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	String name;
	Element parent;
	SystemModel system;
	public List<Property> properties = new ArrayList<Property>();
	
	public String getProperty(String key){
		for(Property each : this.properties){
			if (each.key.equalsIgnoreCase(key)) {
				return each.value;
			}
		}
		return null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Element getParent() {
		return parent;
	}

	public void setParent(Element parent) {
		this.parent = parent;
	}

	public SystemModel getSystem() {
		return system;
	}

	public void setSystem(SystemModel system) {
		this.system = system;
	}
	protected Element(){
		
	}
	
	protected Element(String name){
		this.name = name;
	}

	public Element addProperty(Property property){
		this.properties.add(property);
		return this;
	}
	
	public Element addElement(Element element){
		element.setParent(this);
		element.setSystem(this.getSystem());
		return this;
	}
	
	public Object clone(){
		ByteArrayOutputStream baOut = new ByteArrayOutputStream();
		ObjectInputStream objIn;
		try{
			ObjectOutputStream objOut = new ObjectOutputStream(baOut);
			objOut.writeObject(this);
			ByteArrayInputStream baIn = new ByteArrayInputStream(baOut.toByteArray());
			objIn =	new ObjectInputStream(baIn);
			return objIn.readObject();
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException(cnfe);
		}
	}
}
