
$(document).ready(function(){

	$( "#examine" ).click(function() {
		
		var newGraphName ="";
		var newEndpoint = "";
		
		var oldGraphName ="";
		var oldEndpoint = "";		
		
		newGraphName = $('input#newGraphName').val();
		newEndpoint = $('input#newEndpoint').val();
		
		oldGraphName = $('input#oldGraphName').val();
		oldEndpoint = $('input#oldEndpoint').val();

		$.ajax({
			type: "POST",
			url: "Comparison",
			data: {
				action: "examine",
				newGraphName: newGraphName,
				newEndpoint: newEndpoint,
				oldGraphName: oldGraphName,
				oldEndpoint: oldEndpoint,
			},			
			success: function( data ) {
				console.log(data);
				$('div#resultDiv').empty();
				
				$('div#resultDiv').append('<table>');
				$('div#resultDiv').append('<tr>');
						
				$('div#resultDiv').append('<th></th>');
				$('div#resultDiv').append('<th>Old</th>');
				$('div#resultDiv').append('<th>New</th>');
				
				$('div#resultDiv').append('</tr>');
				
				$('div#resultDiv').append('<tr>');
				$('div#resultDiv').append('<td>Class Count</td>');
				$('div#resultDiv').append('<td>' + data.classCountCompare.classCount.classCountOld + '</td>');
				$('div#resultDiv').append('<td>' + data.classCountCompare.classCount.classCountNew + '</td>');
				$('div#resultDiv').append('</tr>');
				
				$('div#resultDiv').append('<tr>');
				$('div#resultDiv').append('<td>Object Property</td>');
				$('div#resultDiv').append('<td>' + data.objectPropertiesCountCompare.objectPropertyCount.objectPropertyCountOld + '</td>');
				$('div#resultDiv').append('<td>' + data.objectPropertiesCountCompare.objectPropertyCount.objectPropertyCountNew + '</td>');
				
				$('div#resultDiv').append('</tr>');
				
				$('div#resultDiv').append('<tr>');
				$('div#resultDiv').append('<td>Data Property</td>');
				$('div#resultDiv').append('<td>' + data.dataPropertiesCountCompare.dataPropertyCount.dataPropertyCountOld + '</td>');
				$('div#resultDiv').append('<td>' + data.dataPropertiesCountCompare.dataPropertyCount.dataPropertyCountNew + '</td>');
				$('div#resultDiv').append('</tr>');
				
				$('div#resultDiv').append('<tr>');
				$('div#resultDiv').append('<td>Individuals</td>');
				$('div#resultDiv').append('<td>' + data.individualCountCompare.individualCount.individualCountOld + '</td>');
				$('div#resultDiv').append('<td>' + data.individualCountCompare.individualCount.individualCountNew + '</td>');
				$('div#resultDiv').append('</tr>');
				
				
				$('div#resultDiv').append('</table>');

				
				$('div.classCount').text("Number of Classes: " + data.classCount);
				$('div.dataPropertiesCountCompare').text("Number of Data Properties: " + data.dataPropertiesCountCompare);
				$('div.objectPropertyCount').text("Number of Object Properties: " + data.objectPropertyCount);
				$('div.classInstance').text("Number of Individuals: " + data.classInstance);
				$('div#resultDiv').text(data.dataPropertiesCountCompare.dataPropertyCountNew);

			}
		});
	});

});