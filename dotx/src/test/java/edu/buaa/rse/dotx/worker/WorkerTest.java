package edu.buaa.rse.dotx.worker;

import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;

import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.Connection;
import edu.buaa.rse.dotx.model.Feature;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.dotx.task.Task;

public class WorkerTest {

	public static void main(String[] args) {
		Worker.getInstance().work();
		while(true){
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
