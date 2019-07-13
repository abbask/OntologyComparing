package edu.uga.cs.ontologycomparision.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.VersionService;
import edu.uga.cs.ontologycomparision.util.FreeMarkerTemplate;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@WebServlet("/RetrieveSchema")
public class RetrieveSchema extends HttpServlet{
	
	private static final long serialVersionUID = 2L;
	final static Logger logger = Logger.getLogger(VersionNew.class);
	
	static String templateName = "RetrieveSchema.ftl";
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		loadPage(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {		

		loadPage(req, res);
		
	}
	
	public void loadPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		BufferedWriter toClient = null;
		Map<String, Object> root = new HashMap<>();	

		FreeMarkerTemplate freeMarkerTemplate = new FreeMarkerTemplate();
		Template template = freeMarkerTemplate.loadTemplate(getServletContext(), templateName);		
		
		int version_id = 0;
		version_id = (!req.getParameter("id").equals(""))? Integer.parseInt(req.getParameter("id")) : version_id ;
		
		toClient = new BufferedWriter(
				new OutputStreamWriter(res.getOutputStream(), template.getEncoding()));
		res.setContentType("text/html; charset=" + template.getEncoding());
		
		VersionService versionService = new VersionService();
		Version version = null;
		try {
			version = versionService.get(version_id);
		} catch (SQLException e1) {
			throw new ServletException(
					"Error while retrieve version.", e1);
		}
		root.put("version", version);

		try {
			template.process(root, toClient);
			toClient.flush();
		} catch (TemplateException e) {
			throw new ServletException(
					"Error while processing FreeMarker template", e);
		}

		toClient.close();
	}
	

}
