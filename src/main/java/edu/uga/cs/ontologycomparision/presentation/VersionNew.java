package edu.uga.cs.ontologycomparision.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
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

@WebServlet("/VersionNew")
public class VersionNew extends HttpServlet{
	
	private static final long serialVersionUID = 2L;
	final static Logger logger = Logger.getLogger(VersionNew.class);
	
	static String templateName = "VersionNew.ftl";

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		loadPage(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {		

		save(req,res);
		
	}
	
	public void loadPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		BufferedWriter toClient = null;
		Map<String, Object> root = new HashMap<>();	

		FreeMarkerTemplate freeMarkerTemplate = new FreeMarkerTemplate();
		Template template = freeMarkerTemplate.loadTemplate(getServletContext(), templateName);		
		
		toClient = new BufferedWriter(
				new OutputStreamWriter(res.getOutputStream(), template.getEncoding()));
		res.setContentType("text/html; charset=" + template.getEncoding());

		try {
			template.process(root, toClient);
			toClient.flush();
		} catch (TemplateException e) {
			throw new ServletException(
					"Error while processing FreeMarker template", e);
		}

		toClient.close();
	}
	
	protected void save(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String name = "";
		String number = "";
		String dateStr = "";

		name = (!req.getParameter("name").equals(""))? req.getParameter("name") : name ;
		number = (!req.getParameter("number").equals(""))? req.getParameter("number") : number;
		dateStr = (!req.getParameter("date").equals(""))? req.getParameter("date") : number;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			throw new ServletException(
					"Invalid date. Please use correct date", e);
		}		
		
		Version version = new Version(name, number, date);
		VersionService versionService = new VersionService();
		versionService.add(version);

		
		res.sendRedirect(req.getContextPath() + "/VersionList");
		
	}
}
