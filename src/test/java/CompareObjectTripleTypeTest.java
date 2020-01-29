import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Result;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.CompareService;
import edu.uga.cs.ontologycomparision.service.VersionService;

class CompareObjectTripleTypeTest {

	@Test
	void test() throws SQLException {
		MySQLConnection mySQLConnection = new MySQLConnection();
		VersionService service = new VersionService(mySQLConnection.openConnection());
		Version ver1 = service.get(37);
		Version ver2 = service.get(38);
//		CompareService compare = new CompareService(ver1, ver2);
//		System.out.println(compare.compareObjectTripleTypes());
		Map<String, Object> root = new HashMap<>();	
		CompareService compareService = new CompareService(ver1, ver2);
		
		List<Result<String, String>> objectTriple = compareService.compareObjectTripleTypes();
		System.out.println(objectTriple.size());
		
		root.put("objectTriple", objectTriple);

		Gson gson = new Gson();
		String result = gson.toJson(root);
		System.out.println(result);
		
	}
}
