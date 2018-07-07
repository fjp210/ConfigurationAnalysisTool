package edu.buaa.rse.dotx.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import edu.buaa.rse.dotx.config.Config;

public class FtpAdaptor {
	protected static FtpAdaptor _instance;
	private static ThreadLocal<FtpAdaptor> threadInstance = new ThreadLocal<FtpAdaptor>();
	private FTPClient ftpClient;
	private String strIp;
	private int intPort;
	private String user;
	private String password;
	private static Logger logger = Logger.getLogger(FtpAdaptor.class.getName());
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	
	private FtpAdaptor(String strIp, int intPort, String user, String Password) {
		this.strIp = strIp;
		this.intPort = intPort;
		this.user = user;
		this.password = Password;
		this.ftpClient = new FTPClient();
	}
	
	synchronized public static FtpAdaptor getDotxInstance(){
		FtpAdaptor instance = threadInstance.get();
		if(instance==null){
			synchronized(FtpAdaptor.class){
				if(instance == null){
					String ftphost = Config.getInstance().get("ftp.host");
					System.out.println("host:"+ftphost);
					int ftpport = Integer.valueOf(Config.getInstance().get("ftp.port")).intValue();
					String ftpuser = Config.getInstance().get("ftp.user");
					System.out.println("user:"+ftpuser);
					String ftppassword = Config.getInstance().get("ftp.password");
					FtpAdaptor _instance_tmp = new FtpAdaptor(ftphost, ftpport, ftpuser, ftppassword);
					_instance_tmp.ftpLogin();
					System.out.println("succesed login");
					instance = _instance_tmp;
				}
			}
		}
		return instance;
    }

    synchronized private boolean ftpLogin() {
		if (null != this.ftpClient && this.ftpClient.isConnected() && this.ftpClient.isAvailable()) {
			return true;
		}
		boolean isLogin = false;
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		this.ftpClient = new FTPClient();
		this.ftpClient.setControlEncoding("UTF-8");
		this.ftpClient.configure(ftpClientConfig);
		try {
			if (this.intPort > 0) {
				this.ftpClient.connect(this.strIp, this.intPort);
			} else {
				this.ftpClient.connect(this.strIp);
			}
			// FTP服务器连接回答
			int reply = this.ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.ftpClient.disconnect();
				logger.error("登录FTP服务失败！");
				return isLogin;
			}
			this.ftpClient.login(this.user, this.password);
			// 设置传输协议
			this.ftpClient.enterLocalPassiveMode();
			this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			logger.info("恭喜" + this.user + "成功登陆FTP服务器");
			this.ftpClient.setBufferSize(1024 * 2);
			this.ftpClient.setDataTimeout(30 * 1000);
			ftpClient.sendNoOp();
			isLogin = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(this.user + "登录FTP服务失败！" + e.getMessage());
		}
		return isLogin;
	}

	private void ftpLogOut() {
		if (null != this.ftpClient && this.ftpClient.isConnected()) {
			try {
				boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
				if (reuslt) {
					logger.info("成功退出服务器");
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("退出FTP服务器异常！" + e.getMessage());
			} finally {
				try {
					this.ftpClient.disconnect();// 关闭FTP服务器的连接
				} catch (IOException e) {
					e.printStackTrace();
					logger.warn("关闭FTP服务器的连接异常！");
				}
			}
		}
	}
	private String formatString(String str){
		// 将路径中的斜杠统一
        char[] chars = str.toCharArray();
        StringBuffer sbStr = new StringBuffer(256);
        for (int i = 0; i < chars.length; i++) {
            if ('\\' == chars[i]) {
                sbStr.append('/');
            } else {
                sbStr.append(chars[i]);
            }
        }
        str = sbStr.toString();
		return str;
	}
    private String mkFtpDir(String ftpPath) {
    	this.ftpLogin();
    	try {
			ftpClient.cwd("/");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	String result = ftpPath;
        if (!ftpClient.isConnected()) {
            return "";
        }
        try {
        	ftpPath = this.formatString(ftpPath);
            if (ftpPath.indexOf('/') == -1) {
                // 只有一层目录
                ftpClient.makeDirectory(new String(ftpPath.getBytes(), "utf-8"));
                ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes(), "utf-8"));
            } else {
                // 多层目录循环创建
                String[] paths = ftpPath.split("/");
                int length = paths.length;
                if(!ftpPath.endsWith("/")){
                	length--;
                }
                result = "";
                for (int i = 0; i < paths.length; i++) {
                	if(paths[i]==null || "".equals(paths[i]))continue;
                    ftpClient.makeDirectory(new String(paths[i].getBytes(), "utf-8"));
                    ftpClient.changeWorkingDirectory(new String(paths[i].getBytes(), "utf-8"));
                    result = result+"/"+paths[i];
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public boolean uploadFile2Dir(String localFilePath, String remoteDir) {
    	File localFile = new File(localFilePath);
    	String remoteFileName = localFile.getName();
    	return uploadFile(localFilePath, remoteDir, remoteFileName);
    }

	public boolean uploadFile(String localFilePath, String remoteDir, String remoteFileName) {
		this.ftpLogin();
		File localFile = new File(localFilePath);
		BufferedInputStream inStream = null;
		boolean success = false;
		try {
			remoteDir = this.mkFtpDir(remoteDir);
			this.ftpClient.cwd(remoteDir);// 改变工作路径
			inStream = new BufferedInputStream(new FileInputStream(localFile));
			logger.info(localFile.getName() + "开始上传.....");
			success = this.ftpClient.storeFile(remoteFileName, inStream);
			if (success == true) {
				logger.info(localFile.getName() + "上传成功");
				return success;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(localFile + "未找到");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	public String uploadDir2Dir(String localDirPath, String remoteDir) {
		File localDirFile = new File(localDirPath);
		String targetDir = this.mkFtpDir(remoteDir+"/"+localDirFile.getName());
		File[] allFile = localDirFile.listFiles();
		for (int index = 0; index < allFile.length; index++) {
			if (!allFile[index].isDirectory()) {
				String srcName = allFile[index].getPath().toString();
				uploadFile2Dir(srcName, targetDir);
			}else{
				uploadDir2Dir(allFile[index].getPath().toString(), targetDir);
			}
		}
		return targetDir;
	}

	public boolean uploadDir(String localDir, String remoteDir, String remoteDirName) {
		File src = new File(localDir);
		String targetDir = this.mkFtpDir(remoteDir+"/"+remoteDirName);
		uploadDir2Dir(localDir,targetDir);
		return true;
	}
	
	public boolean downloadFile(String remoteFilePath, String localDir, String localFileName){
		this.ftpLogin();
		File remoteFile = new File(remoteFilePath);
		String remoteDirPath = remoteFile.getParent();
		String remoteFileName = remoteFile.getName();
		String localFilePath = localDir + "/" + localFileName;
		new File(localDir).mkdirs();
		BufferedOutputStream outStream = null;
		boolean success = false;
		try {
			this.ftpClient.cwd(remoteDirPath);
			outStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
			logger.info(remoteFileName + "开始下载....");
			success = this.ftpClient.retrieveFile(remoteFileName, outStream);
			if (success == true) {
				logger.info(remoteFileName + "成功下载到" + localFilePath);
				return success;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(remoteFileName + "下载失败");
		} finally {
			if (null != outStream) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (success == false) {
			logger.error(remoteFileName + "下载失败!!!");
		}
		return success;
	}
	
	public boolean downloadFile2Dir(String remoteFilePath, String localDir) {
		File remoteFile = new File(remoteFilePath);
		String remoteFileName = remoteFile.getName();
		return downloadFile(remoteFilePath, localDir, remoteFileName);
	}
	
	public String downloadDir(String remoteDirPath, String localDir, String localDirName){
		this.ftpLogin();
		String targetDir = null;
		try {
			targetDir = localDir + "/" + localDirName;
			new File(localDir).mkdirs();
			FTPFile[] allFile = this.ftpClient.listFiles(remoteDirPath);
			for (int index = 0; index < allFile.length; index++) {
				if (allFile[index].isDirectory()) {
					String childRemoteDir = remoteDirPath + "/" + allFile[index].getName();
					downloadDir2Dir(childRemoteDir, targetDir);
				}else{
					downloadFile2Dir(remoteDirPath+"/"+allFile[index].getName(), targetDir);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("下载文件夹失败");
			return targetDir;
		}
		return targetDir;
	}
	
	public boolean downloadDir2Dir(String remoteDirPath, String localDir) {
		String localDirName = new File(remoteDirPath).getName();
		downloadDir(remoteDirPath, localDir, localDirName);
		return true;
	}
	
	public static void test(){
		FtpAdaptor ftp = new FtpAdaptor("172.16.246.128", 21, "ftp", "ftp");
		
		String baseTestDir = "/Users/zhangquan07/Documents/testftp";
		//上传文件
		//ftp.uploadFile2Dir(baseTestDir+"/readme.txt", "/filetest");
		//上传文件夹
		//ftp.uploadDir2Dir(baseTestDir+"/testdir", "/dirtest");
		
		// 下载文件
		ftp.downloadFile2Dir("/filetest/readme.txt", "/Users/zhangquan07/Documents/testftp/download");
		// 下载文件夹
		ftp.downloadDir2Dir("/dirtest/testdir", "/Users/zhangquan07/Documents/testftp/download");
		ftp.ftpLogOut();
	}
	
	
}