package edu.uga.cs.ontologycomparision.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.uga.cs.ontologycomparision.data.DataStoreConnection;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Servlet implementation class Comparison
 */
@WebServlet("/Comparison")
public class Comparison extends HttpServlet {
	private static final long serialVersionUID = 1L;


	private String	   	   templatePath = null;
	static  String         templateDir = "WEB-INF/templates";
	static  String         templateName = "comparison.ftl";

	private Configuration  cfg; 


	String newGraphName = "<http://prokino.uga.edu>";
	String newEndpoint = "http://vulcan.cs.uga.edu:8890/sparql";
	String oldGraphName = "<http://prokino.uga.edu>";
	String oldEndpoint = "http://localhost:8890/sparql/";

	public void init() {

		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setServletContextForTemplateLoading( getServletContext(), 
				templateDir );
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		loadPage(req, res);
	}


	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {		

		compareAll(req,res);
		
	}

	public void compareAll(HttpServletRequest req, HttpServletResponse res){	
	
		Map<String, Object> classCountCompare = new HashMap<String, Object>();
		Map<String, Object> objectPropertiesCountCompare = new HashMap<String, Object>();
		Map<String, Object> dataPropertiesCountCompare = new HashMap<String, Object>();
		Map<String, Object> individualCountCompare = new HashMap<String, Object>();
		
		Map<String, Object> classCompare = new HashMap<String, Object>();
		
		Map<String, Object> root = new HashMap<String, Object>();
		
		String action = req.getParameter("action");

		newGraphName = (!req.getParameter("newGraphName").equals(""))? req.getParameter("newGraphName") : newGraphName ;
		newEndpoint  = (!req.getParameter("newEndpoint").equals("")) ? req.getParameter("newEndpoint")  : newEndpoint;
		oldGraphName = (!req.getParameter("oldGraphName").equals(""))? req.getParameter("oldGraphName") : oldGraphName;
		oldEndpoint =  (!req.getParameter("oldEndpoint").equals("")) ? req.getParameter("oldEndpoint")  : oldEndpoint;
		
		switch (action) {
		case "examine":
			
			classCountCompare = examinClassCount(newGraphName, newEndpoint, oldGraphName, oldEndpoint);
			objectPropertiesCountCompare = examinObjectPropertyCount(newGraphName, newEndpoint, oldGraphName, oldEndpoint);
			dataPropertiesCountCompare = examinDataPropertyCount(newGraphName, newEndpoint, oldGraphName, oldEndpoint);
			individualCountCompare = examinIndividualCount(newGraphName, newEndpoint, oldGraphName, oldEndpoint);
			
			//classCompare = examineClassListCompare(newGraphName, newEndpoint, oldGraphName, oldEndpoint);
			
			
			break;

		default:
			break;
		}

		root.put("classCountCompare", classCountCompare);
		root.put("objectPropertiesCountCompare", objectPropertiesCountCompare);
		root.put("dataPropertiesCountCompare", dataPropertiesCountCompare);
		root.put("individualCountCompare", individualCountCompare);
		root.put("classCompare", classCompare);
		
		
		Gson gson = new Gson();
		String result = gson.toJson(root);

		res.setContentType("application/json");
		PrintWriter pw = null;
		try {
			pw = res.getWriter ();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			pw.print(result);
			pw.close();
		}
		
		
	}
	
	
	public Map<String, Object> examinClassCount(String newGraphName, String newEndpoint, String oldGraphName, String oldEndpoint  ){

		Map<String, Object> root = new HashMap<>();	

		Map<String, Object> classCount = new HashMap<>();	
		Map<String, Object> classCountNew = new HashMap<>();
		Map<String, Object> classCountOld = new HashMap<>();

		DataStoreConnection queryNew = new DataStoreConnection(newEndpoint, newGraphName);
		DataStoreConnection queryOld = new DataStoreConnection(oldEndpoint, oldGraphName);


		classCountNew = queryNew.countClasses();
		classCountOld = queryOld.countClasses();


		classCount.put("classCountNew", classCountNew.get("classCount"));
		classCount.put("classCountOld", classCountOld.get("classCount"));

		root.put("classCount", classCount);

		return root;

	}

	public Map<String, Object> examinObjectPropertyCount(String newGraphName, String newEndpoint, String oldGraphName, String oldEndpoint  ){

		Map<String, Object> root = new HashMap<>();	


		Map<String, Object> objectPropertyCount = new HashMap<>();	
		Map<String, Object> objectPropertyCountNew = new HashMap<>();
		Map<String, Object> objectPropertyCountOld = new HashMap<>();

		DataStoreConnection queryNew = new DataStoreConnection(newEndpoint, newGraphName);
		DataStoreConnection queryOld = new DataStoreConnection(oldEndpoint, oldGraphName);


		objectPropertyCountNew = queryNew.countObjectProperty();
		objectPropertyCountOld = queryOld.countObjectProperty();


		objectPropertyCount.put("objectPropertyCountNew", objectPropertyCountNew.get("objectPropertyCount"));
		objectPropertyCount.put("objectPropertyCountOld", objectPropertyCountOld.get("objectPropertyCount"));

		root.put("objectPropertyCount", objectPropertyCount);

		return root;

	}

	public Map<String, Object> examinDataPropertyCount(String newGraphName, String newEndpoint, String oldGraphName, String oldEndpoint  ){

		Map<String, Object> root = new HashMap<>();	


		Map<String, Object> dataPropertyCount = new HashMap<>();	
		Map<String, Object> dataPropertyCountNew = new HashMap<>();
		Map<String, Object> dataPropertyCountOld = new HashMap<>();

		DataStoreConnection queryNew = new DataStoreConnection(newEndpoint, newGraphName);
		DataStoreConnection queryOld = new DataStoreConnection(oldEndpoint, oldGraphName);


		dataPropertyCountNew = queryNew.countDataProperty();
		dataPropertyCountOld = queryOld.countDataProperty();


		dataPropertyCount.put("dataPropertyCountNew", dataPropertyCountNew.get("dataPropertyCount"));
		dataPropertyCount.put("dataPropertyCountOld", dataPropertyCountOld.get("dataPropertyCount"));

		root.put("dataPropertyCount", dataPropertyCount);

		return root;

	}

	public Map<String, Object> examinIndividualCount(String newGraphName, String newEndpoint, String oldGraphName, String oldEndpoint  ){

		Map<String, Object> root = new HashMap<>();	


		Map<String, Object> individualCount = new HashMap<>();	
		Map<String, Object> individualCountNew = new HashMap<>();
		Map<String, Object> individualCountOld = new HashMap<>();

		DataStoreConnection queryNew = new DataStoreConnection(newEndpoint, newGraphName);
		DataStoreConnection queryOld = new DataStoreConnection(oldEndpoint, oldGraphName);


		individualCountNew = queryNew.restoreInstancesOfClasses();
		individualCountOld = queryOld.restoreInstancesOfClasses();


		individualCount.put("individualCountNew", individualCountNew.get("classInstance"));
		individualCount.put("individualCountOld", individualCountOld.get("classInstance"));

		root.put("individualCount", individualCount);

		return root;

	}

	public void loadPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Template       					template;
		String         					templatePath = null;
		BufferedWriter 					toClient = null;


		// set the template path
		templatePath = templateDir + "/" + templateName;

		// load the template
		try {
			template = cfg.getTemplate(templateName);
		} 
		catch (IOException e) {
			throw new ServletException( 
					"Can't load template " + templateDir + "/" + templateName + ": " + e.toString());
		}

		Map<String, Object> root = new HashMap<>();				

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




}
