package edu.uga.cs.ontologycomparision.service;



import java.sql.SQLException;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.Property;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.data.DataStoreConnection;

public class RetrieveSchemaService {
	
	final static Logger logger = Logger.getLogger(RetrieveSchemaService.class);
	
	public boolean checkEndPoint(String endpointURL, String graphName) {
		
		DataStoreConnection dataStoreConn = new DataStoreConnection(endpointURL, graphName);
		dataStoreConn.executeASK("ask { ?x ?c ?e }");
		
		return true;
		
	}

	public boolean retrieveAllClasses(String endpointURL, String graphName, int versionId) throws SQLException {
		VersionService versionService = new VersionService();
		Version version = versionService.get(versionId);
		
		
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect("PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT ?s FROM " + graphName + " WHERE{ ?s a owl:Class.  }");

		for(QuerySolution soln : list) {
			DataStoreConnection dataStoreConn = new DataStoreConnection(endpointURL, graphName);
			
			RDFNode subject = soln.get("s");
			Resource res = soln.getResource("s");
			String classLabel = res.getLocalName();
			String classURL = res.getURI();
			
			if (res.getLocalName() != null) {
				ClassService classService = new ClassService();
				//retrieve individual count
				String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT (count(?ind) as ?Count) FROM " + graphName + " WHERE{ ?ind a <" + subject + "> .}";
				List<QuerySolution> individuals =  dataStoreConn.executeSelect(queryString);
				int count = individuals.get(0).get("Count").asLiteral().getInt();
				
				//retrieve parent class
				queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + classURL + "> rdfs:subClassOf ?parent .}";
				List<QuerySolution> parents =  dataStoreConn.executeSelect(queryString);
				Class parentClass = null;
				if (parents.size() > 0) {
					Resource parentResource = parents.get(0).getResource("parent");					
					
					
					queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT (count(?ind) as ?Count) FROM " + graphName + " WHERE{ ?ind a <" + parentResource.getURI() + "> .}";
					List<QuerySolution> parent =  dataStoreConn.executeSelect(queryString);
					int parentCount = parent.get(0).get("Count").asLiteral().getInt();
					
					parentClass = new Class(parentResource.getURI(), parentResource.getLocalName(), "", parentCount, version, null);					
					parentClass = classService.addIfNotExist(parentClass);
				}
				
				Class myClass = new Class();
				myClass.setLabel(classLabel);
				myClass.setCount(count);
				myClass.setUrl(classURL);
				myClass.setParent(parentClass);
//				myClass.setComment(comment);
				myClass.setVersion(version);
				
				myClass = classService.addIfNotExist(myClass);
			}
			
		}
				
		return true;
		
	}
	
	public boolean retrieveAllObjectProperties(String endpointURL, String graphName, int versionId) throws SQLException {
		VersionService versionService = new VersionService();
		Version version = versionService.get(versionId);
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		
		
		
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

		List<QuerySolution> list = conn.executeSelect(queryStringTriple);
		
		for(QuerySolution soln : list) {
			DataStoreConnection dataStoreConn = new DataStoreConnection(endpointURL, graphName);
			
			RDFNode predicate = soln.get("predicate");
			Resource res = soln.getResource("predicate");

			if (res.getLocalName() != null) {
				//count instance of object property
				Resource domainResource = soln.getResource("domain");
				Resource rangeResource = soln.getResource("range");
				Literal count = soln.getLiteral("count");
				
				//retrieve parent class
				String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + predicate + "> rdfs:subPropertyOf ?parent .}";
				List<QuerySolution> parents =  dataStoreConn.executeSelect(queryString);
				Property parentProperty = null;
				PropertyService propertyService = new PropertyService();
				
				if (parents.size() > 0) {
					Resource parentResource = parents.get(0).getResource("parent");					
					String parentURI = parentResource.getURI();
					
					String queryStringTripleParent = "PREFIX prokino: <http://om.cs.uga.edu/prokino/2.0/#>"
							+ "PREFIX rdf:    <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "PREFIX owl:    <http://www.w3.org/2002/07/owl#> "
							+ "PREFIX rdfs:   <http://www.w3.org/2000/01/rdf-schema#>";
					
					queryStringTripleParent += "SELECT DISTINCT ?domain ?range (COUNT(?object) as ?count) "
							+ "FROM " + graphName + "  "
									+ "WHERE { ?subject " + parentURI + " ?object "
									+ "optional { " + parentURI + " rdfs:domain ?o. ?o owl:unionOf ?l.  "
									+ "{?l rdf:first ?domain. } UNION {?l rdf:rest ?rest. ?rest rdf:first ?domain}} "
									+ "optional {" + parentURI + " rdfs:domain ?domain} "
									+ "optional {" + parentURI + " rdfs:range ?range. ?range rdf:type owl:Class} ";
					
//					
					List<QuerySolution> parent =  dataStoreConn.executeSelect(queryStringTripleParent);
					int parentCount = parent.get(0).get("Count").asLiteral().getInt();
					
					
					parentProperty = new Property(parentResource.getURI(), parentResource.getLocalName(),"", parentCount, version, null);
									
					parentProperty = propertyService.addIfNotExist(parentProperty);
				}
				
				Property myProperty = new Property(res.getURI(), res.getLocalName(), "", count.getLong(), version, parentProperty);
				myProperty = propertyService.addIfNotExist(myProperty);				
				
			}
			
		}
		
		return true;
	}
	
	public boolean retrieveAllDataTypeProperties(String endpointURL, String graphName, int versionId) throws SQLException {
		VersionService versionService = new VersionService();
		Version version = versionService.get(versionId);
		
		DataStoreConnection conn = new DataStoreConnection(endpointURL, graphName);
		List<QuerySolution> list = conn.executeSelect("PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT ?s FROM " + graphName + " WHERE{ ?s a owl:DatatypeProperty.  }");

		for(QuerySolution soln : list) {
			DataStoreConnection dataStoreConn = new DataStoreConnection(endpointURL, graphName);
			
			RDFNode predicate = soln.get("s");
			Resource res = soln.getResource("s");

			if (res.getLocalName() != null) {
				//count instance of object property
				String queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT (count(?s) as ?Count) FROM " + graphName + " WHERE{ ?s <" + predicate + "> ?o .}";
				List<QuerySolution> individuals =  dataStoreConn.executeSelect(queryString);
				int count = individuals.get(0).get("Count").asLiteral().getInt();
				
				//retrieve parent class
				queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> SELECT ?parent FROM " + graphName + " WHERE{ <" + predicate + "> rdfs:subPropertyOf ?parent .}";
				List<QuerySolution> parents =  dataStoreConn.executeSelect(queryString);
				Property parentProperty = null;
				PropertyService propertyService = new PropertyService();
				
				if (parents.size() > 0) {
					Resource parentResource = parents.get(0).getResource("parent");					
					
					
					queryString = "PREFIX owl: <http://www.w3.org/2002/07/owl#> SELECT (count(?ind) as ?Count) FROM " + graphName + " WHERE{ ?s <" + parentResource.getURI() + "> ?o .}";
					List<QuerySolution> parent =  dataStoreConn.executeSelect(queryString);
					int parentCount = parent.get(0).get("Count").asLiteral().getInt();
					
					
					parentProperty = new Property(parentResource.getURI(), parentResource.getLocalName(),"", parentCount, version, null);
									
					parentProperty = propertyService.addIfNotExist(parentProperty);
				}
				
				Property myProperty = new Property(res.getURI(), res.getLocalName(), "", count, version, parentProperty);
				myProperty = propertyService.addIfNotExist(myProperty);				
				
			}
			
		}
		
		return true;
	}
	
}
