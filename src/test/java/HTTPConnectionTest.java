import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.data.HTTPConnection;

class HTTPConnectionTest {

	@Test
	void test() throws IOException {
		String endPoint = "http://vulcan.cs.uga.edu:8890/sparql/";
		String sparqlQuery = "select * where {?s ?p ?o. FILTER( ?p IN(owl:unionOf, owl:intersectionOf) ) } LIMIT 100";
		
		HTTPConnection connection = new HTTPConnection(endPoint, sparqlQuery);
		System.out.println(connection.execute());
	}

}
