package edu.uga.cs.ontologycomparision.presentation;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.uga.cs.ontologycomparision.model.EndPoint;
import edu.uga.cs.ontologycomparision.service.EndPointService;

@Path("EndPoint")
public class EndPointUI {
	
	@GET 
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEndPoint() {
		
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
//Gson gson = new Gson();
//String[] items = {"test1", "test2", "test3"};		
//String result = gson.toJson(items);					
//return Response.status(200).entity(result).build();
//				
//JSONObject jsonObject = new JSONObject();
//Double fahrenheit = 98.24;
//Double celsius;
//celsius = (fahrenheit - 32) * 5 / 9;
//jsonObject.put("F Value", fahrenheit);
//jsonObject.put("C Value", celsius);
//String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + gson.toJson("test");