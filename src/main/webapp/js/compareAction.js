/**
 * 
 */
$(document).ready(function(){
	$( "#compare" ).click(function() {
		event.preventDefault();
		version1_id = $('#version1').val();
		version1_text = $('#version1 :selected').text();

		version2_id = $('#version2').val();
		version2_text = $('#version2 :selected').text();
		
		console.log('version 1: ' + version1_text + ' know by id=' + version1_id);
		console.log('version 2: ' + version2_text + ' know by id=' + version2_id);
		
	});//compare click
});
