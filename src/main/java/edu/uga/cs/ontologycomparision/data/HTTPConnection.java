package edu.uga.cs.ontologycomparision.data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HTTPConnection { // took from web page https://www.baeldung.com/java-http-request
	
	private URL url;
	private String endPoint;
	private String query;
	
	private String debug;
	private String timeout;
	private String outputFormat ;
	
	public HTTPConnection(String endPoint) {
		this.endPoint = endPoint;		
		
		debug = "off";
		timeout = "0" ;
		outputFormat = "application/sparql-results+json";
	}
	
	
	public HTTPConnection(String endPoint, String sparqlQuery) {
		this.endPoint = endPoint;
		this.query = sparqlQuery;
		
		debug = "off";
		timeout = "0" ;
		outputFormat = "application/sparql-results+json";
	}
	
	public void setSparqlQuery(String sparqlQuery) {
		this.query = sparqlQuery;
	}

	public String execute() throws IOException {
		String strURL = endPoint;
		
		url = new URL(strURL);
				
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
				
		Map<String, String> parameters = new HashMap<>();
		parameters.put("default-graph-uri", "");
		parameters.put("query", query);
		parameters.put("format", outputFormat);
		parameters.put("timeout", timeout);
		parameters.put("debug", debug);
		
		con.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		out.flush();
		out.close();
		
		int status = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
		
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		
		in.close();
		con.disconnect();
		
		in =null;
		con = null;
		return content.toString();
		
	}
	
}
