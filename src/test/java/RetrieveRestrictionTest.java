import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

class RetrieveRestrictionTest {

	@Test
	void test() throws SQLException {
		String 	endpointURL 	= "http://lod.openlinksw.com/sparql/";
		String 	graphName 		= "";
		int 	versionId 		= 1;
		
		RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
		service.retrieveAllRestrictions();
		

	}

}
