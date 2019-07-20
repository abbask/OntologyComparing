import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.XSDType;
import edu.uga.cs.ontologycomparision.service.XSDTypeService;


public class TestXSDType {
	@Test
	public void testAddAndGetByType() throws SQLException {
		
		MySQLConnection mySQLConnection = new MySQLConnection();
		XSDTypeService typeService = new XSDTypeService(mySQLConnection.openConnection());
		XSDType type = new XSDType();
		
		type.setType("test");
		type.setUrl("http://test.com/");
		
        typeService.add(type);
        
        int id = type.getID();
        Assert.assertNotNull(id);
        
        XSDType newType = typeService.getByType("test");
        Assert.assertEquals("test", newType.getType());
        Assert.assertEquals("http://test.com/",newType.getUrl());
        
        return;
		
	}
	
}
