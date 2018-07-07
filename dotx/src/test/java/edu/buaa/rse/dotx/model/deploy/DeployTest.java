package edu.buaa.rse.dotx.model.deploy;

import java.io.FileOutputStream;

import com.thoughtworks.xstream.XStream;

import edu.buaa.rse.dotx.constant.DotxRuntime;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.Connection;
import edu.buaa.rse.dotx.model.Feature;
import edu.buaa.rse.dotx.model.SystemModel;
import edu.buaa.rse.modelCombination.TransformUtility;

public class DeployTest {

	public static void main(String[] args) {
		TransformUtility.xml2Deploy("/Users/zhangquan07/Documents/workspace_aadl/dotx/workspace_agent/job/24/deploy.xml");

	}
	
	public static void saveDeployToFile(){
	}

}
