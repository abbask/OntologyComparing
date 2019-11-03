import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

class RetrieveRestrictionTest {

	@Test
	void test() throws SQLException, IOException {
		String 	endpointURL 	= "http://localhost:8890/sparql";
//		String 	endpointURL 	= "http://lod.openlinksw.com/sparql/";
		String 	graphName 		= "<http://obi-ontology.org>";
		int 	versionId 		= 1;
		
		RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
		service.retrieveAllRestrictions();
		

	}

}
