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
			
			$('#classD').html(tableHeader + tableContent + tableFooter);
			console.log($('#classD'));
			console.log(tableHeader + tableContent + tableFooter);
			$('#myTab li:second-child a').tab('show');
			
		}).fail(function (data) {
			
		}).responseText;
	}
	
	$( "#compare" ).click(function() {
		event.preventDefault();
		version1_id = $('#version1').val();
		version1_text = $('#version1 :selected').text();

		version2_id = $('#version2').val();
		version2_text = $('#version2 :selected').text();

		if (version1_id != "" && version2_id != ""){
			
			getGeneralCounts(version1_id, version2_id);
			getClasses(version1_id, version2_id);
			
			
		}// if 

		
	});//compare click
});
