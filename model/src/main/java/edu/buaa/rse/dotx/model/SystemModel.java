package edu.buaa.rse.dotx.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("system")
public class SystemModel extends Element{
	private static final long serialVersionUID = 1L;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	List<Component> components = new ArrayList<Component>();
	List<Connection> connections = new ArrayList<Connection>();
	
	public SystemModel(String name) {
		super(name);
		this.setSystem(this);
	}
	public SystemModel addComponent(Component component){
		super.addElement(component);
		components.add(component);
		return this;
	}
	public SystemModel addConnection(Connection connection){
		super.addElement(connection);
		connections.add(connection);
		return this;
	}
	/**
	 * @author ����
	 * @return
	 */
	public List<Component> getComponents(){
		return this.components;
	}
	public List<Connection> getConnections(){
		return this.connections;
	}
	
	public List<Component> getComponentsByCategory(Component.Category category){
		List<Component> result = new ArrayList<Component>();
		for(int i=0; i<this.components.size(); i++){
			Component component = components.get(i);
			if(category.getName().equals(component.getCategory())){
				result.add(component);
			}
		}
		return result;
	}
	public Component getComponentByCseq(Component.Category category, int cseq){
		Component result = null;
		for(int i=0; i<this.components.size(); i++){
			Component component = components.get(i);
			if(category.getName().equals(component.getCategory())){
				if(component.getCseq() == cseq){
					return component;
				}
			}
		}
		return result;
	}
}
