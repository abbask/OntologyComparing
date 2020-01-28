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
	    	
	    	
	    	getClasses(endpoint, graphName, version_id);
	    		    		    
	    }).fail(function (data){
	    	$('.progressDiv').html('<p>Endpoint check: <span class="glyphicon glyphicon-remove"></span></p>');
	    }).responseText;
	}
	
	function getClasses(endpoint, graphname, version_id){
		return $.ajax({
		    url: 'rest/RetrieveSchema/classes',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname, 'version_id': version_id},
		    async: true
		}).done(function (data) {
			$('.progressDiv').append('<p>Classes: <span class="glyphicon glyphicon-ok"></span></p>');	
			getObjectProperties(endpoint, graphName, version_id);
	    	
	    	
		}).fail(function (data) {
			$('.progressDiv').append('<p>Classes: <span class="glyphicon glyphicon-remove"></span></p>');
		}).responseText;
	}	
	
	function getObjectProperties(endpoint, graphname, version_id){
		return $.ajax({
		    url: 'rest/RetrieveSchema/ObjectProperties',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname, 'version_id': version_id},
		    async: true
		}).done(function (data) {
			$('.progressDiv').append('<p>Object Properties: <span class="glyphicon glyphicon-ok"></span></p>');		
			getDataTypeProperties(endpoint, graphName, version_id);
		}).fail(function (data) {
			$('.progressDiv').append('<p>Object Properties: <span class="glyphicon glyphicon-remove"></span></p>');
		}).responseText;
	}
	
	function getDataTypeProperties(endpoint, graphname, version_id){
		return $.ajax({
		    url: 'rest/RetrieveSchema/DataTypeProperties',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname, 'version_id': version_id},
		    async: true
		}).done(function (data) {
			$('.progressDiv').append('<p>Data Type Properties: <span class="glyphicon glyphicon-ok"></span></p>');	
			getTheRest(endpoint, graphname, version_id);
		}).fail(function (data) {
			$('.progressDiv').append('<p>Data Type Properties: <span class="glyphicon glyphicon-remove"></span></p>');
		}).responseText;
	}
	
	function getTheRest(endpoint, graphname, version_id){
		getObjectTripleTypes(endpoint, graphname, version_id);
		getDataTypeTripleTypes(endpoint, graphname, version_id);
		getRestrictions(endpoint, graphname, version_id);
		getExpressions(endpoint, graphname, version_id);
	}
	
	function getObjectTripleTypes(endpoint, graphname, version_id){
		return $.ajax({
		    url: 'rest/RetrieveSchema/ObjectTripleTypes',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname, 'version_id': version_id},
		    async: true
		}).done(function (data) {
			$('.progressDiv').append('<p>Object Triple Types: <span class="glyphicon glyphicon-ok"></span></p>');
		}).fail(function (data) {
			$('.progressDiv').append('<p>Object Triple Types: <span class="glyphicon glyphicon-remove"></span></p>');
		}).responseText;
	}
	
	function getDataTypeTripleTypes(endpoint, graphname, version_id){
		return $.ajax({
		    url: 'rest/RetrieveSchema/DataTypeTripleTypes',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname, 'version_id': version_id},
		    async: true
		}).done(function (data) {
			$('.progressDiv').append('<p>Data Type Triple Types: <span class="glyphicon glyphicon-ok"></span></p>');
		}).fail(function (data) {
			$('.progressDiv').append('<p>Data Type Triple Types: <span class="glyphicon glyphicon-remove"></span></p>');
		}).responseText;
	}
	
	function getRestrictions(endpoint, graphname, version_id){
		return $.ajax({
		    url: 'rest/RetrieveSchema/Restriction',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname, 'version_id': version_id},
		    async: true
		}).done(function (data) {
			$('.progressDiv').append('<p>Restrictions: <span class="glyphicon glyphicon-ok"></span></p>');
		}).fail(function (data) {
			$('.progressDiv').append('<p>Restrictions: <span class="glyphicon glyphicon-remove"></span></p>');
		}).responseText;
	}
	
	function getExpressions(endpoint, graphname, version_id){
		return $.ajax({
		    url: 'rest/RetrieveSchema/Expression',
		    type: 'GET',
		    data: {'endpoint': endpoint, 'graphName': graphname, 'version_id': version_id},
		    async: true
		}).done(function (data) {
			$('.progressDiv').append('<p>Expressions: <span class="glyphicon glyphicon-ok"></span></p>');
		}).fail(function (data) {
			$('.progressDiv').append('<p>Expressions: <span class="glyphicon glyphicon-remove"></span></p>');
		}).responseText;
	}
	
	// Temp
//	$('#endpoint').val('http://gumbo.cs.uga.edu:8890/sparql');
	$('#endpoint').val('http://128.192.62.253:8890/sparql');
	$('#graphName').val('<http://prokino.uga.edu>');
	
	$( "#Retrieve" ).click(function() {
		event.preventDefault();
		endpoint = $('#endpoint').val();
		graphName = $('#graphName').val();
		version_id = $('#version_id').val();		
		
		if (endpoint != ""){
			
			getRemote(endpoint, graphName);
		}// if 
		
	});//Retrieve click
	
	
	
	
});
	

	