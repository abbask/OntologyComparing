package edu.uga.cs.ontologycomparision.service;


import org.apache.log4j.Logger;
import edu.uga.cs.ontologycomparision.data.DataStoreConnection;

public class RetrieveSchemaService {
	
	final static Logger logger = Logger.getLogger(RetrieveSchemaService.class);
	
	public boolean checkEndPoint(String endpointURL, String graphName) {
		
		DataStoreConnection dataStoreConn = new DataStoreConnection(endpointURL, graphName);
		dataStoreConn.executeASK("ask { ?x ?c ?e }");
		
		return true;
		
	}

}
