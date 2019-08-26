import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

class RetrieveExpressionTest {

	@Test
	void test() throws SQLException, IOException, InterruptedException {
		String 	endpointURL 	= "http://lod.openlinksw.com/sparql/";//"http://lod.openlinksw.com/sparql/";
//		/http://vulcan.cs.uga.edu:8890/sparql
		String 	graphName 		= "";
		int 	versionId 		= 2;
		
		RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
		service.retrieveAllExpressions();
		
	}

}
