import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.CompareService;
import edu.uga.cs.ontologycomparision.service.VersionService;

class CompareCounts {

	@Test
	void test() throws Exception {
		MySQLConnection mySQLConnection = new MySQLConnection();
		VersionService service = new VersionService(mySQLConnection.openConnection());
		Version ver1 = service.get(2);
		Version ver2 = service.get(3);
		CompareService compare = new CompareService(ver1, ver2);
//		System.out.println(compare.compareIndividualCount());
//		
//		System.out.println(compare.compareIndividualCountEachClass());
//		
//		System.out.println(compare.compareObjectTripleTypeCount());
//		System.out.println(compare.compareDatatypeTripleTypeCount());
//		
		System.out.println(compare.compareObjectTripleTypeCountEachTriple());
//		System.out.println(compare.compareDatatypeTripleTypeCountEachTriple());
//		
//		List<Result<ObjectTripleType, Integer>> results = compare.compareObjectTripleTypeCountEachTriple();
//		System.out.println(results.size());
//		for (Result<ObjectTripleType, Integer> r : results) {
//			System.out.println(r);
//		}

		
	}

}
