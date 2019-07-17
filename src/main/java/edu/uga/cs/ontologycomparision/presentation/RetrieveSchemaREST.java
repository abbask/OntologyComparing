package edu.uga.cs.ontologycomparision.presentation;


import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

@Path("/RetrieveSchema")
public class RetrieveSchemaREST {
	
	/* NOT USED */
	@GET
	@Path("/check/endpoint/{endpoint}/graph/{graphName}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkEndPoint(
			@PathParam("endpoint") String endpointURL,
			@PathParam("graphName") String graphName ) {
		boolean result = false;
		RetrieveSchemaService service = new RetrieveSchemaService();

		System.out.println("endpoint: " + endpointURL);
		System.out.println("graphName: " + graphName);
		
		try {
			result =  service.checkEndPoint(endpointURL, graphName);
		}
		catch(Exception ex) {
			String error = ex.getMessage();
			return Response.status(Response.Status.NO_CONTENT).entity(error).build();
		}
		return Response.status(200).entity(result).build();

	}
	
	@GET
	@Path("{id}")
	public Response getUserById(@PathParam("id") String id) {

	   return Response.status(200).entity("getUserById is called, id : " + id).build();

	}
	
	
	
	@GET
	@Path("/classes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveClasses(@QueryParam("endpoint") String endpointURL, @QueryParam("graphName") String graphName, @QueryParam("version_id") int versionId) {
		
		RetrieveSchemaService service = new RetrieveSchemaService();
		
		try {
			System.out.printf("endpoint: %s, graphName: %s, versionId: %s ", endpointURL, graphName, versionId);
			if (service.retrieveAllClasses(endpointURL, graphName, versionId)) {
				return Response.ok("done", MediaType.TEXT_HTML).header(HttpHeaders.CONTENT_LENGTH, 4).build();
			}
			return Response.status(500).entity("failed").build();
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	

}
