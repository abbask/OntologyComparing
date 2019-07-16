package edu.uga.cs.ontologycomparision.service;



import java.sql.SQLException;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import edu.uga.cs.ontologycomparision.model.Class;
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
					parentClass = new Class(parentResource.getURI(), parentResource.getLocalName(), "", 0, version, null);					
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
	
}