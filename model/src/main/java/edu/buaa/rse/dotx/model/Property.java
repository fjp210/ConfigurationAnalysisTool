package edu.buaa.rse.dotx.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("property")
public class Property extends Base{
	@XStreamAsAttribute
	public String key;
	@XStreamAsAttribute
	public String value;
	
	public Property(String key, String value){
		this.key = key;
		this.value = value;
	}
	/**
	 * @author ╪яен
	 */

	
}

