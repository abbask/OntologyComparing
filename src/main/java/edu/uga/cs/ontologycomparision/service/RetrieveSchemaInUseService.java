package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.data.DataStoreConnection;
import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.Version;

public class RetrieveSchemaInUseService {
	final static String ObjectPropertyType = "ObjectProperty";
	final static String DatatypePropertyType = "DatatypeProperty";
	
	final static Logger logger = Logger.getLogger(RetrieveSchemaInUseService.class);
	
	private String endpointURL;
	private String graphName;
	private Version version;
	
	private Connection connection;	
	
	public RetrieveSchemaInUseService(String endpointURL, String graphName) {
		this.endpointURL = endpointURL;
		this.graphName = graphName;
	}
	
	public RetrieveSchemaInUseService(String endpointURL, String graphName, int versionId) throws SQLException {
		this.endpointURL = endpointURL;
		this.graphName = graphName;
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		connection = mySQLConnection.openConnection();
	
		VersionService versionService = new VersionService(connection);
		version = versionService.get(versionId);
		
	}
	
public boolean checkEndPoint(String endpointURL, String graphName) {
		
		DataStoreConnection dataStoreConn = new DataStoreConnection(endpointURL, graphName);
		dataStoreConn.executeASK("ask { ?x ?c ?e }");
		
		return true;
		
	}

	public boolean retrieveAllClasses() throws SQLException {		
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
		queryString += "SELECT DISTINCT ?s ?parent (count(?ind) as ?Count) "
				+ " FROM " + graphName + " "
						+ "WHERE{ ?s a owl:Class. optional {?ind a ?s.} optional {?s rdfs:subClassOf ?p} "
						+ "bind(IF(?p = '', '' , ?p) AS ?parent) } "
						+ "GROUP BY ?s ?parent "
						+ "ORDER BY ?s ?parent";
		
		List<QuerySolution> list = conn.executeSelect(queryString);
		
		
		ClassService classService = new ClassService(connection);		

		for(QuerySolution soln : list) {
			RDFNode subjectRDFNode = soln.get("s");
			RDFNode parentRDFNode = soln.get("parent");
			
			if (subjectRDFNode.asResource().getURI() == null)
				continue;
			
			long count =  soln.get("Count").asLiteral().getLong();
			Class parentClass = null;
			
			if (parentRDFNode != null) {				
				 parentClass = collectClass(parentRDFNode.asResource());						
			}	
			
			
			Class myClass = new Class(subjectRDFNode.asResource().getURI(), subjectRDFNode.asResource().getLocalName(), "", count, version, parentClass);
			
			myClass = classService.addIfNotExist(myClass);				
			
		}
		
		return true;
		
	}
	
	private Class collectClass(Resource subjectResource) throws SQLException {
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
		queryString += "SELECT DISTINCT ?s ?parent (count(?ind) as ?Count) "
				+ " FROM " + graphName + " "
						+ "WHERE{ optional {?ind a <" + subjectResource.getURI()  + ">.} "
								+ "optional {<" + subjectResource.getURI()  + "> rdfs:subClassOf ?p} "
						+ "bind(IF(?p = '', '' , ?p) AS ?parent) } "
						+ "GROUP BY ?s ?parent "
						+ "ORDER BY ?s ?parent";
		
		List<QuerySolution> records =  conn.executeSelect(queryString);
		RDFNode parentRDFNode = records.get(0).get("parent");
		long count = records.get(0).get("Count").asLiteral().getLong();
				
		Class parentClass = null;	
		if (parentRDFNode != null) {
			
			parentClass = collectClass(parentRDFNode.asResource());
		}
				
		
		ClassService classService = new ClassService(connection);		
		
		Class myClass = new Class(subjectResource.getURI(), subjectResource.getLocalName(), "", count, version, parentClass);
		
		myClass = classService.addIfNotExist(myClass);	
		
		return myClass;
		
	}
	

}
