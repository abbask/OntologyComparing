<!DOCTYPE html>
<html lang="en">
<head>
  <title>Version - List</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="/OntologyComparing/VersionList">Ontology Comparing</a>
    </div>
    <ul class="nav navbar-nav">
      <li class="active"><a href="/OntologyComparing/VersionList">Versions</a></li>      
      
    </ul>
  </div>
</nav>
  
<div class="container">
	<h3>List of Versions</h3>
	<p>You can see list of versions and load them.</p>
	<div  style="float: left;">
		<a href="VersionNew" class="btn btn-primary btn-md active" role="button" aria-pressed="true">Add new Version</a>
	</div>
	<div>
		<table class="table">
		  <thead>
		    <tr>
		      <th scope="col">Name</th>
		      <th scope="col">Number</th>
		      <th scope="col">Date</th>
		      <th scope="col"></th>
		    </tr>
		  </thead>
		  <tbody>
		  	<#list versions as version>
				<tr>
					<td>
						${version.name}
					</td>
					<td style="text-overflow: ellipsis;">
						${version.number}					
					</td>
					<td >
						${version.date}					
					</td>
					<td>
						<a href="VersionRemove?id=${version.ID}" class="btn btn-danger btn-md active" role="button" aria-pressed="true">Remove</a>
					</td>
					
				</tr>
			<#else>
			  <tr>
			  	<td colspan="4">
			  		No data found
			  	</td>
			  </tr>
			</#list>
			

		  </tbody>
		</table>
	</div>
</div>

</body>
</html>