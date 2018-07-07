package edu.buaa.rse.dotx.backend;

import java.io.*;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.buf.StringUtils;
import org.jboss.netty.util.internal.StringUtil;

import com.google.gson.JsonObject;

import edu.buaa.rse.dotx.agent.DotxAdaptor;
import edu.buaa.rse.dotx.backend.util.ServletUtil;
import edu.buaa.rse.dotx.zip.ZipUtil;

/**
 * 
 * @author Administrator
 * http://blog.csdn.net/hzc543806053/article/details/7524491
 * 文件上传
 * 具体步骤：
 * 1）获得磁盘文件条目工厂 DiskFileItemFactory 要导包
 * 2） 利用 request 获取 真实路径 ，供临时文件存储，和 最终文件存储 ，这两个存储位置可不同，也可相同
 * 3）对 DiskFileItemFactory 对象设置一些 属性
 * 4）高水平的API文件上传处理  ServletFileUpload upload = new ServletFileUpload(factory);
 * 目的是调用 parseRequest（request）方法  获得 FileItem 集合list ，
 * 5）在 FileItem 对象中 获取信息，遍历，判断表单提交过来的信息 是否是 普通文本信息  另做处理
 * 6）
 *    第一种. 用第三方 提供的  item.write( new File(path,filename) );  直接写到磁盘上
 *    第二种. 手动处理  
 *
 */
public class Submit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletUtil.setServletContext(request.getServletContext());
		request.setCharacterEncoding("utf-8");	//设置编码
		//获得磁盘文件条目工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//获取文件需要上传到的路径
		String webTempDir = request.getServletContext().getRealPath("/temp");
		//如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
		//设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
		/**
		 * 原理它是先存到暂时存储室，然后在真正写到对应目录的硬盘上， 
		 * 按理来说 当上传一个文件时，其实是上传了两份，第一个是以.tem格式的 
		 * 然后再将其真正写到 对应目录的硬盘上
		 */
		factory.setRepository(new File(webTempDir));
		//设置缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
		factory.setSizeThreshold(1024*1024) ;
		//高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
			//可以上传多个文件
		List<FileItem> list = null;
		try {
			list = (List<FileItem>)upload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		String taskIdStr = null;
		for(FileItem item : list){
			String name = item.getFieldName();
			if(item.isFormField()){					
				String value = item.getString() ;
				request.setAttribute(name, value);
			}else{
				String value = item.getName() ;
				int start = value.lastIndexOf("\\");
				String filename = value.substring(start+1);
				if(filename == null || filename == "")continue;
				request.setAttribute(name, filename);
				String tempDir = String.valueOf(System.currentTimeMillis());
				(new File(webTempDir+"/"+tempDir)).mkdirs();
				OutputStream out = new FileOutputStream(new File(webTempDir+"/"+tempDir,"target.zip"));
				InputStream in = item.getInputStream() ;
				int length = 0 ;
				byte [] buf = new byte[1024] ;
				System.out.println("获取上传文件的总共的容量："+item.getSize());
				while( (length = in.read(buf) ) != -1){
					out.write(buf, 0, length);
				}
				in.close();
				out.close();
				taskIdStr = this.submitTask(new File(webTempDir+"/"+tempDir,"target.zip").getAbsolutePath());
			}
		}
		JsonObject result = new JsonObject();
		result.addProperty("status", "success");
		result.addProperty("taskId", taskIdStr);
    	PrintWriter out=response.getWriter();
        out.println(result.toString());
        out.close();
		//request.getRequestDispatcher("filedemo.jsp").forward(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		this.doPost(request, response);
	}
	
	private String submitTask(String filePath){
		String dir = new File(filePath).getParent();
		ZipUtil.unzip(filePath, dir);
		String targetDir = this.getTargetDir(dir);
		System.out.println("-----the target dir is:"+targetDir);
		return DotxAdaptor.getInstance().push(targetDir);
	}
	
	private String getTargetDir(String dir){
		File file = new File(dir);
		File[] files = file.listFiles();
		for(int i=0; i<files.length; i++){
			File current = files[i];
			if(current.isDirectory()){
				current.renameTo(new File(dir+"/target"));
			}
		}
		return dir+"/target";
	}
}
