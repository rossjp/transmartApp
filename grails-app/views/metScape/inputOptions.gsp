<%@ page contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="org.ncibi.metab.network.NetworkType" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Cytoscape Web example</title>
</head>
<body>
<div id="header"><g:render template="/layouts/commonheader"	model="['app':'metScape']" /></div>
<div id="main">

<g:form action="graph">
	Compounds<br>
	<textarea rows="10" cols="40" name="cids" id="cids"></textarea>
	<br><br>Genes<br>
	<textarea rows="10" cols="40" name="geneids" id="geneids"></textarea>
	<br>Network Type<br>
	<g:select name="networktype"
          from="${NetworkType.values()}"
          value="${NetworkType.CREG}"
          optionKey="shortName" />
    <br>Organism<br>
    <select name="taxid">
		<option value=9606>Human</option>
		<option value=10116>Rat</option>
		<option value=10090>Mouse</option>
	</select><br>
	<input type="submit" value="Submit" />
</g:form>

</div>

</body>
</html>