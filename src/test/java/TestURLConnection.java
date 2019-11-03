import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.data.HTTPConnection;

class TestURLConnection {

	@Test
	void test() throws IOException {
		String 	endpointURL 	= "http://localhost:8890/sparql";
		String 	graphName 		= "<http://obi-ontology.org>";
		String q = "SELECT Count(*) FROM " + graphName + " WHERE {[] a ?Concept}";
		int i = 3000000;
		HTTPConnection http = new HTTPConnection(endpointURL);
		while(i > 0 ) {
			http.setSparqlQuery(q);
			http.execute();
			System.out.println(i);
			i -= 1;
		}
		
	}
	//598,576‬ then error

}
