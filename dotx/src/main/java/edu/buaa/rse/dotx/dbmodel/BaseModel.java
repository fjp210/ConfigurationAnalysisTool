package edu.buaa.rse.dotx.dbmodel;


import javax.persistence.Transient;

import org.hibernate.Session;

import edu.buaa.rse.dotx.dbmodel.util.HibernateUtils;


public class BaseModel {
	
	@Transient
	protected static Session getSession(){
		return HibernateUtils.getSeesion();
	}

}
