package edu.uga.cs.ontologycomparision.presentation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.CompareService;
import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;
import edu.uga.cs.ontologycomparision.service.VersionService;

@Path("/compare")
public class CompareREST {
	
	@GET
	@Path("/process")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveDataTypeTripleTypes(@QueryParam("endpoint") String endpointURL, @QueryParam("graphName") String graphName, @QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
			
			
			
			return Response.status(500).entity("failed").build();
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}

}
