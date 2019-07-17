/**
 * 
 */
$(document).ready(function(){
	
	function getRemote(endpoint, graphname) {
	    return $.ajax({
	        type: "GET",
	        data: {'endpoint': endpoint, 'graphName': graphname},
	        url: "EndPointStatus",
	        async: true
	    }).done(function (data){
	    	$('.progressDiv').html('<p>Endpoint check: <span class="glyphicon glyphicon-ok"></span></p>');
	    	getClasses(endpoint, graphName, version_id)
	    }).fail(function (data){
	    	$('.progressDiv').html('<p>Endpoint check: <span class="glyphicon glyphicon-remove"></span></p>');
	    }).responseText;
	}
	
	function getClasses(endpoint, graphname, versionId){
		return $.ajax({
		    url: 'rest/RetrieveSchema/classes',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname, 'version_id': version_id},
		    async: true
		}).done(function (data) {
			$('.progressDiv').append('<p>Class: <span class="glyphicon glyphicon-ok"></span></p>');
		}).fail(function (data) {
			$('.progressDiv').append('<p>Class: <span class="glyphicon glyphicon-remove"></span></p>');
		}).responseText;
	}
	
	// Temp
	$('#endpoint').val('http://gumbo.cs.uga.edu:8890/sparql');
	$('#graphName').val('<http://prokino.uga.edu>');
	
	$( "#Retrieve" ).click(function() {
		event.preventDefault();
		endpoint = $('#endpoint').val();
		graphName = $('#graphName').val();
		version_id = $('#version_id').val();		
		
		if (endpoint != "" && graphName != ""){
			
			getRemote(endpoint, graphName);
		}// if 
		
	});//Retrieve click
	
	
	
	
});
	

	