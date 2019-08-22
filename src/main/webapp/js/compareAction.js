/**
 * 
 */
$(document).ready(function(){

	function getGeneralCounts(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/counts',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {

			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Version 1</th><th scope="col">Version 2</th><th scope="col">Difference</th></tr></thead><tbody>';
			
			var classCountHtml = '<tr><th scope="row">Number of Classes</th><td>'+data.classCountList[0].result +'</td><td>'+ data.classCountList[1].result + '</td><td>' + (data.classCountList[1].result - data.classCountList[0].result) + '</td></tr>';
			var objectPropertyHtml = '<tr><th scope="row">Number of Object Properties</th><td>'+data.objectPropertCount[0].result +'</td><td>'+ data.objectPropertCount[1].result + '</td><td>' + (data.objectPropertCount[1].result - data.objectPropertCount[0].result) + '</td></tr>';
			var datatypePropertyHtml = '<tr><th scope="row">Number of Datatype Properties</th><td>'+data.datatypePropertyCount[0].result +'</td><td>'+ data.datatypePropertyCount[1].result + '</td><td>' + (data.datatypePropertyCount[1].result - data.datatypePropertyCount[0].result) + '</td></tr>';
			var individualHtml = '<tr><th scope="row">Number of individuals</th><td>'+data.individualCount[0].result +'</td><td>'+ data.individualCount[1].result + '</td><td>' + (data.individualCount[1].result - data.individualCount[0].result) + '</td></tr>';
			var objectTripleTypeHtml = '<tr><th scope="row">Number of Object Triple Types</th><td>'+data.objectTripleTypeCount[0].result +'</td><td>'+ data.objectTripleTypeCount[1].result + '</td><td>' + (data.objectTripleTypeCount[1].result - data.objectTripleTypeCount[0].result) + '</td></tr>';
			var datatypeTripleTypeHtml = '<tr><th scope="row">Number of Datatype Triple Types</th><td>'+data.datatypeTripleTypeCount[0].result +'</td><td>'+ data.datatypeTripleTypeCount[1].result + '</td><td>' + (data.datatypeTripleTypeCount[1].result - data.datatypeTripleTypeCount[0].result) + '</td></tr>';
			
			var tableContent = classCountHtml + objectPropertyHtml + datatypePropertyHtml + individualHtml +objectTripleTypeHtml +  datatypeTripleTypeHtml;
			var tableFooter = '</tbody></table>';
			
			$('#count').html(tableHeader + tableContent + tableFooter);
			$('#myTab li:first-child a').tab('show');
			
		}).fail(function (data) {
			
		}).responseText;
	}
	
	function getClasses(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/classes',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {

			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Version </th></tr></thead><tbody>';
			
//			classHtml = '<tr><th scope="row">' + data.classes[0].result  + '</th><td>'+ data.classes[1].result +'</td></tr>';
			var classHtml = '';
			data.classes.forEach(function(e){
				classHtml += '<tr><td>' + e.element + '</td><td>'+ e.result +'</td></tr>';
			});
			
			
			var tableContent = classHtml;
			var tableFooter = '</tbody></table>';
			
			if (data.classes.length < 1){
				$('#classD').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#classD').html(tableHeader + tableContent + tableFooter);
			}
			
			$('#myTab a[href="#classD"]').tab('show');
			
		}).fail(function (data) {
			
		}).responseText;
	}
	
	function getIndividualsOfClasses(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/IndividualCountEachClass',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {
			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Difference </th></tr></thead><tbody>';
			
			var individualCountEachClassHtml = '';
			data.individualsOfclasses.forEach(function(e){
				individualCountEachClassHtml += '<tr><td>' + e.element.label + '</td><td>'+ e.result +'</td></tr>';
			});
			
			
			var tableContent = individualCountEachClassHtml;
			var tableFooter = '</tbody></table>';
			
			if (data.individualsOfclasses.length < 1){
				$('#IndividualOfClass').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#IndividualOfClass').html(tableHeader + tableContent + tableFooter);
			}
			
//			$('#myTab a[href="#individualsOfclasses"]').tab('show');
			
		}).fail(function (data) {
			
		}).responseText;
	}
	
	function getObjectProperties(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/objectproperties',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {
			
			
			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Version </th></tr></thead><tbody>';
			
			var objectPropHtml = '';
			data.objectProperties.forEach(function(e){
				objectPropHtml += '<tr><td>' + e.element + '</td><td>'+ e.result +'</td></tr>';
			});
			
			var tableContent = objectPropHtml;
			
			var tableFooter = '</tbody></table>';
			
			if (data.objectProperties.length < 1){
				$('#objectProp').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#objectProp').html(tableHeader + tableContent + tableFooter);
			}
			
//			$('#myTab a[href="#objectProp"]').tab('show');
			
		}).fail(function (data) {
			console.log(data);
		}).responseText;
	}
	
	
	function getDatatypeProperties(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/datatypeproperties',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {
			
			
			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Version </th></tr></thead><tbody>';
			
			var datatypePropHtml = '';
			data.datatypeProperties.forEach(function(e){
				datatypePropHtml += '<tr><td>' + e.element + '</td><td>'+ e.result +'</td></tr>';
			});
			
			var tableContent = datatypePropHtml;
			
			var tableFooter = '</tbody></table>';
			
			if (data.datatypeProperties.length < 1){
				$('#datatypeProp').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#datatypeProp').html(tableHeader + tableContent + tableFooter);
			}
			
			
		}).fail(function (data) {
			console.log(data);
		}).responseText;
	}
	
	function getObjectTriple(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/objectTripleTypes',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {
			
			
			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Version </th></tr></thead><tbody>';
			
			var objectTripleHtml = '';
			data.objectTriple.forEach(function(e){
				objectTripleHtml += '<tr><td>' + e.element + '</td><td>'+ e.result +'</td></tr>';
			});
			
			var tableContent = objectTripleHtml;
			
			var tableFooter = '</tbody></table>';
			
			if (data.objectTriple.length < 1){
				$('#objectTriple').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#objectTriple').html(tableHeader + tableContent + tableFooter);
			}
			
//			$('#myTab a[href="#objectProp"]').tab('show');
			
		}).fail(function (data) {
			console.log(data);
		}).responseText;
	}
	
	function getDatatypeTriple(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/datatypeTripleTypes',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {
			
			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Version </th></tr></thead><tbody>';
			
			var datatypeTripleHtml = '';
			data.datatypeTriple.forEach(function(e){
				datatypeTripleHtml += '<tr><td>' + e.element + '</td><td>'+ e.result +'</td></tr>';
			});
			
			var tableContent = datatypeTripleHtml;
			
			var tableFooter = '</tbody></table>';
			
			if (data.datatypeTriple.length < 1){
				$('#datatypeTriple').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#datatypeTriple').html(tableHeader + tableContent + tableFooter);
			}
			
//			$('#myTab a[href="#objectProp"]').tab('show');
			
		}).fail(function (data) {
			console.log(data);
		}).responseText;
	}
	
	function getObjectTripleForEach(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/objectTripleTypeCountEachTriple',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {
			
			
			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Difference </th></tr></thead><tbody>';
			
			var objectTripleforEachHtml = '';
			data.objectTripleforEach.forEach(function(e){
				objectTripleforEachHtml += '<tr><td>(' + e.element.domain.label + ', ' + e.element.predicate.label + ', ' + e.element.range.label +  ')</td><td>'+ e.result +'</td></tr>';
			});
			
			var tableContent = objectTripleforEachHtml;
			
			var tableFooter = '</tbody></table>';
			
			if (data.objectTripleforEach.length < 1){
				$('#objectTripleforEach').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#objectTripleforEach').html(tableHeader + tableContent + tableFooter);
			}
			
//			$('#myTab a[href="#objectProp"]').tab('show');
			
		}).fail(function (data) {
			console.log(data);
		}).responseText;
	}
	
	function getDatatypeTripleForEach(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/datatypeTripleTypeCountEachTriple',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {
			
			
			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Difference </th></tr></thead><tbody>';
			var count = 0;
			var datatypeTripleforEachHtml = '';
			data.datatypeTripleforEach.forEach(function(e){
				if (e.result != 0){
					domainName = (e.element.domain == null ? "" : e.element.domain.label);
					predicateName = (e.element.predicate == null ? "" : e.element.predicate.label);
					rangeType = (e.element.range == null ? "" : e.element.range.type);
					
					datatypeTripleforEachHtml += '<tr><td>(' + domainName + ', ' + predicateName + ', ' + rangeType +  ')</td><td>'+ e.result +'</td></tr>';
					count += 1;
				}
			});
			
			var tableContent = datatypeTripleforEachHtml;
			
			var tableFooter = '</tbody></table>';
			
			if (count< 1){
				$('#datatypeTripleforEach').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#datatypeTripleforEach').html(tableHeader + tableContent + tableFooter);
			}
			
//			$('#myTab a[href="#objectProp"]').tab('show');
			
		}).fail(function (data) {
			console.log(data);
		}).responseText;
	}
	
	function getRestrictions(version1_id, version2_id){
		return $.ajax({
		    url: 'rest/compare/restrictions',
		    type: 'GET',
		    data: { 'version1': version1_id, 'version2': version2_id},
		    async: true
		}).done(function (data) {
			
			var tableHeader = '<table class="table table-striped"><thead><tr><th scope="col"></th><th scope="col">Version </th></tr></thead><tbody>';
			
			var restrictionHtml = '';
			data.restrictions.forEach(function(e){
				restrictionHtml += '<tr><td>' + e.element + '</td><td>'+ e.result +'</td></tr>';
			});
			
			var tableContent = restrictionHtml;
			
			var tableFooter = '</tbody></table>';
			
			if (data.restrictions.length < 1){
				$('#restrictions').html('<table class="table table-striped"><thead><tr><th scope="col">No Difference</th></tr></thead><tbody></tbody></table>');
			}
			else{
				$('#restrictions').html(tableHeader + tableContent + tableFooter);
			}
			
//			$('#myTab a[href="#objectProp"]').tab('show');
			
		}).fail(function (data) {
			console.log(data);
		}).responseText;
	}
	
	function loadSpinner(){
		console.log($('#spinning'));
		$('#count').html('<div class="loader"></div>');	
		$('#classD').html('<div class="loader"></div>');	
		$('#IndividualOfClass').html('<div class="loader"></div>');	
		$('#objectProp').html('<div class="loader"></div>');	
		$('#datatypeProp').html('<div class="loader"></div>');	
		$('#objectTriple').html('<div class="loader"></div>');	
		$('#datatypeTriple').html('<div class="loader"></div>');	
		$('#objectTripleforEach').html('<div class="loader"></div>');	
		
		$('#datatypeTripleforEach').html('<div class="loader"></div>');	
		$('#restrictions').html('<div class="loader"></div>');	
	}
	
	$( "#compare" ).click(function() {
		event.preventDefault();
		version1_id = $('#version1').val();
		version1_text = $('#version1 :selected').text();

		version2_id = $('#version2').val();
		version2_text = $('#version2 :selected').text();

		if (version1_id != "" && version2_id != ""){
			loadSpinner();
			getGeneralCounts(version1_id, version2_id);
			getClasses(version1_id, version2_id);
			getIndividualsOfClasses(version1_id, version2_id);
			getObjectProperties(version1_id, version2_id);
			getDatatypeProperties(version1_id, version2_id);
			getObjectTriple(version1_id, version2_id);
			getDatatypeTriple(version1_id, version2_id);
			getObjectTripleForEach(version1_id, version2_id);
			getDatatypeTripleForEach(version1_id, version2_id);
			getRestrictions(version1_id, version2_id);
		}// if 

		
	});//compare click
});
