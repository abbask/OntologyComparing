package edu.uga.cs.ontologycomparision.service;



import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.http.HttpConnection;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.AnonId;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.RDFVisitor;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.JenaException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectReader;

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
	
	final static boolean test = false; //for retrieve restriction from LOD
	
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
	
	public int retrieveClassCount() throws SQLException{
		int count = 0 ;
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> ";
		queryString += "SELECT (count(?s) as ?count)"
				+ (graphName.isBlank()? "" : "FROM " + graphName) + " "
						+ "WHERE{ ?s a owl:Class. } ";
		
		List<QuerySolution> list = conn.executeSelect(queryString);
		
		count = list.get(0).get("count").asLiteral().getInt();
		
		return count;
	}

	public boolean retrieveAllClasses() throws IOException, SQLException {		
		
		int numberofLimit = 1000;
		
		int countClasses = retrieveClassCount();
		System.out.println(countClasses);
		int page = countClasses / numberofLimit;
		
		for(int i = 0 ; i < page ; i++) {
			
			HTTPConnection http = new HTTPConnection(endpointURL);
		
			String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> ";
			queryString += " SELECT ?s " + (graphName.isBlank()? "" : "FROM " + graphName) + " WHERE { ?s a owl:Class.}"
							+ " LIMIT " + numberofLimit + " OFFSET " + i * numberofLimit;
			http.setSparqlQuery(queryString);
			String strResult = http.execute();

			
			ArrayList<ArrayList<String>> list = parseJson(strResult);
			
			System.out.println("page: " + i + ": " + queryString);			
			
			
			for (ArrayList<String> row : list) {
				
				
				String[] vars = new String[] {"s"};	
				HashMap <String, Resource> map = extractItemResources(row, vars);
				
				collectClass(map.get("s").toString(), http);	
				
//				String classLabel = soln.get("label").asLiteral().getString();
//				RDFNode parentRDFNode = soln.get("parent");
//				
//				
//				if (subjectRDFNode.asResource().getURI() == null){
//					continue;
//				}
//	
//				long count =  soln.get("Count").asLiteral().getLong();
//				Class parentClass = null;
//				
//				if (parentRDFNode != null) {
//					
//					 parentClass = collectClass(parentRDFNode.asResource().toString());						
//				}	
//				
//				
//				
//				Class myClass = new Class(subjectRDFNode.asResource().getURI(),classLabel  , subjectRDFNode.asResource().getLocalName(), count, version, parentClass);
////				System.out.println(myClass);
//				myClass = classService.addIfNotExist(myClass);				
				
			}
		}
		
		return true;
		
	}
	
	private Class collectClass(String subjectString, HTTPConnection http) throws SQLException, IOException {
		
		if (subjectString.isEmpty())
			return null;
		
		ClassService classService = new ClassService(connection);
		Class myClass = classService.getByURI(subjectString, version.getID());
//		System.out.println(myClass);
		if (myClass == null ) {
			if (http.isUsed())
				http = new HTTPConnection(endpointURL);

			String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>";
			queryString += "SELECT DISTINCT ?s IF(bound(?label), ?label, '') as ?label IF(bound(?parent), ?parent, '') as ?parent (count(?ind) as ?Count) "
					+ (graphName.isBlank()? "" : " FROM " + graphName )+ " "
							+ "WHERE{optional{?s rdfs:label ?label.} optional {?ind a <" + subjectString  + ">.} "	
							+ "optional{<" + subjectString + "> rdfs:subClassOf ?parent. ?parent a owl:Class. }"		
							+ "VALUES (?s) {(<" + subjectString + "> )}  }" 
							+ "GROUP BY ?s ?parent ?label "
							+ "ORDER BY ?s ?parent ?label ";
			http.setSparqlQuery(queryString);
			ArrayList<ArrayList<String>> list = parseJson(http.execute());
				
			if (list.size() == 0 ) 
				return null;
			String[] vars = new String[] {"s", "label", "parent", "Count"};
			HashMap<String, Resource> items = extractItemResources(list.get(0), vars);	
			
			Resource subjectResource = items.get("s");
			Resource labelResource = items.get("label");
			long count  = Long.valueOf(items.get("Count").toString());
			
			List<Class> parents = new ArrayList<Class>();
			
			for (ArrayList<String> item : list) {
				String[] parentVars = new String[] {"parent"};	
				HashMap <String, Resource> map = extractItemResources(item, parentVars);
				
				Class parentClass = collectClass(map.get("parent").toString(), http);
				if (parentClass != null)
					parents.add(parentClass);
			}
			
			myClass = new Class(subjectResource.getURI(),labelResource.toString() ,subjectResource.getLocalName(), count, version, parents);
			
			myClass = classService.addIfNotExist(myClass);
			
		}
		else {
//			System.out.println("Not null");
		}
		
		return myClass;
		
	}
	
	
	public boolean retrieveAllObjectProperties() throws SQLException, IOException {
		
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> ";						
		queryStringTriple += "SELECT ?predicate " + (graphName.isBlank()? "" : "FROM " + graphName) + " WHERE {?predicate a owl:ObjectProperty.}";
		
		
		
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
	
	public boolean retrieveAllDataTypeProperties() throws SQLException, IOException {
		
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";						
		queryStringTriple += "SELECT ?predicate" + (graphName.isBlank()? "" : " FROM " + graphName) + "WHERE {?predicate rdf:type owl:DatatypeProperty.}";
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		for(QuerySolution soln : list) {
			RDFNode predicate = soln.get("predicate");
			Resource res = soln.getResource("predicate");
			if (res == null)
				continue;
			if (res.getLocalName() != null) {				
				collectProperty(predicate.toString(), DatatypePropertyType);						
			}			
		}
		
		return true;
	}
	
	
	private Property collectProperty(String propertyString, String type) throws SQLException, IOException {
		
		if (propertyString.isEmpty())
			return null;
		Property myProperty ;

		
		String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT IF(bound(?parent), ?parent, '') as ?parent ?p "
				+ (graphName.isBlank()? "" : "FROM " + graphName) + " WHERE{ optional{?p rdfs:subPropertyOf ?parent} .values (?p ) {(<" + propertyString + ">)}}";
		
//		System.out.println(queryString);
		HTTPConnection http = new HTTPConnection(endpointURL, queryString);
		ArrayList<ArrayList<String>> list = parseJson( http.execute() ) ;
		
		
		Property parentProperty = null;	
		
		String[] vars = new String[] {"p", "parent"};
		HashMap<String, Resource> items = extractItemResources(list.get(0), vars);	
		
		Resource propertyResource = items.get("p");
		Resource parentResource = items.get("parent");
				
		if (parentResource != null) {
			
			parentProperty = collectProperty(parentResource.toString(), type);
		}
				
		PropertyService propertyService = new PropertyService(connection);												
		myProperty = new Property(propertyResource.getURI(), propertyResource.getLocalName(),type, "", version, parentProperty);
		myProperty = propertyService.addIfNotExist(myProperty);	
		
		return myProperty;
		
	}
	
	public boolean retrieveAllObjectTripleTypes() throws SQLException {
		
		String queryStringTriple = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";	
		
		queryStringTriple += "SELECT distinct (?sc as ?domain) (?p as ?property) (?oc as ?range) (count(?s) as ?count)" +
				(graphName.isBlank()? "" : " FROM " + graphName )+ " " +
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
				domain = classService.getByURI(domainNode.asResource().toString(), version.getID());				
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
				range = classService.getByURI(rangeNode.asResource().toString(), version.getID());				
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
				 (graphName.isBlank()? "" : " FROM " + graphName )+ " " +
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
				+ (graphName.isBlank()? "" : " FROM " + graphName )+ " "
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
			
			List<QuerySolution> rangeList = conn.executeSelect("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT ?range " + (graphName.isBlank()? "" : " FROM " + graphName )+ " WHERE{ optional { <" + predicateNode.asResource() + "> rdfs:range ?range} optional { <" + predicateNode.asResource()  + "> rdfs:subClassOf* ?c. ?c rdfs:range ?range} }" );
			RDFNode rangeNode = rangeList.get(0).get("range");
			try {
				range = new XSDType(rangeNode.asResource().getURI(), rangeNode.asResource().getLocalName());
				range = xsdTypeService.addIfNotExist(range);	
			} catch (NullPointerException e) {
				range = null;
				logger.warn("RetrieveSchemaService.retrieveAllObjectTypeTriples : range is missing for domain: " + domainNode + " and predicate: " + predicateNode);
			}
			
			try {
				domain = classService.getByURI(domainNode.asResource().toString(), version.getID());				
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
	
	public boolean retrieveAllRestrictions() throws SQLException, IOException {
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFrom  = "SELECT ?s";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?s FROM " + graphName;
		queryStringTriple += selectFrom + " WHERE {?s rdf:type owl:Restriction.}";
		
		if (test )
			queryStringTriple += " ORDER BY ?s LIMIT 20";

//		System.out.println(queryStringTriple);
		
		HTTPConnection http = new HTTPConnection(endpointURL, queryStringTriple);
		ArrayList<ArrayList<String>> list = parseJson(http.execute());
		
		RestrictionService restrictionService = new RestrictionService(connection);
		RestrictionTypeService restrictionTypeService = new RestrictionTypeService(connection);
		List<RestrictionType> restrictionTypes = restrictionTypeService.getListAll();
		
		Map<String, Integer> restrictionTypeMap = restrictionTypes.stream().collect(
                Collectors.toMap(RestrictionType::getType, RestrictionType::getID));
		
//		restrictionTypeMap.forEach((K,V)->{System.out.println(V);});
		
		for(ArrayList<String> row : list) {
//			System.out.println("row: " + row);
			String[] vars = new String[] {"s"};
			Resource subjectResource = extractItemResources(row, vars).get("s");
			
			
			
//				String subjectLocalName = getAppropriateLocalName(subject);
			
//				System.out.println("subject: " + subject + " subjectLocalName: " + subjectLocalName);
//				
			Restriction restriction = findRestrictionByNode(subjectResource.toString(), restrictionTypeMap, http);
			if (restriction != null)
				restrictionService.add(restriction);

		}
		
		return true;
		
	}	
	
	public Restriction findRestrictionByNode(String node, Map<String, Integer> restrictionTypeMap, HTTPConnection http) throws SQLException, IOException {
		
		String selectFrom  = "SELECT ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?p ?o FROM " + graphName;
		
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> ";						
		queryStringTriple += selectFrom + " WHERE {<" + node + "> ?p ?o}";

//		System.out.println(queryStringTriple);
		http = new HTTPConnection(endpointURL, queryStringTriple);
		ArrayList<ArrayList<String>> list = parseJson(http.execute());
				
		RestrictionType type = null;
		Property onProperty = null;
		Class onClass = null;
		String value = null;
		
		
		try {
		
			for(ArrayList<String> row : list) {
//				System.out.println("row: " + row);
				String[] vars = new String[] {"s","p","o"};
				HashMap<String, Resource> map = extractItemResources(row, vars);
				
				Resource predicateResource = map.get("p");
				Resource objectResource = map.get("o");	
				
//				System.out.println("p: " + predicateResource + ", o:" + objectResource.toString());
				
				String predicate = removeNS(predicateResource.getLocalName());
				
				if (restrictionTypeMap.containsKey(predicate)) {			
					RestrictionTypeService restrictionTypeService = new RestrictionTypeService(connection);
					RestrictionType restrictionType = new RestrictionType(predicate) ;
					type = restrictionTypeService.addIfNotExist(restrictionType);
					
//					if (containsCardinality(predicate)) {
						
//						List<Class> classes = new ArrayList<Class>();
//						classes = findClassesForRestriction(objectResource.getURI(), classes);
//						// AK: should handle datatypes
//						value = objectResource.asLiteral().getString();
						
						value = objectResource.toString();
								
//					}
				}
				else if (predicate.equals("onProperty")){
					
					onProperty = collectProperty(objectResource.toString(), ObjectPropertyType);
				}
				else if (predicate.equals("onClass")){
					
					onClass = collectClass(objectResource.toString(), http);
				}				
				else {
					logger.warn("RetrieveSchemaService.findRestrictionByNode : Cannot find the predicate of restriction");
				}
							
			}//for
			
			return( new Restriction(node, onProperty, type, value, onClass, version));
		}
		catch (JenaException jex) {
			System.out.println(queryStringTriple);
			System.out.println(value);
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
	
	public String[] extractItems(ArrayList<String> row) {
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
		return new String[] {subject, predicate, object};
	}
	
	public boolean retrieveAllExpressions() throws SQLException, IOException, InterruptedException {		
					
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
			String[] vars = new String[] {"s","p","o"};
			HashMap<String, Resource> map = extractItemResources(row, vars);
			
			Resource subjectResource = map.get("s");
			Resource predicateResource = map.get("p");
			Resource objectResource = map.get("o");
		
			String[] result = getExpressionType(subjectResource.toString());
			
			if (retrieveEachExpression(objectResource.toString(),result) == null)
				return false;
			
		}				
		return true;
		
	}
	
	public String[] getExpressionType(String strNode) throws IOException {
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFrom  = "SELECT ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?p ?o FROM " + graphName;
		queryStringTriple += selectFrom + " WHERE {<" + strNode + "> ?p ?o. }";
					
		String type = "", property ="";
		
		HTTPConnection http = new HTTPConnection(endpointURL, queryStringTriple);
		ArrayList<ArrayList<String>> list = parseJson(http.execute());
		
		for (ArrayList<String> row : list) {
			String[] vars = new String[] {"p", "o"};
			HashMap<String, Resource> items = extractItemResources(row, vars);
						
			Resource predicateResource = items.get("p");
			Resource objectResource = items.get("o");
			if (predicateResource.getLocalName().equals("type")) {
				property = objectResource.getLocalName();
			}
			else {
				type = predicateResource.getLocalName();
			}
		}
		
		return new String[] {type, property};
		
	}
	
	public Expression retrieveEachExpression(String strNode, String[] result ) throws IOException, SQLException {
		ExpressionService expressionService = new ExpressionService(connection);
		
		List<Class> classes = new ArrayList<Class>();
		
		if (strNode.isEmpty())
			return null;
		
		Expression myExpression = expressionService.getByURI(strNode);
		if (myExpression != null)
			return myExpression;
		
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFrom  = "SELECT ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?p ?o FROM " + graphName;
		queryStringTriple += selectFrom + " WHERE {<" + strNode + "> ?p ?o. }";
		
		HTTPConnection http = new HTTPConnection(endpointURL, queryStringTriple);
		ArrayList<ArrayList<String>> list;
		
		try {
			list = parseJson(http.execute());
			
			for (ArrayList<String> row : list) {
				String[] vars = new String[] {"p", "o"};
				HashMap<String, Resource> items = extractItemResources(row, vars);
				
				Resource predicateResource = items.get("p");
				Resource objectResource = items.get("o");
				
//				System.out.println(strNode + " - " + objectResource);
				
				if (!(predicateResource.getLocalName().equals("rest") && objectResource.getLocalName().equals("nill") )) {
					if (result[1].equals("Class"))
						classes = findClasses(objectResource.toString(), classes);
					else
						classes= null;
						
				}
				
				// type : intersection, oneof, union ,...
				// property: class, datatype
				
			}
			
			myExpression = new Expression(strNode, result[0],new Property(), result[1], classes, version);
			expressionService.add(myExpression);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
					
		return myExpression;
	}
		
	private List<Class> findClasses(String strNode, List<Class> classes) throws JenaException, SQLException, IOException{
	
		
		//System.out.println(strNode);
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFrom  = "SELECT ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?p ?o FROM " + graphName;
		queryStringTriple += selectFrom + " WHERE {<" + strNode + "> ?p ?o. }";
		
		if (test )
			queryStringTriple += " ORDER BY ?p ?o LIMIT 100";

//		System.out.println(queryStringTriple);
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
//			System.out.println("p: " + predicate + ", o:" + object);
			String predicateLocalName = getLocalName(predicate);
			if (predicateLocalName.equals("first")) {
				
				
				Class myClass = collectClass(object, http);
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
			
//			System.out.println("array: " + array.toString());
			
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
//		     System.out.println("Error json.");
			err.printStackTrace();
		}
		
		return null;
	}
	
	public boolean parseJsonBoolean(String strJSON) {
		try {
						
			JSONObject jsonObject = new JSONObject(strJSON);
			
			String result = jsonObject.get("boolean").toString();
			
			return result.equals("true");
					     
		}catch (JSONException err){
		     System.out.println("Error json.");
		}
		
		return false;
	}
	
	private String getAppropriateLocalName(String uri) {
		if (uri.contains("nodeID"))
			return getLocalNamefromBlankNode(uri);
		else
			return getLocalName(uri);
	}
	
	private String getLocalNamefromBlankNode(String uri) {
		return uri.substring(uri.indexOf("/")+2 , uri.length());
	}
	
	private String getLocalName(String uri) {
		return uri.substring(uri.indexOf("#")+1 , uri.length());
	}
	
	private List<Class> findClassesForRestriction(String strNode, List<Class> classes) throws JenaException, SQLException, IOException{
		
//		System.out.println(strNode);
		// check whether is a class or blank node
		String queryCheckClass = "PREFIX owl: <http://www.w3.org/2002/07/owl#> ASK {<" + strNode + "> a owl:Class.}";
		HTTPConnection http = new HTTPConnection(endpointURL, queryCheckClass);
		boolean result = parseJsonBoolean(http.execute());
		if ( result) {
			Class myClass = collectClass(strNode, http);
			classes.add(myClass);
			return classes;
		
		}
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFrom  = "SELECT ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?p ?o FROM " + graphName;
		queryStringTriple += selectFrom + " WHERE {<" + strNode + "> ?p ?o. }";
		
		if (test )
			queryStringTriple += " ORDER BY ?p ?o LIMIT 100";

		http = new HTTPConnection(endpointURL, queryStringTriple);
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
				
				
				Class myClass = collectClass(object, http);
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
	
	public HashMap<String, Resource> extractItemResources(ArrayList<String> row, String[] vars) {
		HashMap<String, Resource> map = new HashMap<String, Resource>();
		for(String item : row) {
			
			for (String var : vars) {
				int index = item.indexOf(":");
				if (item.substring(0,index).equals(var)) {
					String value = item.substring(index+1, item.length());
//					if (value.matches("[0-9]+")) {
//						
//					}
					
					Resource res = ResourceFactory.createResource(value);
					map.put(var, res);
					
				}
			}
		}

		return map;					
	}
	
	public String makeQuery(String proj, String where) {
		String queryCHk= "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";
		String selectFromCHK  = "SELECT " + proj;
		
		if (!graphName.isBlank())
			selectFromCHK = "SELECT " + proj + " FROM " + graphName;
		queryCHk += selectFromCHK + " WHERE {" + where + " }";
//		queryCHk += selectFromCHK + " WHERE {?s ?p <" + subject + "> }";
		
		if (test )
			queryCHk += " ORDER BY ?s LIMIT 20";
		
		return queryCHk;
	}
	
	
}
