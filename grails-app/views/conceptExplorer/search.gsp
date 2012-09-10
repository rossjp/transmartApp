<%@ page contentType="text/html;charset=ISO-8859-1" import="org.transmart.conceptgen.module.*" %>
<%
def conceptKeyword2 = "cell";

DataAssembler da = new DataAssembler();
def concepts = da.searchConcept(conceptKeyword, "20");


%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="layout" content="main"/>
<title>Conceptgen > Search Result</title>

<!--  CSS --------------------------------------------------------------------------------------------------------------------------------->

<link rel="stylesheet" href="${resource(dir:'css',file:'conceptgen.css')}" />

</head>

<body>
  <div class="content">
	<table width="100%" border="0" cellspacing="4" cellpadding="4">
  	<g:each in="${concepts}" var="item">
  	<tr>
   		<td>
		<div class="resultText"> 
			<a href="index?id=${item.conceptId}&queryType=concept&owner=Public" title="Click on name to launch Concept Explorer">
			${item.conceptName?.encodeAsHTML()}</a> 
		</div>
		<br />
		<div class="contentType"> Concept Type : <b>${item.concept_type_name?.encodeAsHTML()}</b></div>
		<br />
		<div class="contentStatistics">Gene List Size : <b>${item.elementSize?.encodeAsHTML()}</b> </div>
		<br />
		<br />
		</td>
	</tr>
	</g:each>  
  </table>
  </div>
</body>
</html>