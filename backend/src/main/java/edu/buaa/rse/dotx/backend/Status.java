package edu.buaa.rse.dotx.backend;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.buaa.rse.dotx.agent.DotxAdaptor;
import edu.buaa.rse.dotx.backend.util.ServletUtil;
public class Status extends HttpServlet
{
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
    	ServletUtil.setServletContext(req.getServletContext());
    	String taskId = req.getParameter("taskId");
    	Map result = DotxAdaptor.getInstance().status(taskId);
		Gson gson = new GsonBuilder().create();
		String resultStr = gson.toJson(result);
    	PrintWriter out=resp.getWriter();
        out.println(resultStr);
        out.close();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		this.doPost(request, response);
	}
}