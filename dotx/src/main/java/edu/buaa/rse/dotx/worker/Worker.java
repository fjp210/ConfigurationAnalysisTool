package edu.buaa.rse.dotx.worker;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.queue.Queue;
import edu.buaa.rse.dotx.task.Task;
import edu.buaa.rse.dotx.worker.digester.Digester;

public class Worker {
	private int workerId;
	private List<Digester> digsters = new ArrayList<Digester>();
	private static Worker _instance;
	private Queue queue;
	protected Logger logger = Logger.getLogger(this.getClass());
	public int getWorkerId(){
		return this.workerId;
	}
	public static Worker getInstance(){
		if(_instance==null){
			synchronized(Worker.class){
				if(_instance == null){
					Worker temp = new Worker();
					temp.init();
					_instance = temp;
				}
			}
		}
		return _instance;
	}
	private void init(){
		//this.workerId = this.genWorkerId();
		this.queue = Queue.getWorkerInstance();
	}
	public void work(){

	}

	private int genWorkerId(){
		return Math.abs(SimpleMacAddress.getMacAddress().hashCode());
	}
	
}
