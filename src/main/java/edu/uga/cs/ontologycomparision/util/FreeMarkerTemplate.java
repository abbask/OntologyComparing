package edu.uga.cs.ontologycomparision.util;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerTemplate {
	
	private String templateDir = "WEB-INF/templates";
	private Configuration  cfg; 

	public Template loadTemplate(ServletContext context, String templateName )throws ServletException{
		
		String templatePath = null;
		
		
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setServletContextForTemplateLoading( context , 
				templateDir );
		Template template = null;
	

		templatePath = templateDir + "/" + templateName;

		// load the template
		try {
			template = cfg.getTemplate(templateName);
		} 
		catch (IOException e) {
			throw new ServletException( 
					"Can't load template " + templatePath + ": " + e.toString());
		}
		return template;
		
		
	}

}
