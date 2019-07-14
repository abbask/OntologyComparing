package edu.uga.cs.ontologycomparision.presentation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uga.cs.ontologycomparision.model.EndPoint;
import edu.uga.cs.ontologycomparision.service.EndPointService;
import edu.uga.cs.ontologycomparision.service.RetrieveSchemaService;

@Path("/RetrieveSchema")
public class RetrieveSchemaREST {
	
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
	public Response retrieveClasses() {
		
		EndPointService service = new EndPointService();
		List<EndPoint> list;
		try {
			list =  service.getListAll();
		}
		catch(Exception ex) {
			String error = ex.getMessage();
			return Response.status(Response.Status.NO_CONTENT).entity(error).build();
		}
		return Response.status(200).entity(list.toString()).build();

	}

}
