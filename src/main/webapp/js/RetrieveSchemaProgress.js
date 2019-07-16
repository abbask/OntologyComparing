/**
 * 
 */
$(document).ready(function(){
	
	function getRemote(endpoint, graphname) {
	    return $.ajax({
	        type: "GET",
	        data: {'endpoint': endpoint, 'graphName': graphname},
	        url: "EndPointStatus",
	        async: false
	    }).responseText;
	}
	
	function getClasses(endpoint, graphname){
		return $.ajax({
		    url: 'rest/RetrieveSchema/classes',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname},
		}).done(function (e) {
			console.log("Done");
		}).fail(function (e) {
			console.log("failed");
		});
	}
	
	// Temp
	$('#endpoint').val('http://gumbo.cs.uga.edu:8890/sparql');
	$('#graphName').val('<http://prokino.uga.edu>');
	
	$( "#Retrieve" ).click(function() {
		event.preventDefault();
		endpoint = $('#endpoint').val();
		graphName = $('#graphName').val();
		
		if (endpoint != "" && graphName != ""){

			
			if (getRemote(endpoint, graphName) == 'True'){
				$('.progressDiv').html('<p>Endpoint check: <span class="glyphicon glyphicon-ok"></span></p>');
			}
			else{
				$('.progressDiv').html('<p>Endpoint check: <span class="glyphicon glyphicon-remove"></span></p>');
			}
				
			
			// retieve classes
			getClasses(endpoint, graphName);
			
			//retrieve prop
		}// if 
		
	});//Retrieve click
	
	
	
	
});
	

	