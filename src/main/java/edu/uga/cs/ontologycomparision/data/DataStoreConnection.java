package edu.uga.cs.ontologycomparision.data; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;

import edu.uga.cs.ontologycomparision.model.*;


public class DataStoreConnection {
	private String serviceURI = "http://localhost:8890/sparql/";
	private String graphName ="<http://prokino.uga.edu>";		
	
//	public DataStoreConnection() {
//				
//	}

	public String getServiceURI() {
		return serviceURI;
	}

	public void setServiceURI(String serviceURI) {
		this.serviceURI = serviceURI;
	}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public DataStoreConnection(String serviceURI, String graphName) {
		if (serviceURI != null) 
			this.serviceURI = serviceURI;
		if (graphName != null)
			this.graphName = graphName;
		
	}

	private ArrayList<String> executeQuery(String queryString) {
		

		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
		
		ArrayList<String> list = new ArrayList<String>();
		
		try {					
			org.apache.jena.query.ResultSet rs = qexec.execSelect();
			while(rs.hasNext()) {
				org.apache.jena.query.QuerySolution sol = rs.nextSolution();
				list.add(sol.getResource("s").toString());				
			}

		}
		catch (Exception e) {
			e.printStackTrace();		
		}		
		qexec.close();	
		return list;
	}	

	//showClasses	
	public Map<String, Object> restoreAllClasses () {
		String queryString = "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:    <http://www.w3.org/2002/07/owl#> PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
		//		String queryString = "SELECT DISTINCT ?className ?classLabel ?superClass FROM <http://prokino.uga.edu> WHERE { ?className rdf:type owl:Class. optional {?className rdfs:label ?classLabel.} optional {?className rdfs:subClassOf ?superClass.} FILTER regex(str(?className),'prokino') } ORDER BY ?className";
		queryString += "SELECT DISTINCT ?className ?classLabel ?superClass (COUNT(DISTINCT ?entity) as ?count)	 FROM " + graphName + " WHERE { ?className rdf:type owl:Class. ?entity rdf:type ?className. optional {?className rdfs:label ?classLabel.} optional {?className rdfs:subClassOf ?superClass.} FILTER regex(str(?className),'prokino') } GROUP BY ?className ?classLabel ?superClass ORDER BY ?className";
		//System.out.println(queryString);
		
		
		
		
		MyConcept concept;
		Map<String, Object> returnResult = new HashMap<String, Object>();
		ArrayList<MyConcept> conceptList = new ArrayList<MyConcept>();

		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
		try {					

			
			ResultSet rs = qexec.execSelect();

			while (rs.hasNext()){

				concept = new MyConcept();
				org.apache.jena.query.QuerySolution soln = rs.nextSolution();

				concept.setClassName(soln.getResource("className").getLocalName());				

				concept.setClassLabel(soln.getLiteral("classLabel") != null ? soln.getLiteral("classLabel").toString() : "");

				if (soln.getResource("superClass") != null){
					concept.setSuperClass(soln.getResource("superClass").getLocalName());
				}
				else{
					concept.setSuperClass("");
				}								
				conceptList.add(concept);

				concept.setCount(soln.getLiteral("count").getInt());

			}

			returnResult.put("classes", conceptList);

		}
		catch (Exception e) {
			e.printStackTrace();		
		}		
		qexec.close();		
		return returnResult;
	}

	//listAllInfo, updateAllInfo
	public Map<String, Object> countClasses () {
		String queryString = "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:    <http://www.w3.org/2002/07/owl#> PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
		queryString += " SELECT (count(?className) as ?count) FROM " + graphName + " WHERE { ?className a owl:Class. optional {?className rdfs:label ?classLabel.} optional {?className rdfs:subClassOf ?superClass.} ";
		
		queryString += "FILTER regex(str(?className),'prokino') }";
		//System.out.println(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
			
		Map<String, Object> returnResult = new HashMap<String, Object>();		
		
		
		try {					
			ResultSet rs = qexec.execSelect();
			
			int count=0; 
			while (rs.hasNext()){
				org.apache.jena.query.QuerySolution soln = rs.nextSolution();
				count =soln.getLiteral("count").getInt();
			}
			returnResult.put("classCount", count);
		}
		catch (Exception e) {
			e.printStackTrace();		
		}		
		qexec.close();		
		return returnResult;
	}
	
	//listAllInfo, updateAllInfo
	public Map<String, Object> countDataProperty () {
		String queryString = "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:    <http://www.w3.org/2002/07/owl#> PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
		queryString += "SELECT (count(?name) as ?count) FROM " + graphName  + " WHERE { ?name rdf:type owl:DatatypeProperty optional {?name rdfs:domain ?o. ?o owl:unionOf ?l. {?l rdf:first ?domain.} UNION {?l rdf:rest ?rest. ?rest rdf:first ?domain}} optional {?name rdfs:domain ?domain} optional {?name rdfs:range ?range} } ";
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
		
			
		Map<String, Object> returnResult = new HashMap<String, Object>();		

		try {					
			ResultSet rs = qexec.execSelect();
			
			
			int count=0; 
			while (rs.hasNext()){

				org.apache.jena.query.QuerySolution soln = rs.nextSolution();
				count =soln.getLiteral("count").getInt();

			}
			
			returnResult.put("dataPropertyCount", count);
		}
		catch (Exception e) {
			e.printStackTrace();		
		}		
		qexec.close();		
		return returnResult;
	}
	
	//listAllInfo, updateAllInfo
	public Map<String, Object> countObjectProperty () {
		String queryString = "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:    <http://www.w3.org/2002/07/owl#> PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
		queryString += "SELECT (count(?name) as ?count) FROM " + graphName + " WHERE { ?name rdf:type owl:ObjectProperty optional {?name rdfs:domain ?o. ?o owl:unionOf ?l. {?l rdf:first ?domain.} UNION {?l rdf:rest ?rest. ?rest rdf:first ?domain}} optional {?name rdfs:domain ?domain} optional {?name rdfs:range ?range} } ";
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
			
		Map<String, Object> returnResult = new HashMap<String, Object>();		

		try {					
			ResultSet rs = qexec.execSelect();
			
			
			int count=0; 
			while (rs.hasNext()){

				org.apache.jena.query.QuerySolution soln = rs.nextSolution();
				count =soln.getLiteral("count").getInt();

			}

			
			returnResult.put("objectPropertyCount", count);
		}
		catch (Exception e) {
			e.printStackTrace();		
		}		
		qexec.close();		
		return returnResult;
	}

	
	//showObjectProperties
	public Map<String, Object> restoreAllObejectProperties () {
		
		String queryString = "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:    <http://www.w3.org/2002/07/owl#> PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
		queryString += "SELECT DISTINCT ?domain ?name ?range (COUNT(?object) as ?count) FROM " + graphName + "  WHERE { ?name rdf:type owl:ObjectProperty optional { ?name rdfs:domain ?o. ?o owl:unionOf ?l.  {?l rdf:first ?domain. } UNION {?l rdf:rest ?rest. ?rest rdf:first ?domain}} optional {?name rdfs:domain ?domain} optional {?name rdfs:range ?range. ?range rdf:type owl:Class} ?subject ?name ?object} GROUP By ?name ?domain ?range ORDER BY ?name";
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
		
		
		MyProperty prop;
		Map<String, Object> returnResult = new HashMap<String, Object>();
		ArrayList<MyProperty> propertytList = new ArrayList<MyProperty>();

		try {					

			ResultSet rs = qexec.execSelect();			
			
			int count = 0;
			
			List<MyProperty> lstDomain = new LinkedList<MyProperty>();				
			
			
			while (rs.hasNext()){
				prop = new MyProperty();
				org.apache.jena.query.QuerySolution soln = rs.nextSolution();


				if (soln.getResource("name") != null){

					prop.setName(soln.getResource("name").getLocalName());

				}
				else{
					prop.setName("");
				}

				if (soln.getResource("domain") != null){

					prop.setDomain(soln.getResource("domain").getLocalName());

				}
				else{
					prop.setDomain("");
				}

				if (soln.getResource("range") != null){

					prop.setRange(soln.getResource("range").getLocalName());

				}
				else{
					prop.setRange("");
				}
				
				prop.setCount(soln.getLiteral("count").getInt());
				
				count++;
				propertytList.add(prop);				
			}

			returnResult.put("objectProperties", propertytList);
			returnResult.put("objectPropertyCount", count);

		}
		catch (Exception e) {
			e.printStackTrace();		
		}		
		qexec.close();	

		
		return returnResult;
	}

	//showDataProperties	
	public Map<String, Object> restoreAllDataProperties () {
		String queryString = "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:    <http://www.w3.org/2002/07/owl#> PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
		//String queryString = "SELECT DISTINCT ?domain ?name ?range COUNT(?name) as ?count FROM <http://prokino.uga.edu> WHERE { ?name rdf:type owl:DatatypeProperty optional {?name rdfs:domain ?domain} optional {?name  rdfs:range ?range} } ORDER BY ?name ?domain ?range";
		queryString += "SELECT DISTINCT ?domain ?name ?range FROM " + graphName + " WHERE { ?name rdf:type owl:DatatypeProperty optional {?name rdfs:domain ?o. ?o owl:unionOf ?l. {?l rdf:first ?domain.} UNION {?l rdf:rest ?rest. ?rest rdf:first ?domain}} optional {?name rdfs:domain ?domain} optional {?name rdfs:range ?range} } ORDER BY ?name ?domain ?range";

		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
		
		
		MyProperty prop;
		Map<String, Object> returnResult = new HashMap<String, Object>();
		ArrayList<MyProperty> propertytList = new ArrayList<MyProperty>();

		try {					
			ResultSet rs = qexec.execSelect();
		
			int count = 0;
			while (rs.hasNext()){
				prop = new MyProperty();
				org.apache.jena.query.QuerySolution soln = rs.nextSolution();


				if (soln.getResource("name") != null){

					prop.setName(soln.getResource("name").getLocalName());

				}
				else{
					prop.setName("");
				}

				if (soln.getResource("domain") != null){

					prop.setDomain(soln.getResource("domain").getLocalName());

				}
				else{
					prop.setDomain("");
				}


				if (soln.getResource("range") != null){

					prop.setRange(soln.getResource("range").getLocalName());

				}
				else{
					prop.setRange("");
				}

				count++;
				propertytList.add(prop);				
			}

			returnResult.put("dataProperties", propertytList);
			returnResult.put("dataPropertyCount", count);

		}
		catch (Exception e) {
			e.printStackTrace();		
		}		
		qexec.close();		
		return returnResult;
	}
	
	//listAllInfo, updateAllInfo
	public Map<String, Object> restoreInstancesOfClasses () {
		String queryString = "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:    <http://www.w3.org/2002/07/owl#> PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
		queryString += "SELECT (COUNT(DISTINCT ?entity) as ?count) FROM " + graphName + " WHERE{ ?entity rdf:type ?class. ?class rdf:type owl:Class. }";
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString);
		

		Map<String, Object> returnResult = new HashMap<String, Object>();


		try {					

			ResultSet rs = qexec.execSelect();

			while (rs.hasNext()){

				org.apache.jena.query.QuerySolution soln = rs.nextSolution();
								
				returnResult.put("classInstance", soln.getLiteral("count").getValue());			

			}
		}
		catch (Exception e) {
			e.printStackTrace();		
		}		
		qexec.close();	
		return returnResult;
	}

}
