package edu.uga.cs.ontologycomparision.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import com.github.andrewoma.dexx.collection.ArrayList;

import edu.uga.cs.ontologycomparision.data.DataStoreConnection;
import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.ObjectProperty;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;

public class RetrieveSchemaInUseService {

	
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
	
	public boolean retrieveAllObjectProperties() throws SQLException {
		
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> ";						
		queryStringTriple += "SELECT ?predicate FROM " + graphName + " WHERE {?predicate a owl:ObjectProperty.}";

		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		for(QuerySolution soln : list) {
			RDFNode predicate = soln.get("predicate");
			Resource res = soln.getResource("predicate");

			if (res.getLocalName() != null) {				
				collectObjectProperty(predicate);						
			}			
		}
		
		return true;
	}
	
	private ObjectProperty collectObjectProperty(RDFNode propertyRDFNode) throws SQLException {
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + propertyRDFNode + "> rdfs:subPropertyOf ?parent .}";
		List<QuerySolution> parents =  conn.executeSelect(queryString);
		
		queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + propertyRDFNode + "> rdfs:domain ?domain .}";
		List<QuerySolution> domains =  conn.executeSelect(queryString);
		
		queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + propertyRDFNode + "> rdfs:range ?range .}";
		List<QuerySolution> ranges =  conn.executeSelect(queryString);
		
		ObjectProperty parentProperty = null;	
		if (parents.size() > 0) {	
			parentProperty = collectObjectProperty(parents.get(0).get("parent"));
		}

		Resource propertyResource = propertyRDFNode.asResource();	
		ArrayList<ObjectProperty> objectProperties = new ArrayList<ObjectProperty>();
		
		Class domain = null;
		Class range = null;
		if (domains.size() > 0 && ranges.size() > 0 ) {
			for (QuerySolution domainSoln : domains) { // We might have more than domain and range
				domain = collectClass(domainSoln.getResource("domain"));	
				for (QuerySolution rangeSoln : ranges) {
					range = collectClass(rangeSoln.getResource("range"));
					
					ObjectPropertyService propertyService = new ObjectPropertyService(connection);												
					ObjectProperty objectProperty = new ObjectProperty(propertyResource.getURI(), propertyResource.getLocalName(), "", version, parentProperty, domain, range);
					objectProperty = propertyService.addIfNotExist(objectProperty);	
					objectProperties.append(objectProperty);
					range = null;
				}
				domain = null;
			}			 
		}
		
			
		
		
		
		return objectProperties.get(0);
		
	}
	

}
