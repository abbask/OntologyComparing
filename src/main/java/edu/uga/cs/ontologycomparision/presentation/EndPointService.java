package edu.uga.cs.ontologycomparision.presentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("hello")
public class EndPointService {
	
	@GET 
	@Produces(MediaType.TEXT_PLAIN)
	public String getWins() {return "Hello, world!";}

}
