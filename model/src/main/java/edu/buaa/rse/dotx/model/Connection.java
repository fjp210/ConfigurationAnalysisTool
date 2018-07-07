package edu.buaa.rse.dotx.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("connection")
public class Connection extends Element{
	Feature source;
	Feature destination;
	
	public Connection(String name) {
		super(name);
	}

	public Feature getSource() {
		return source;
	}

	public void setSource(Feature source) {
		this.source = source;
	}

	public Feature getDestination() {
		return destination;
	}

	public void setDestination(Feature destination) {
		this.destination = destination;
	}
	
	public Component getSourceComponent(){
		return (Component) this.getSource().getParent();
	}
	
	public Component getTargetComponent(){
		return (Component) this.getDestination().getParent();
	}
}
