import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.data.HTTPConnection;
import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

class HTTPConnectionTest {

	@Test
	void test() throws IOException, SQLException {
		String endPoint = "http://vulcan.cs.uga.edu:8890/sparql/";
		String sparqlQuery = "select * where {?s ?p ?o. FILTER( ?p IN(owl:unionOf, owl:intersectionOf) ) } LIMIT 100";
		for (int i = 0 ; i < 3 ; i++) {
			HTTPConnection connection = new HTTPConnection(endPoint, sparqlQuery);
			String result = connection.execute();
			RetrieveSchemaService service = new RetrieveSchemaService("", "");
			service.parseJson(result);
		}
		
	}
	
	

}
