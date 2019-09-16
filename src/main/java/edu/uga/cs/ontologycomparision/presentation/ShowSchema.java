package edu.uga.cs.ontologycomparision.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.VersionService;
import edu.uga.cs.ontologycomparision.util.FreeMarkerTemplate;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@WebServlet("/showschema")
public class ShowSchema extends HttpServlet{

	private static final long serialVersionUID = 2L;
	final static Logger logger = Logger.getLogger(ShowSchema.class);
	
	static String templateName = "ShowSchema.ftl";
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		loadPage(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {		
		loadData(req, res);
	
	}
	
	public void loadPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		BufferedWriter toClient = null;
		Map<String, Object> root = new HashMap<>();	

		FreeMarkerTemplate freeMarkerTemplate = new FreeMarkerTemplate();
		Template template = freeMarkerTemplate.loadTemplate(getServletContext(), templateName);									
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		VersionService service = new VersionService(mySQLConnection.openConnection());
		List<Version> list;
		
		try {		
			list =  service.getListAll();			
			root.put("versions", list);
			
			toClient = new BufferedWriter(
					new OutputStreamWriter(res.getOutputStream(), template.getEncoding()));
			res.setContentType("text/html; charset=" + template.getEncoding());
			
			template.process(root, toClient);
			toClient.flush();

		} catch (TemplateException e) {
			throw new ServletException(
					"Error while processing FreeMarker template", e);
		} catch(SQLException sqlEx) {
			throw new ServletException("Error while query Database", sqlEx);
		}		
		toClient.close();
	}
	
	public void loadData(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		BufferedWriter toClient = null;
		Map<String, Object> root = new HashMap<>();	

		FreeMarkerTemplate freeMarkerTemplate = new FreeMarkerTemplate();
		Template template = freeMarkerTemplate.loadTemplate(getServletContext(), templateName);									
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		VersionService versionService = new VersionService(mySQLConnection.openConnection());
		try {
			
			int version_id = 0;
			version_id = (!req.getParameter("version").equals(""))? Integer.parseInt(req.getParameter("version")) : version_id ;
			Version version = versionService.get(version_id);
			
			ShowService showService = new ShowService(version);
			List<Version> list;
			root.put("selectedVersion", version);
			
				
			list =  versionService.getListAll();			
			root.put("versions", list);
			
			root = showService.getAllCounts(root);
			root.put("classes", showService.getClasses());
			
			toClient = new BufferedWriter(
					new OutputStreamWriter(res.getOutputStream(), template.getEncoding()));
			res.setContentType("text/html; charset=" + template.getEncoding());
			
			template.process(root, toClient);
			toClient.flush();

		} catch (TemplateException e) {
			throw new ServletException(
					"Error while processing FreeMarker template", e);
		} catch(SQLException sqlEx) {
			throw new ServletException("Error while query Database", sqlEx);
		}		
		toClient.close();
	}
}
