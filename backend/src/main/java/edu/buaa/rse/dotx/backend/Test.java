package edu.buaa.rse.dotx.backend;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.buaa.rse.dotx.backend.util.ServletUtil;
import edu.buaa.rse.dotx.config.Config;
public class Test extends HttpServlet
{
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	ServletUtil.setServletContext(req.getServletContext());
    	String taskid = req.getParameter("taskId");
    	String host = Config.getInstance().get("zk.host");
    	PrintWriter out=resp.getWriter();
        out.println(host);
        out.close();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		this.doPost(request, response);
	}
}