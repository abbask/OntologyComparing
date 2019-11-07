import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

class RetrieveDatatypeTripleTypeTest {

	@Test
	void test() throws IOException, SQLException {
		
		String 	endpointURL 	= "http://localhost:8890/sparql";		
		String 	graphName 		= "<http://prokino.uga.edu>";
		int 	versionId 		= 1;
		
		RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
		service.retrieveAllDataTypeTripleTypes();
		
	}

}
