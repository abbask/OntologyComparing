package edu.uga.cs.ontologycomparision.presentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import edu.uga.cs.ontologycomparision.data.MySQLConnection;
import edu.uga.cs.ontologycomparision.model.Class;
import edu.uga.cs.ontologycomparision.model.DataTypeTripleType;
import edu.uga.cs.ontologycomparision.model.ObjectTripleType;
import edu.uga.cs.ontologycomparision.model.Result;
import edu.uga.cs.ontologycomparision.model.Version;
import edu.uga.cs.ontologycomparision.service.CompareService;
import edu.uga.cs.ontologycomparision.service.VersionService;

@Path("/compare")
public class CompareREST {
	final static Logger logger = Logger.getLogger(CompareREST.class);
	
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
			
			List<Result<String, Long>> restrictionCount        = compareService.compareRestrictionCount();
			
			root.put("classCountList", classCountList);
			root.put("objectPropertCount", objectPropertCount);
			root.put("datatypePropertyCount", datatypePropertyCount);
			root.put("individualCount", individualCount);
			root.put("objectTripleTypeCount", objectTripleTypeCount);
			root.put("datatypeTripleTypeCount", datatypeTripleTypeCount);
			root.put("restrictionCount", restrictionCount);

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
	@Path("/IndividualCountEachClass")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareIndividualsOfClasses(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
			
			List<Result<Class, Integer>> individualsOfclasses 		  = compareService.compareIndividualCountEachClass();

			root.put("individualsOfclasses", individualsOfclasses);

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
	
	@GET
	@Path("/objectTripleTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareObjectTriple(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
						
			List<Result<String, String>> objectTriple = compareService.compareObjectTripleTypes();
			
			root.put("objectTriple", objectTriple);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/datatypeTripleTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareDatatypeTriple(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
						
			List<Result<String, String>> datatypeTriple = compareService.compareDatatypeTripleTypes();
			
			root.put("datatypeTriple", datatypeTriple);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/objectTripleTypeCountEachTriple")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareObjectTripleTypeCountEach(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
						
			List<Result<ObjectTripleType, Integer>>  objectTripleforEach = compareService.compareObjectTripleTypeCountEachTriple();
			
			root.put("objectTripleforEach", objectTripleforEach);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/datatypeTripleTypeCountEachTriple")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareDatatypeTripleTypeCountEach(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);
						
			List<Result<DataTypeTripleType, Integer>> datatypeTripleforEach = compareService.compareDatatypeTripleTypeCountEachTriple();
			
			root.put("datatypeTripleforEach", datatypeTripleforEach);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}
	
	@GET
	@Path("/restrictions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareRestriction(@QueryParam("version1") int version1Id, @QueryParam("version2") int version2Id) {
			
		try {
			
			Map<String, Object> root = new HashMap<>();	
			
			MySQLConnection mySQLConnection = new MySQLConnection();
			VersionService versionService = new VersionService(mySQLConnection.openConnection());
			
			Version version1 = versionService.get(version1Id);
			Version version2 = versionService.get(version2Id);
			
			CompareService compareService = new CompareService(version1, version2);

			List<Result<String, String>> restrictions = compareService.compareRestrictions();
			
			
			root.put("restrictions", restrictions);

			Gson gson = new Gson();
			String result = gson.toJson(root);
			return Response.ok(result, MediaType.APPLICATION_JSON).build();
										
		} catch (Exception e) {
			
			e.printStackTrace();
			return Response.status(500).entity("failed").build();
		}
		
	}

}
