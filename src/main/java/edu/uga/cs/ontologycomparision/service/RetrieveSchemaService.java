package edu.uga.cs.ontologycomparision.service;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.data.DataStoreConnection;

public class RetrieveSchemaService {
	
	private String endpointURL;
	private String graphName;
	private Version version;
	
	final static Logger logger = Logger.getLogger(RetrieveSchemaService.class);
	
	public RetrieveSchemaService(String endpointURL, String graphName) throws SQLException {
		this.endpointURL = endpointURL;
		this.graphName = graphName;
		
	}
	
	public RetrieveSchemaService(String endpointURL, String graphName, int versionId) throws SQLException {
		this.endpointURL = endpointURL;
		this.graphName = graphName;
		
		VersionService versionService = new VersionService();
		version = versionService.get(versionId);
		
	}
	
	public boolean checkEndPoint(String endpointURL, String graphName) {
		
		DataStoreConnection dataStoreConn = new DataStoreConnection(endpointURL, graphName);
		dataStoreConn.executeASK("ask { ?x ?c ?e }");
		
		return true;
		
	}

	public boolean retrieveAllClasses() throws SQLException {
		
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect("PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT ?s FROM " + graphName + " WHERE{ ?s a owl:Class.  }");

		for(QuerySolution soln : list) {
			RDFNode subject = soln.get("s");
			Resource res = soln.getResource("s");

			if (res.getLocalName() != null) {				
				collectClass(subject);						
			}			
		}
		
		return true;
		
	}
	
	private Class collectClass(RDFNode classRDFNode) throws SQLException {
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + classRDFNode + "> rdfs:subClassOf ?parent .}";
		List<QuerySolution> classes =  conn.executeSelect(queryString);
		
		queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT (count(?ind) as ?Count) FROM " + graphName + " WHERE{ ?ind a <" + classRDFNode + "> .}";
		List<QuerySolution> individuals =  conn.executeSelect(queryString);
		long count = individuals.get(0).get("Count").asLiteral().getLong();
		
		Class parentClass = null;	
		if (classes.size() > 0) {
			
			parentClass = collectClass(classes.get(0).get("parent"));
		}
		
		Resource classResource = classRDFNode.asResource();
		ClassService classService = new ClassService();		
		Class myClass = new Class(classResource.getURI(), classResource.getLocalName(), "", count, version, parentClass);
		
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
				collectProperty(predicate);						
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
				collectProperty(predicate);						
			}			
		}
		
		return true;
	}
	
	
	private Property collectProperty(RDFNode propertyRDFNode) throws SQLException {
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + propertyRDFNode + "> rdfs:subPropertyOf ?parent .}";
		List<QuerySolution> parents =  conn.executeSelect(queryString);
		
		Property parentProperty = null;	
		if (parents.size() > 0) {
			
			parentProperty = collectProperty(parents.get(0).get("parent"));
		}
		
		Resource propertyResource = propertyRDFNode.asResource();
		PropertyService propertyService = new PropertyService();												
		Property myProperty = new Property(propertyResource.getURI(), propertyResource.getLocalName(), "", 0L, version, parentProperty);
		myProperty = propertyService.addIfNotExist(myProperty);	
		
		return myProperty;
		
	}
	
	public boolean retrieveAllObjectTripleTypes() throws SQLException {
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "SELECT DISTINCT ?domain ?name ?range (COUNT(?object) as ?count) " + 
				"FROM <http://prokino.uga.edu> " + 
				"WHERE {" + 
				"	?name rdf:type owl:ObjectProperty" + 
				"	optional {" + 
				"		?name rdfs:domain ?o." + 
				"		?o owl:unionOf ?l." + 
				"		{?l rdf:first ?domain. } UNION {?l rdf:rest ?rest. ?rest rdf:first ?domain}" + 
				"	}" + 
				"	optional {" + 
				"		?name rdfs:domain ?domain" + 
				"	}" + 
				"	optional {" + 
				"		?name rdfs:range ?range. ?range rdf:type owl:Class" + 
				"	}" + 
				"	?subject ?name ?object" + 
				"} " + 
				"GROUP By ?name ?domain ?range ORDER BY ?name";
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		ClassService classService = new ClassService();
		PropertyService propertyService = new PropertyService();
		ObjectTripleTypeService service = new ObjectTripleTypeService(); 
				
		
		for(QuerySolution soln : list) {
			RDFNode domainNode = soln.get("domain");
			RDFNode predicateNode = soln.get("name");
			RDFNode rangeNode = soln.get("range");
			Literal count = soln.getLiteral("count");					
			
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
													
			ObjectTripleType objectTriple = new ObjectTripleType(domain, predicate, range, count.getLong());			
			service.addIfNotExist(objectTriple);			
					
		}
		
		return true;
	}
	
	public boolean retrieveAllDataTypeTripleTypes() throws SQLException {
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "SELECT DISTINCT ?domain ?name ?range (COUNT(?object) as ?count) " + 
				"FROM " + graphName + " " +
				"WHERE {" + 
				"	?name rdf:type owl:ObjectProperty" + 
				"	optional {" + 
				"		?name rdfs:domain ?o." + 
				"		?o owl:unionOf ?l." + 
				"		{?l rdf:first ?domain. } UNION {?l rdf:rest ?rest. ?rest rdf:first ?domain}" + 
				"	}" + 
				"	optional {" + 
				"		?name rdfs:domain ?domain" + 
				"	}" + 
				"	optional {" + 
				"		?name rdfs:range ?range. ?range rdf:type owl:Class" + 
				"	}" + 
				"	?subject ?name ?object" + 
				"} " + 
				"GROUP By ?name ?domain ?range ORDER BY ?name";
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		ClassService classService = new ClassService();
		PropertyService propertyService = new PropertyService();
		ObjectTripleTypeService service = new ObjectTripleTypeService(); 
				
		
		for(QuerySolution soln : list) {
			RDFNode domainNode = soln.get("domain");
			RDFNode predicateNode = soln.get("name");
			RDFNode rangeNode = soln.get("range");
			Literal count = soln.getLiteral("count");					
			
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
													
			ObjectTripleType objectTriple = new ObjectTripleType(domain, predicate, range, count.getLong());			
			service.addIfNotExist(objectTriple);			
					
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
			DataStoreConnection dataStoreConn = new DataStoreConnection(endpointURL, graphName);			
			RDFNode predicate = soln.get("s");
			Resource res = soln.getResource("s");
			if (res.getLocalName() != null) {
				String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT (count(?s) as ?Count) FROM " + graphName + " WHERE{ ?s <" + predicate + "> ?o .}";
				List<QuerySolution> individuals =  dataStoreConn.executeSelect(queryString);
				Literal count = individuals.get(0).get("Count").asLiteral();							
				Property myProperty = new Property(res.getURI(), res.getLocalName(), "", count.getLong(), version, null);
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
			Literal count = soln.getLiteral("count");
			
			if (predicate.getLocalName() != null) {							
				Property myProperty = new Property(predicate.getURI(), predicate.getLocalName(), "", count.getLong(), version, null);
				propertyList2.add(myProperty);
			}
			
		}
		
		System.out.println("Property List 1 :" + propertyList);
		System.out.println("Property List 2 :" + propertyList2);
		
		
	}
	
}
