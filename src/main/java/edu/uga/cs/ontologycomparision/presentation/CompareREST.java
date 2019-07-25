package edu.uga.cs.ontologycomparision.presentation;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Result;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.CompareService;
import edu.uga.cs.ontologycomparision.service.VersionService;

@Path("/compare")
public class CompareREST {
	
	@GET
	@Path("/counts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareGeneralCounts(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
			List<Result<String, Long>> classCountList          = compareService.compareClassCount();			
			List<Result<String, Long>> objectPropertCount      = compareService.compareObjectPropertyCount();
			List<Result<String, Long>> datatypePropertyCount   = compareService.compareDatatypePropertyCount();
			
			List<Result<String, Long>> individualCount         = compareService.compareIndividualCount();
			List<Result<String, Long>> objectTripleTypeCount   = compareService.compareObjectTripleTypeCount();
			List<Result<String, Long>> datatypeTripleTypeCount = compareService.compareDatatypeTripleTypeCount();
			
			root.put("classCountList", classCountList);
			root.put("objectPropertCount", objectPropertCount);
			root.put("datatypePropertyCount", datatypePropertyCount);
			root.put("individualCount", individualCount);
			root.put("objectTripleTypeCount", objectTripleTypeCount);
			root.put("datatypeTripleTypeCount", datatypeTripleTypeCount);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/classes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareClasses(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
			
			List<Result<String, String>> classes 		  = compareService.compareClasses();
//			List<Result<String, String>> objectProperties = compareService.compareObjectProperties();
//			List<Result<String, String>> datatypeProperties = compareService.compareDatetypeProperties();

			
			root.put("classes", classes);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/objectproperties")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareObjectProperties(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
						
			List<Result<String, String>> objectProperties = compareService.compareObjectProperties();
					
			root.put("objectProperties", objectProperties);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/datatypeproperties")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareDatatypeProperties(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
						
			List<Result<String, String>> datatypeProperties = compareService.compareDatetypeProperties();
			
			root.put("datatypeProperties", datatypeProperties);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}

}
