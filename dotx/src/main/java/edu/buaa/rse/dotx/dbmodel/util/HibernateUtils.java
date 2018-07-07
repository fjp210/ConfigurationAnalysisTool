package edu.buaa.rse.dotx.dbmodel.util;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import edu.buaa.rse.dotx.config.Config;

public class HibernateUtils {

	//定义静态的SessionFactory，产生单例，只生成一个SessionFactory
	private static final SessionFactory sessionFactory = buildSessionFactory();

	//用来初始化SessionFactory
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure(new File(Config.getInstance().getConfigBaseDir()+"/hibernate.cfg.xml")).buildSessionFactory();
        }catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    //得到SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;//返回SessionFactory的对象
    }
    
    public static Session getSeesion(){
    	return getSessionFactory().getCurrentSession();
    }

}
