package edu.buaa.rse.dotx.zip;

import java.io.File;

import edu.buaa.rse.dotx.agent.DotxAdaptor;

public class ZipTest {

	public static void main(String[] args) {
		String zipFile = "/Users/zhangquan07/Documents/workspace_aadl/backend/src/main/webapp/test/bb.zip";
		String dir = new File(zipFile).getParent();
		ZipUtil.unzip(zipFile, dir);
		String todo = null;
		File file = new File(dir);
		File[] files = file.listFiles();
		for(int i=0; i<files.length; i++){
			File current = files[i];
			if(current.isDirectory()){
				current.renameTo(new File(dir+"/target"));
			}
		}
	}

}
