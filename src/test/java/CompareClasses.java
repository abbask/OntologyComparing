import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.CompareService;
import edu.uga.cs.ontologycomparision.service.ObjectTripleTypeService;
import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;
import edu.uga.cs.ontologycomparision.service.VersionService;

class CompareClasses {

	@Test
	void test() throws SQLException, IOException {
		MySQLConnection mySQLConnection = new MySQLConnection();
	//		Connection connection = mySQLConnection.openConnection();
		VersionService service = new VersionService(mySQLConnection.openConnection());
		Version ver1 = service.get(37);
		Version ver2 = service.get(38);
		CompareService compare = new CompareService(ver1, ver2);
		System.out.println(compare.compareClasses());
//		System.out.println(compare.compareClassCount());
		
//		ObjectTripleTypeService service = new ObjectTripleTypeService(connection);
//		List<ObjectTripleType> list = service.listAll(5);
//		for (ObjectTripleType obj : list) {
//			System.out.println(obj);
//		}
		
//		String endpointURL = "http://128.192.62.253:8890/sparql";
//		String graphName = "<http://ncicb.nci.nih.gov>";
//		
//		RetrieveSchemaService retrieve = new RetrieveSchemaService(endpointURL, graphName);
//		if ( retrieve.retrieveAllDataTypeProperties() )
//			System.out.println("done.");
//		else
//			System.out.println(" Failed");
//		
		
	}

}
