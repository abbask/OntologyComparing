<!DOCTYPE html>
<html lang="en">
<head>
  <title>Compare Versions</title>
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
      <li ><a href="/OntologyComparing/VersionList">Versions</a></li>      
   	<ul class="nav navbar-nav">
      <li class="active"><a href="/OntologyComparing/compare">Comparison</a></li>   
    </ul>
  </div>
</nav>
  
<div class="container">
	<h3>List of Versions</h3>
	<p>please select versions you want to compare.</p>
	
	<div  style="float: left;">
		<form class="form-inline">
		
		<div class="col-xs-16">
			
			<select class="form-control" id="version1" name="version1">
				<option value="0">-- select --</option>
				<#list versions as version>
					<option value="${version.ID}">${version.name} - ${version.number}</option>
				</#list>			    		
		    </select>
		    <select class="form-control" id="version2" name="version2">
				<option value="0">-- select --</option>
				<#list versions as version>
					<option value="${version.ID}">${version.name} - ${version.number}</option>
				</#list>			    		
		    </select>
			<button id="compare" type="button" class="btn btn-primary">Compare</button>
		</div>
	</form>
	</div>
	
</div>

</body>
</html>