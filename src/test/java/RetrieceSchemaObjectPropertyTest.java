import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

class RetrieceSchemaObjectPropertyTest {

	@Test
	void test() throws SQLException {
		String 	endpointURL 	= "http://gumbo.cs.uga.edu:8890/sparql";
		String 	graphName 		= "<http://prokino.uga.edu>";
		int 	versionId 		= 1;
		
		
		RetrieveSchemaService service = new RetrieveSchemaService();
		service.checkDifferenceBetween2Approaches(endpointURL, graphName, versionId);
		
	}

}
