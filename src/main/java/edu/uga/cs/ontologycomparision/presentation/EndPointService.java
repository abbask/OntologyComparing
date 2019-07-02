package edu.uga.cs.ontologycomparision.presentation;

import javax.ws.rs.*;

@ApplicationPath("/")
public class EndPointService {
	
	@GET @Path("/EndPoint/greetings")@Produces("text/plain")
	public String getWins() {return "Hello, world!";}

}
