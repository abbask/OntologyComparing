package edu.uga.cs.ontologycomparision.presentation;

import java.util.List;

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
	public Response retrieveClasses(@QueryParam("endpoint") String endpointURL, @QueryParam("graphName") String graphName) {

		System.out.println("[classes]graphName: " + graphName);
		System.out.println("[classes]endpointURL: " + endpointURL);
		for (int i = 0 ; i < 1000000000; i++) {
			
		}
		
		//res.setContentType("text/html; charset=" + template.getEncoding());
		//return Response.status(200).entity("true").build();
		//Response.ok(str , MediaType.APPLICATION_JSON).header(HttpHeaders.CONTENT_LENGTH), str.getBytes("UTF-8").length)).build();

		return Response.ok("true", MediaType.TEXT_HTML).header(HttpHeaders.CONTENT_LENGTH, 4).build();
	}
	

}
