/**
 * 
 */
$(document).ready(function(){
	
	function getRemote(remote_url) {
	    return $.ajax({
	        type: "GET",
	        url: remote_url,
	        async: false
	    }).responseText;
	}
	
	// Temp
	$('#endpoint').val('http://gumbo.cs.uga.edu:8890/sparql');
	$('#graphName').val('<http://prokino.uga.edu>');
	
	$( "#Retrieve" ).click(function() {
		event.preventDefault();
		endpoint = $('#endpoint').val();
		graphName = $('#graphName').val();
		
		if (endpoint != "" && graphName != ""){
			// ASK query to check endpoint
			
			var encodedEndpoint = encodeURIComponent(endpoint);
			var encodedGraphName = encodeURIComponent(graphName);
			
			url = "http://localhost:8080/OntologyComparing/rest/RetrieveSchema/check/endpoint/" + encodedEndpoint + "/graph/" + encodedGraphName + "/";

			console.log("url: " + url);
			
			if (getRemote(url) == true){
				$('.progressDiv').html('<p>Endpoint check: <span class="glyphicon glyphicon-ok"></span></p>');
			}
			else{
				$('.progressDiv').html('<p>Endpoint check: <span class="glyphicon glyphicon-remove"></span></p>');
			}
				
			
			// retieve classes
			
			
			//retrieve prop
		}// if 
		
	});//form submit
});
	

	