package edu.uga.cs.ontologycomparision.service;



import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.http.HttpConnection;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.JenaException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.Expression;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Restriction;
import edu.uga.cs.ontologycomparision.model.RestrictionType;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.XSDType;
import edu.uga.cs.ontologycomparision.data.DataStoreConnection;
import edu.uga.cs.ontologycomparision.data.HTTPConnection;
import edu.uga.cs.ontologycomparision.data.MySQLConnection;

public class RetrieveSchemaService {
	
	final static boolean test = true; //for retrieve restriction from LOD
	
	final static String ObjectPropertyType = "ObjectProperty";
	final static String DatatypePropertyType = "DatatypeProperty";
	
	final static Logger logger = Logger.getLogger(RetrieveSchemaService.class);
	
	private String endpointURL;
	private String graphName;
	private Version version;
	private List<Class> classes;
	
	
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
				 parentClass = collectClass(parentRDFNode.asResource().toString());						
			}	
			
			
			Class myClass = new Class(subjectRDFNode.asResource().getURI(), subjectRDFNode.asResource().getLocalName(), "", count, version, parentClass);
			
			myClass = classService.addIfNotExist(myClass);				
			
		}
		
		return true;
		
	}
	
	private Class collectClass(String subjectString) throws SQLException {
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
		queryString += "SELECT DISTINCT ?s ?parent (count(?ind) as ?Count) "
				+ (graphName.isBlank()? "" : " FROM " + graphName )+ " "
						+ "WHERE{ optional {?ind a <" + subjectString  + ">.} "
								+ "optional {<" + subjectString  + "> rdfs:subClassOf ?p} "
						+ "bind(IF(?p = '', '' , ?p) AS ?parent)  "
						+ "bind(<" + subjectString + "> AS ?s) }"
						+ "GROUP BY ?s ?parent "
						+ "ORDER BY ?s ?parent";
		
		List<QuerySolution> records =  conn.executeSelect(queryString);
		RDFNode parentRDFNode = records.get(0).get("parent");
		Resource subjectResource = records.get(0).get("s").asResource();
		
		long count = records.get(0).get("Count").asLiteral().getLong();
				
		Class parentClass = null;	
		if (parentRDFNode != null) {
			parentClass = collectClass(parentRDFNode.toString());
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
				collectProperty(predicate.toString(), ObjectPropertyType);						
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
				collectProperty(predicate.toString(), DatatypePropertyType);						
			}			
		}
		
		return true;
	}
	
	
	private Property collectProperty(String propertyString, String type) throws SQLException {
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent ?p"
				+ (graphName.isBlank()? "" : "FROM " + graphName) + " WHERE{ <" + propertyString + "> rdfs:subPropertyOf ?parent .bind(<" + propertyString + "> AS ?p)}";
		List<QuerySolution> parents =  conn.executeSelect(queryString);
		
		Property parentProperty = null;	
		if (parents.size() > 0) {
			
			parentProperty = collectProperty(parents.get(0).get("parent").toString(), type);
		}
				
		Resource propertyResource = parents.get(0).get("p").asResource();
				
		PropertyService propertyService = new PropertyService(connection);												
		Property myProperty = new Property(propertyResource.getURI(), propertyResource.getLocalName(),type, "", version, parentProperty);
		myProperty = propertyService.addIfNotExist(myProperty);	
		
		return myProperty;
		
	}
	
	public boolean retrieveAllObjectTripleTypes() throws SQLException {
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "SELECT distinct (?sc as ?domain) (?p as ?property) (?oc as ?range) (count(?s) as ?count)" + 
				"WHERE{" + 
				"   ?p a owl:ObjectProperty." + 
				"   ?s ?p ?o." + 
				"   ?s a ?sc." + 
				"   ?o a ?oc" + 
				"}" +
				"GROUP BY ?sc ?p ?oc " +
				"ORDER BY ?sc ?p ?oc";
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);		
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		ClassService classService = new ClassService(connection);		
		
		PropertyService propertyService = new PropertyService(connection);
		ObjectTripleTypeService service = new ObjectTripleTypeService(connection); 
				
		
		for(QuerySolution soln : list) {
			RDFNode domainNode = soln.get("domain");
			RDFNode predicateNode = soln.get("property");
			RDFNode rangeNode = soln.get("range");
//			long count = retrieveCountforTriples(domainNode,predicateNode,rangeNode);					
			long count = soln.get("count").asLiteral().getLong();
					
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
													
			ObjectTripleType objectTriple = new ObjectTripleType(domain, predicate, range, count,version);			
			service.addIfNotExist(objectTriple);			
					
		}
		
		
		return true;
	}
	
	public long retrieveCountforTriples(RDFNode domain, RDFNode property, RDFNode range) {
		
		long result = 0;
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "select (COUNT(?s) as ?count)" + 
				"WHERE {" + 
				"?s <" + property + "> ?o." ;
		
		String[] restrictionList = {"?s a <" + domain + ">. ?o a <" + range + ">.}" , "?s a ?k. ?k rdfs:subClassOf* <" + domain + ">. ?o a <" + range + ">.}",
				"?s a ?k. ?k rdfs:subClassOf* <" + domain + ">. ?o a ?c. ?c rdfs:subClassOf* <" + range + ">.}", "?s a <" + domain + ">. ?o a ?c. ?c rdfs:subClassOf* <" + range + ">.} " };
		
		
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);		
		
		for (String rest : restrictionList) {
			List<QuerySolution> list = conn.executeSelect(queryStringTriple + rest);
			
			long value = list.get(0).getLiteral("count").getLong();
			if (value == 0 ) {
			}
			result += value;
		}
		
		return result;
		
		
	}
	
	public boolean retrieveAllDataTypeTripleTypes() throws SQLException {
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "SELECT (?sc as ?domain) (?p as ?property) (count(?s) as ?count) "
				+ "WHERE{ ?p a owl:DatatypeProperty. ?s ?p ?o. ?s a ?sc. } "
				+ "GROUP BY ?sc ?p "
				+ "ORDER BY ?sc ?p" ;
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		ClassService classService = new ClassService(connection);		
		
		PropertyService propertyService = new PropertyService(connection);
		XSDTypeService xsdTypeService = new XSDTypeService(connection);
		DataTypeTripleTypeService service = new DataTypeTripleTypeService(connection); 
					
		for(QuerySolution soln : list) {
			RDFNode domainNode = soln.get("domain");
			RDFNode predicateNode = soln.get("property");
//			RDFNode rangeNode = soln.get("range"); we need to find the range for predicate later
			long count = soln.get("count").asLiteral().getLong();			
//			long count = retrieveCountforTriples(domainNode, predicateNode, rangeNode);										
			
			Class domain;
			Property predicate;
			XSDType range;
			
			List<QuerySolution> rangeList = conn.executeSelect("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT ?range WHERE{ optional { <" + predicateNode.asResource() + "> rdfs:range ?range} optional { <" + predicateNode.asResource()  + "> rdfs:subClassOf* ?c. ?c rdfs:range ?range} }" );
			RDFNode rangeNode = rangeList.get(0).get("range");
			try {
				range = new XSDType(rangeNode.asResource().getURI(), rangeNode.asResource().getLocalName());
				range = xsdTypeService.addIfNotExist(range);	
			} catch (NullPointerException e) {
				range = null;
				logger.warn("RetrieveSchemaService.retrieveAllObjectTypeTriples : range is missing for domain: " + domainNode + " and predicate: " + predicateNode);
			}
			
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
			
			
													
			DataTypeTripleType dataTypeTriple = new DataTypeTripleType(domain, predicate, range, count,version);			
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
		
		
	}
	
	public boolean retrieveAllRestrictions() throws SQLException {
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFrom  = "SELECT ?s";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?s FROM " + graphName;
		queryStringTriple += selectFrom + " WHERE {?s rdf:type owl:Restriction.}";
		
		if (test )
			queryStringTriple += " ORDER BY ?s LIMIT 1000";

		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		RestrictionService restrictionService = new RestrictionService(connection);
		RestrictionTypeService restrictionTypeService = new RestrictionTypeService(connection);
		List<RestrictionType> restrictionTypes = restrictionTypeService.getListAll();
		
		Map<String, Integer> restrictionTypeMap = restrictionTypes.stream().collect(
                Collectors.toMap(RestrictionType::getType, RestrictionType::getID));
		
		for(QuerySolution soln : list) {
						
			RDFNode node = soln.get("s");
					
			Restriction restriction = findRestrictionByNode(node, restrictionTypeMap);
			if (restriction != null)
				restrictionService.add(restriction);
		}
		
		return true;
		
	}
	
	public Restriction findRestrictionByNode(RDFNode node, Map<String, Integer> restrictionTypeMap) throws SQLException {
		String selectFrom  = "SELECT ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?p ?o FROM " + graphName;
		
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> ";						
		queryStringTriple += selectFrom + " WHERE {<" + node.asResource() + "> ?p ?o}";

		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		RestrictionType type = null;
		Property onProperty = null;
		Class onClass = null;
		int cardinalityValue = 0;
		
		try {
		
			for(QuerySolution soln : list) {
				
				RDFNode predicateNode = soln.get("p");
				RDFNode objectNode = soln.get("o");			
				
				String predicate = removeNS(predicateNode.asResource().getLocalName());
				
				if (restrictionTypeMap.containsKey(predicate)) {			
					RestrictionTypeService restrictionTypeService = new RestrictionTypeService(connection);
					RestrictionType restrictionType = new RestrictionType(predicate) ;
					type = restrictionTypeService.addIfNotExist(restrictionType);
					
					if (containsCardinality(predicate)) {
						cardinalityValue = objectNode.asLiteral().getInt();
					}
				}
				else if (predicate.equals("onProperty")){
					
					onProperty = collectProperty(objectNode.asResource().toString(), ObjectPropertyType);
				}
				else if (predicate.equals("onClass")){
					
					onClass = collectClass(objectNode.asResource().toString());
				}				
				else {
					logger.warn("RetrieveSchemaService.findRestrictionByNode : Cannot find the predicate of restriction");
				}
							
			}//for
			
			return( new Restriction(onProperty, type, cardinalityValue, onClass, version));
		}
		catch (JenaException jex) {
			logger.error("RetrieveSchemaService.findRestrictionByNode : error in Jena conversion Node to Resource. The record Skipped.");
			return null;
		}
		
		
	}
	
	private String removeNS(String name) {
		return name.replace("owl:", "");
	}
	
	private boolean containsCardinality(String name) {
		if (name.toLowerCase().indexOf("cardinality") != -1) {
			return true;
		}
		return false;
		
	}
	
	public boolean retrieveAllExpressions() throws SQLException, IOException, InterruptedException {
		ExpressionService expressionService = new ExpressionService(connection);
					
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFrom  = "SELECT ?s ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?s ?p ?o FROM " + graphName;
		queryStringTriple += selectFrom + " WHERE {?s ?p ?o. FILTER( ?p IN(owl:unionOf, owl:intersectionOf) ) }";
		
		if (test )
			queryStringTriple += " ORDER BY ?s LIMIT 20";
					
		HTTPConnection http = new HTTPConnection(endpointURL, queryStringTriple);
		ArrayList<ArrayList<String>> list = parseJson(http.execute());
		
		for(ArrayList<String> row : list) {
									
			String subject = "", predicate = "", object="";
			for(String item : row) {
				int index = item.indexOf(":");
				switch (item.substring(0,index)) {
				case "s":
					subject = item.substring(index+1, item.length());
					break;
				case "p":
					predicate = item.substring(index+1, item.length());
					break;
				case "o":
					object = item.substring(index+1, item.length());
					break;
				default:
					break;
				}
			}
			String predicateLocalName = getLocalName(predicate);
			if (!subject.equals("http://www.w3.org/2002/07/owl#Thing")) {
				if (predicateLocalName.equals("unionOf") || predicateLocalName.equals("intersectionOf")) {
					String type = "";
					type = getLocalName(predicate);
					classes = new LinkedList<Class>();
					
					//call to recursive method
					classes = findClasses(object, classes);
					
					// retrieve where the expression were used
					Class myClass = null;
					Property property = null;
					
					
					String query= "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
					selectFrom  = "SELECT ?s ?p ?o";
					
					if (!graphName.isBlank())
						selectFrom = "SELECT ?s ?p ?o FROM " + graphName;
					query += selectFrom + " WHERE {?s ?p ?o. FILTER( ?p IN(owl:unionOf, owl:intersectionOf) ) }";
					
					if (test )
						query += " ORDER BY ?s LIMIT 20";
					
					HTTPConnection http2 = new HTTPConnection(endpointURL, query);
					
					ArrayList<ArrayList<String>> usedInList = parseJson(http2.execute());
					for(ArrayList<String> usedIn : usedInList) {
						String subjectUsedIn = "";
						String predicateUsedIn = "";
						
						for(String item : usedIn) {
							int index = item.indexOf(":");
							switch (item.substring(0,index)) {
							case "s":
								subjectUsedIn = item.substring(index+1, item.length());
								break;
							case "p":
								predicateUsedIn = item.substring(index+1, item.length());
								break;
							default:
								break;
							}
						}
						
						myClass = collectClass(subjectUsedIn);
						property = collectProperty(predicateUsedIn, "");
					}
	
					//add the expression here
					Expression expression = new Expression(type,myClass, property, classes, version);
					
					expressionService.add(expression);
				}	
			}								
		}				
		return true;
		
	}
	
	private List<Class> findClasses(String strNode, List<Class> classes) throws JenaException, SQLException, IOException{
		
			
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFrom  = "SELECT ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?p ?o FROM " + graphName;
		queryStringTriple += selectFrom + " WHERE {<" + strNode + "> ?p ?o. }";
		
		if (test )
			queryStringTriple += " ORDER BY ?p ?o LIMIT 1000";

		HTTPConnection http = new HTTPConnection(endpointURL, queryStringTriple);
		ArrayList<ArrayList<String>> list = parseJson(http.execute());
		
		for(ArrayList<String> row : list) {
			String predicate = "", object="";
			for(String item : row) {
				int index = item.indexOf(":");
				switch (item.substring(0,index)) {
				case "p":
					predicate = item.substring(index+1, item.length());
					break;
				case "o":
					object = item.substring(index+1, item.length());
					break;
				default:
					break;
				}
			}
			String predicateLocalName = getLocalName(predicate);
			if (predicateLocalName.equals("first")) {
				
				
				Class myClass = collectClass(object);
				classes.add(myClass);
			}
			else if (predicateLocalName.equals("rest")) {
					if (!object.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#nil")	)					
						classes = findClasses(getLocalName(object), classes);				
			}
			else {
				logger.warn("RetrieveSchemaService.findClasses : UNKOWN predicate in Sequence : " + predicate);
			}										
			
		}
								
		return classes;
		
	}
	
	public ArrayList<ArrayList<String>> parseJson(String strJSON) {
		try {
			
			ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
			
			JSONObject jsonObject = new JSONObject(strJSON);
			
			//Read the variable names
			JSONArray varsObj = new JSONArray(new JSONObject(jsonObject.get("head").toString()).get("vars").toString());
			String[] vars = new String[varsObj.length()];
			
			for (int i = 0; i < varsObj.length(); i++) {
				vars[i] = varsObj.getString(i);
			}
			
			JSONObject resultObject = jsonObject.getJSONObject("results");			
			JSONArray array = new JSONArray(resultObject.get("bindings").toString());
			
			for (int i = 0; i < array.length(); i++) {
				
				ArrayList<String> result = new ArrayList<String>();
				JSONObject row = array.getJSONObject(i);
				
				for (int j = 0 ; j < vars.length ; j++) {
					String var = row.get(vars[j]).toString();
					JSONObject p = new JSONObject(var);
					result.add(vars[j] + ":" + p.get("value").toString());
				}
				
				results.add(result);

			}
		    
			return results;
		     
		}catch (JSONException err){
		     System.out.println("Error json.");
		}
		
		return null;
	}
	
	private String getLocalName(String uri) {
		return uri.substring(uri.indexOf("#")+1 , uri.length());
	}
	
	
}
