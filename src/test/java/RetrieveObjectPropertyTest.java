import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

class RetrieveObjectPropertyTest {

	@Test
	void test() throws SQLException, IOException {
		String 	endpointURL 	= "http://128.192.62.253:8890/sparql";//"http://lod.openlinksw.com/sparql/";//"http://lod.openlinksw.com/sparql/";
//		/http://vulcan.cs.uga.edu:8890/sparql
		String 	graphName 		= "<http://prokino.uga.edu>";
		int 	versionId 		= 17;
		
		RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
		System.out.println(service.retrieveAllObjectProperties());
		System.out.println(service.retrieveAllDataTypeProperties());
	}

}