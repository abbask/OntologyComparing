package edu.uga.cs.ontologycomparision.service;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.XSDType;
import edu.uga.cs.ontologycomparision.data.DataStoreConnection;
import edu.uga.cs.ontologycomparision.data.MySQLConnection;

public class RetrieveSchemaService {
	
	final static String ObjectPropertyType = "ObjectProperty";
	final static String DatatypePropertyType = "DatatypeProperty";
	
	final static Logger logger = Logger.getLogger(RetrieveSchemaService.class);
	
	private String endpointURL;
	private String graphName;
	private Version version;
	
	private Connection connection;	
	
	public RetrieveSchemaService(String endpointURL, String graphName) throws SQLException {
		this.endpointURL = endpointURL;
		this.graphName = graphName;
		
	}
	
	public RetrieveSchemaService(String endpointURL, String graphName, int versionId) throws SQLException {
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
				collectProperty(predicate, ObjectPropertyType);						
			}			
		}
		
		return true;
	}
	
	public boolean retrieveAllDataTypeProperties() throws SQLException {
		
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> ";						
		queryStringTriple += "SELECT ?predicate FROM " + graphName + " WHERE {?predicate a owl:DatatypeProperty.}";

		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		for(QuerySolution soln : list) {
			RDFNode predicate = soln.get("predicate");
			Resource res = soln.getResource("predicate");

			if (res.getLocalName() != null) {				
				collectProperty(predicate, DatatypePropertyType);						
			}			
		}
		
		return true;
	}
	
	
	private Property collectProperty(RDFNode propertyRDFNode, String type) throws SQLException {
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + propertyRDFNode + "> rdfs:subPropertyOf ?parent .}";
		List<QuerySolution> parents =  conn.executeSelect(queryString);
		
		Property parentProperty = null;	
		if (parents.size() > 0) {
			
			parentProperty = collectProperty(parents.get(0).get("parent"), type);
		}
		
		Resource propertyResource = propertyRDFNode.asResource();
		
		
		PropertyService propertyService = new PropertyService(connection);												
		Property myProperty = new Property(propertyResource.getURI(), propertyResource.getLocalName(),type, "", version, parentProperty);
		myProperty = propertyService.addIfNotExist(myProperty);	
		
		return myProperty;
		
	}
	
	public boolean retrieveAllObjectTripleTypes() throws SQLException {
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "SELECT DISTINCT ?domain ?name ?range " + 
				"FROM " + graphName + " " +
				"WHERE {" + 
				"	?name rdf:type owl:ObjectProperty" + 
				"	optional {" + 
				"		?name rdfs:domain ?o." + 
				"		?o owl:unionOf ?l." + 
				"		{?l rdf:first ?domain. } UNION {?l rdf:rest* ?rest. ?rest rdf:first ?domain}" + 
				"	}" + 
				"	optional {" + 
				"		?name rdfs:domain ?domain" + 
				"	}" + 
				"	optional {" + 
				"		?name rdfs:range ?range. ?range rdf:type owl:Class" + 
				"	}" + 
				"} " + 
				"GROUP By ?name ?domain ?range ORDER BY ?name";
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);		
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		ClassService classService = new ClassService(connection);		
		
		PropertyService propertyService = new PropertyService(connection);
		ObjectTripleTypeService service = new ObjectTripleTypeService(connection); 
				
		
		for(QuerySolution soln : list) {
			RDFNode domainNode = soln.get("domain");
			RDFNode predicateNode = soln.get("name");
			RDFNode rangeNode = soln.get("range");
			Literal count = retrieveCountforTriples(domainNode,predicateNode,rangeNode);					
			
			Class domain, range;
			Property predicate;
			
			try {
				domain = classService.getByLabel(domainNode.asResource().getLocalName(), version.getID());				
			} catch (NullPointerException e) {
				domain = null;
				logger.warn("RetrieveSchemaService.retrieveAllObjectTypeTriples : domain is missing for predicate: " + predicateNode + " and range: " + rangeNode);
			}
			
			try {
				predicate = propertyService.getByLabel(predicateNode.asResource().getLocalName(), version.getID());			
			} catch (NullPointerException e) {
				predicate = null;
				logger.warn("RetrieveSchemaService.retrieveAllObjectTypeTriples : predicate is missing for domain: " + domainNode + " and range: " + rangeNode);
			}
			
			try {
				range = classService.getByLabel(rangeNode.asResource().getLocalName(), version.getID());				
			} catch (NullPointerException e) {
				range = null;
				logger.warn("RetrieveSchemaService.retrieveAllObjectTypeTriples : range is missing for domain: " + domainNode + " and predicate: " + predicateNode);
			}
													
			ObjectTripleType objectTriple = new ObjectTripleType(domain, predicate, range, count.getLong(),version);			
			service.addIfNotExist(objectTriple);			
					
		}
		
		
		return true;
	}
	
	public Literal retrieveCountforTriples(RDFNode domain, RDFNode property, RDFNode range) {
		Literal result = null;
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "select (COUNT(?s) as ?count)" + 
				"WHERE {" + 
				"?s ?p ?o." + 
				"?s a <" + domain + ">." +
				"?s <" + property + "> ?o." + 
				"optional { ?o a <" + range + ">}" + 
				"optional { ?o a ?c. ?c rdfs:subClassOf* <" + range + ">}" + 
				"\n" + 
				"}";
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);		
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		if (list.size() > 0 )
			result = list.get(0).get("count").asLiteral();
		
		return result;
	}
	
	public boolean retrieveAllDataTypeTripleTypes() throws SQLException {
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "SELECT DISTINCT ?domain ?name ?range" + 
				"FROM " + graphName + " " +
				"WHERE { ?name rdf:type owl:DatatypeProperty " + 
				"optional {?name rdfs:domain ?o. ?o owl:unionOf ?l. {?l rdf:first ?domain.} UNION {?l rdf:rest* ?rest. ?rest rdf:first ?domain}}" + 
				"optional {?name rdfs:domain ?domain} " + 
				"optional {?name rdfs:range ?range} " + 				
				"}" + 
				"GROUP By ?name ?domain ?range " + 
				"ORDER BY ?name ?domain ?range";
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		ClassService classService = new ClassService(connection);		
		
		PropertyService propertyService = new PropertyService(connection);
		XSDTypeService xsdTypeService = new XSDTypeService(connection);
		DataTypeTripleTypeService service = new DataTypeTripleTypeService(connection); 
		
				
		
		for(QuerySolution soln : list) {
			RDFNode domainNode = soln.get("domain");
			RDFNode predicateNode = soln.get("name");
			RDFNode rangeNode = soln.get("range");
			Literal count = retrieveCountforTriples(domainNode, predicateNode, rangeNode);					
			
			Class domain;
			Property predicate;
			XSDType range;
			
			try {
				domain = classService.getByLabel(domainNode.asResource().getLocalName(), version.getID());				
			} catch (NullPointerException e) {
				domain = null;
				logger.warn("RetrieveSchemaService.retrieveAllObjectTypeTriples : domain is missing for predicate: " + predicateNode + " and range: " + rangeNode);
			}
			
			try {
				predicate = propertyService.getByLabel(predicateNode.asResource().getLocalName(), version.getID());			
			} catch (NullPointerException e) {
				predicate = null;
				logger.warn("RetrieveSchemaService.retrieveAllObjectTypeTriples : predicate is missing for domain: " + domainNode + " and range: " + rangeNode);
			}
			
			try {
				range = new XSDType(rangeNode.asResource().getURI(), rangeNode.asResource().getLocalName());
				range = xsdTypeService.addIfNotExist(range);	
			} catch (NullPointerException e) {
				range = null;
				logger.warn("RetrieveSchemaService.retrieveAllObjectTypeTriples : range is missing for domain: " + domainNode + " and predicate: " + predicateNode);
			}
													
			DataTypeTripleType dataTypeTriple = new DataTypeTripleType(domain, predicate, range, count.getLong(),version);			
			service.addIfNotExist(dataTypeTriple);			
					
		}
		
		return true;
	}
	
	public void checkDifferenceBetween2Approaches() throws SQLException {
		ArrayList<Property> propertyList = new ArrayList<Property>(); 		
		ArrayList<Property> propertyList2 = new ArrayList<Property>();  
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		String q = "PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT ?s FROM " + graphName + " WHERE{ ?s a owl:ObjectProperty.  }";
		List<QuerySolution> propertySolns = conn.executeSelect(q);
		System.out.println(q);
		for(QuerySolution soln : propertySolns) {
			Resource res = soln.getResource("s");
			if (res.getLocalName() != null) {											
				Property myProperty = new Property(res.getURI(), res.getLocalName(),ObjectPropertyType, "", version, null);
				propertyList.add(myProperty);
				
			}
			
		}
		
		String queryStringTriple = "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX owl:    <http://www.w3.org/2002/07/owl#> "
				+ "PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
		
		queryStringTriple += "SELECT DISTINCT ?domain ?name ?range (COUNT(?object) as ?count) "
				+ "FROM " + graphName + "  "
						+ "WHERE { ?name rdf:type owl:ObjectProperty "
						+ "optional { ?name rdfs:domain ?o. ?o owl:unionOf ?l.  "
						+ "{?l rdf:first ?domain. } UNION {?l rdf:rest ?rest. ?rest rdf:first ?domain}} "
						+ "optional {?name rdfs:domain ?domain} "
						+ "optional {?name rdfs:range ?range. ?range rdf:type owl:Class} "
						+ "?subject ?name ?object} "
						+ "GROUP By ?name ?domain ?range ORDER BY ?name";

		List<QuerySolution> tripleSolns = conn.executeSelect(queryStringTriple);
		for(QuerySolution soln : tripleSolns) {
			
			
			Resource predicate = soln.getResource("name");
			
			if (predicate.getLocalName() != null) {							
				Property myProperty = new Property(predicate.getURI(), predicate.getLocalName(),DatatypePropertyType, "", version, null);
				propertyList2.add(myProperty);
			}
			
		}
		
		System.out.println("Property List 1 :" + propertyList);
		System.out.println("Property List 2 :" + propertyList2);
		
		
	}
	
}
