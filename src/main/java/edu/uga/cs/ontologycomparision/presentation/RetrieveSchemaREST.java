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

	
	@GET
	@Path("{id}")
	public Response getUserById(@PathParam("id") String id) {

	   return Response.status(200).entity("getUserById is called, id : " + id).build();

	}
	
	
	
	@GET
	@Path("/classes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveClasses(@QueryParam("endpoint") String endpointURL, @QueryParam("graphName") String graphName, @QueryParam("version_id") int versionId) {
		
		try {
			RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
			
			if (service.retrieveAllClasses()) {
				return Response.ok("done", MediaType.TEXT_HTML).header(HttpHeaders.CONTENT_LENGTH, 4).build();
			}
			return Response.status(500).entity("failed").build();
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/ObjectProperties")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveObjectProperties(@QueryParam("endpoint") String endpointURL, @QueryParam("graphName") String graphName, @QueryParam("version_id") int versionId) {
			
		try {
			RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
			
			if (service.retrieveAllObjectProperties()) {
				return Response.ok("done", MediaType.TEXT_HTML).header(HttpHeaders.CONTENT_LENGTH, 4).build();
			}
			return Response.status(500).entity("failed").build();
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/DataTypeProperties")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveDataTypeProperties(@QueryParam("endpoint") String endpointURL, @QueryParam("graphName") String graphName, @QueryParam("version_id") int versionId) {
				
		try {
			RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
			
			if (service.retrieveAllDataTypeProperties()) {
				return Response.ok("done", MediaType.TEXT_HTML).header(HttpHeaders.CONTENT_LENGTH, 4).build();
			}
			return Response.status(500).entity("failed").build();
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/ObjectTripleTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response retrieveObjectTripleTypes(@QueryParam("endpoint") String endpointURL, @QueryParam("graphName") String graphName, @QueryParam("version_id") int versionId) {
			
		try {
			RetrieveSchemaService service = new RetrieveSchemaService(endpointURL, graphName, versionId);
			
			if (service.retrieveAllObjectTypeTriples()) {
				return Response.ok("done", MediaType.TEXT_HTML).header(HttpHeaders.CONTENT_LENGTH, 4).build();
			}
			return Response.status(500).entity("failed").build();
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
}
