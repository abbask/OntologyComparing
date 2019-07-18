package edu.uga.cs.ontologycomparision.presentation;


import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;


@WebServlet("/EndPointStatus")
public class EndPointStatus extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(EndPointStatus.class);	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		loadPage(req, res);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {		
		loadPage(req, res);	
	}

	public void loadPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String graphName = "";
		String endpointURL = "";
		boolean result = false;
		
		
		endpointURL = (!req.getParameter("endpoint").equals(""))? req.getParameter("endpoint") : endpointURL;
		graphName = (!req.getParameter("graphName").equals(""))? req.getParameter("graphName") : graphName ;
		
		try {
			RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName);
			result =  service.checkEndPoint(endpointURL, graphName);
		}
		catch(Exception ex) {
			throw new ServletException(
					"Invalid date. Please use correct date", ex);
		}
		
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		if (result)
			out.append("True");
		else
			out.append("True");
		
		out.close();
	}
}
