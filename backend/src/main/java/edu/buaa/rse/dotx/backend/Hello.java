package edu.buaa.rse.dotx.backend;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.buaa.rse.dotx.backend.util.ServletUtil;
public class Hello extends HttpServlet
{
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	ServletUtil.setServletContext(req.getServletContext());
    	String taskid = req.getParameter("taskId");
    	
    	PrintWriter out=resp.getWriter();
        out.println("hello!");
        //out.println(req.getServletContext().getRealPath("/upload"));
        out.close();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		this.doPost(request, response);
	}
}