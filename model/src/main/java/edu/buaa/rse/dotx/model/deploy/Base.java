package edu.buaa.rse.dotx.model.deploy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Base implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	
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
