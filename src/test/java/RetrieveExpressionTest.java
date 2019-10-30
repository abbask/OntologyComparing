import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

class RetrieveExpressionTest {

	@Test
	void test() throws SQLException, IOException, InterruptedException {
		String 	endpointURL 	= "http://128.192.62.253:8890/sparql";//"http://lod.openlinksw.com/sparql/";//"http://lod.openlinksw.com/sparql/";
//		/http://vulcan.cs.uga.edu:8890/sparql
		String 	graphName 		= "<http://obi-ontology.org>";
		int 	versionId 		= 3;
		
		RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
		System.out.println(service.retrieveAllExpressions());
		
	}

}
