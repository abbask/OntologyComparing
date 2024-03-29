import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.CompareService;
import edu.uga.cs.ontologycomparision.service.VersionService;

class CompareObjectProperty {

	@Test
	void test() throws SQLException {
		MySQLConnection mySQLConnection = new MySQLConnection();
		VersionService service = new VersionService(mySQLConnection.openConnection());
		Version ver1 = service.get(1);
		Version ver2 = service.get(2);
		CompareService compare = new CompareService(ver1, ver2);
		System.out.println(compare.compareObjectProperties().size());
		System.out.println(compare.compareObjectProperties());
	}

}
