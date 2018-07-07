package edu.buaa.rse.dotx.ftp;

public class FtpTest {

	public static void main(String[] args) {
		FtpAdaptor.getDotxInstance().uploadDir2Dir("/Users/zhangquan07/Documents/workspace_aadl/backend/src/main/webapp/temp", "/bb");
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
