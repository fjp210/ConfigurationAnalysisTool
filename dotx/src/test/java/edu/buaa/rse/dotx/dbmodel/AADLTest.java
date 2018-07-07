package edu.buaa.rse.dotx.dbmodel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.query.Query;

import edu.buaa.rse.dotx.dbmodel.util.HibernateUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class AADLTest {

   public static void main(String[] args) {
       test4();
   }
   
   public static void test4(){
	   SessionFactory factory = HibernateUtils.getSessionFactory();
       Session session = factory.getCurrentSession();
       session.getTransaction().begin();
       TaskModel task = TaskModel.getTask(session, 3);
       //TaskResultModel.getTaskResut(session, 4);
       task.updateStatus();
       
       TaskResultModel taskResult = new TaskResultModel();
       taskResult.setTask(task);
       session.persist(taskResult);
       
       session.persist(task);
       session.getTransaction().commit();
   }
   
   public static void test3(){
	   SessionFactory factory = HibernateUtils.getSessionFactory();
       Session session = factory.getCurrentSession();
       try {
           session.getTransaction().begin();
           TaskModel task = TaskModel.getTask(session, 4);
           Set<JobModel> jobs = task.getJobs();
           for(JobModel job:jobs){
        	   System.out.println(job.getId());
           }
           session.getTransaction().commit();
       } catch (Exception e) {
           e.printStackTrace();
           session.getTransaction().rollback();
       }
	   
   }
   
   public static void test2(){
	   SessionFactory factory = HibernateUtils.getSessionFactory();
       Session session = factory.getCurrentSession();
       try {
           session.getTransaction().begin();
           TaskModel task = new TaskModel();
           session.persist(task);
           JobModel job = new JobModel();
           job.setTask(task);
           session.persist(job);
           job = new JobModel();
           job.setTask(task);
           session.persist(job);
           session.getTransaction().commit();
       } catch (Exception e) {
           e.printStackTrace();
           session.getTransaction().rollback();
       }
   }
   
   public static void test1(){
	   SessionFactory factory = HibernateUtils.getSessionFactory();
       Session session = factory.getCurrentSession();
       try {
           session.getTransaction().begin();
           String sql = "Select task from " + TaskModel.class.getName() + " task "
                   + " order by task.id ";
           Query<TaskModel> query = session.createQuery(sql);
           List<TaskModel> tasks = query.getResultList();
           for (TaskModel task : tasks) {
               System.out.println("task: " + task.getId());
           }
           session.getTransaction().commit();
       } catch (Exception e) {
           e.printStackTrace();
           session.getTransaction().rollback();
       }
   }

}