<%@ page contentType="text/html;charset=ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Transmart Data Pipeline > Gene Expression</title>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="../../css/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="${resource(dir:'css/bootstrap/css',file:'bootstrap.min.css')}" />


<style type="text/css">
body {
	padding-top: 40px;
	padding-bottom: 40px;
	background-color: #f5f5f5;
}
</style>
</head>
<body>
	<div class="container">
		<center>
			<h1>Data Upload Status Report</h1>
		</center>
		<table class="table table-bordered">
			<tr class="info">
   			 	<td>GSE #</td>
    			<td>${gse}</td>
  			</tr>
  			<tr class="info">
   			 	<td>Username</td>
    			<td>${username}</td>
  			</tr>
  			<tr class="info">
   			 	<td>Description</td>
    			<td>${description}</td>
  			</tr>
  			<tr class="info">
   			 	<td>Sample Size</td>
    			<td>${sampleSize}</td>
  			</tr>
  			<tr class="info">
   			 	<td>Probset Id Size</td>
    			<td>${probesetIdSize}</td>
  			</tr>
  			<tr class="info">
   			 	<td>Dataset Size</td>
    			<td>${dataSize}</td>
  			</tr>
		</table>
	</div>
</body>
</html>