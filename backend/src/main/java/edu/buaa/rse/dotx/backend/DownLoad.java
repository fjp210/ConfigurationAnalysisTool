package edu.buaa.rse.dotx.backend;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import edu.buaa.rse.dotx.backend.util.ServletUtil;

public class DownLoad extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServletUtil.setServletContext(req.getServletContext());
//		req.setCharacterEncoding("utf-8");
//		resp.setCharacterEncoding("utf-8");
//		resp.setContentType("text/html;charset=utf-8");
//		req.setCharacterEncoding("utf-8");
//		resp.setContentType("text/html;charset=UTF-8");
//		  resp.setContentType("text/plain");
		//进行编码的转换，因为不能识别中文
		resp.setHeader("content-type","text/html;charset=UTF-8");
		String path = getServletContext().getRealPath("/") + "images/";
		String fileName = req.getParameter("filename");
		String filename = null;
		filename = new String(fileName.getBytes("8859_1"),"utf-8");
//		filename = new String(filename.getBytes("8859_1"),"uft-8");
		System.out.println("路径：" + path + "文件名：" + filename);
		File file = new File(path + filename);
		if (file.exists()) {
			//由于下载的时候与浏览器的编码不符，浏览器不能识别中文编码，这里要进行转换
			String value = new String(filename.getBytes("utf-8"),"ISO-8859-1");
			resp.setContentType("application/x-msdownload");
			resp.setHeader("Content-Disposition", "attachment;filename=\""
					+ value + "\"");
			InputStream inputStream = new FileInputStream(file);
			ServletOutputStream outputStream = resp.getOutputStream();
			byte b[] = new byte[1024];
			int n;
			while ((n = inputStream.read(b)) != -1) {
				outputStream.write(b, 0, n);
			}

			outputStream.close();
			inputStream.close();
		} else {
			req.setAttribute("errorResult", "文件不存在下载失败！！");
			resp.sendRedirect("luntan.jsp");
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
