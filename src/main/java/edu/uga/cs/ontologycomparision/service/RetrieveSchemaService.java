package edu.uga.cs.ontologycomparision.service;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Restriction;
import edu.uga.cs.ontologycomparision.model.RestrictionType;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.model.XSDType;
import edu.uga.cs.ontologycomparision.data.DataStoreConnection;
import edu.uga.cs.ontologycomparision.data.MySQLConnection;

public class RetrieveSchemaService {
	
	final static boolean test = true; //for retrieve restriction from LOD
	
	final static String ObjectPropertyType = "ObjectProperty";
	final static String DatatypePropertyType = "DatatypeProperty";
	
	final static Logger logger = Logger.getLogger(RetrieveSchemaService.class);
	
	private String endpointURL;
	private String graphName;
	private Version version;
	private ArrayList<Class> classList;
	
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
			System.out.println("query: "+ queryStringTriple + rest);
			List<QuerySolution> list = conn.executeSelect(queryStringTriple + rest);
			
			long value = list.get(0).getLiteral("count").getLong();
			if (value == 0 ) {
				System.out.println("zero result ");
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
		
		System.out.println("Property List 1 :" + propertyList);
		System.out.println("Property List 2 :" + propertyList2);
		
		
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
		
		System.out.println("All records " + list.size());
		int count = 0;
		for(QuerySolution soln : list) {
						
			RDFNode node = soln.get("s");
					
			Restriction restriction = findRestrictionByNode(node, restrictionTypeMap);
			restrictionService.add(restriction);
			
			System.out.println(count++ + " Finished " + node.asResource().getLocalName());
		}
		
		
		return true;
		
	}
	
	public Restriction findRestrictionByNode(RDFNode node, Map<String, Integer> restrictionTypeMap) throws SQLException {
		String selectFrom  = "SELECT ?p ?o";
		
		if (!graphName.isBlank())
			selectFrom = "SELECT ?p ?o FROM " + graphName;
		
		String queryStringTriple = "PREFIX owl: <http://www.w3.org/2002/07/owl#> ";						
		queryStringTriple += selectFrom + " WHERE {<" + node.asResource() + "> ?p ?o}";

		System.out.println("Query: " + queryStringTriple);
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		RestrictionType type = null;
		Property onProperty = null;
		Class onClass = null;
		int cardinalityValue = 0;
		
		PropertyService propertyService = new PropertyService(connection);	
		ClassService classService = new ClassService(connection);
		
		for(QuerySolution soln : list) {
			
			RDFNode predicateNode = soln.get("p");
			RDFNode objectNode = soln.get("o");			
			
			System.out.println("predicate: " + predicateNode);
			String predicate = removeNS(predicateNode.asResource().getLocalName());
			
			
			if (restrictionTypeMap.containsKey(predicate)) {			
				RestrictionTypeService restrictionTypeService = new RestrictionTypeService(connection);
				RestrictionType restrictionType = new RestrictionType(predicate) ;
				type = restrictionTypeService.addIfNotExist(restrictionType);											
			}
			else if (predicate == "onProperty"){
				String object = removeNS(objectNode.asResource().getLocalName());
				onProperty = propertyService.getByLabel(object, version.getID());
				if ( onProperty == null )
					onProperty = propertyService.addIfNotExist(new Property(objectNode.asResource().getURI(), objectNode.asResource().getLocalName(), "", "", version, null));
				
				
			}
			else if (predicate == "onClass"){
				String object = removeNS(objectNode.asResource().getLocalName());
				onClass = classService.getByLabel(object, version.getID());
				if (onClass == null)
					onClass = classService.addIfNotExist(new Class(objectNode.asResource().getURI(), objectNode.asResource().getLocalName(), "", 0));				
					
			}
			else if (containsCardinality(predicate)){
				cardinalityValue = objectNode.asLiteral().getInt();
			}
			else {
				logger.warn("RetrieveSchemaService.findRestrictionByNode : Cannot find the predicate of restriction");
			}
						
		}//for
		
		return( new Restriction(onProperty, type, cardinalityValue, onClass, version));
		
		
	}
	
	private String removeNS(String name) {
		return name.replace("owl:", "");
	}
	
	private boolean containsCardinality(String name) {
		return name.toLowerCase().contains("cardinality");
	}
	
}
