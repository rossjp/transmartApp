<%@ page contentType="text/html;charset=ISO-8859-1" import="java.util.*; java.net.*; java.io.*; org.transmart.conceptgen.module.*" %>
<%
String url = "http://conceptgen.ncibi.org/ConceptWeb/cws?qt=conceptName&conceptId=" + conceptId;

URL u1 = new URL(url);
BufferedReader inp1 = new BufferedReader(new InputStreamReader(u1.openStream()));
String conceptName = inp1.readLine();




URL u = new URL("http://conceptgen.ncibi.org/ConceptWeb/cws?qt=search&st=cell");
BufferedReader inp = new BufferedReader(new InputStreamReader(u.openStream()));

ArrayList<String[]> list = new ArrayList<String[]>();
String[] line = inp.readLine().split("\\]\\, \\[");


for(String tmp : line)
{
	tmp = tmp.trim();
	tmp = tmp.replaceAll("\\[", "");
	tmp = tmp.replaceAll("\\]", "");
	tmp = tmp.replaceAll("\"", "");
	list.add(tmp.split(","));
}

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
	<% 
  	for(String[] rs : list)
	{
	%>
  	<tr>
   		<td>
		<div class="resultText"> 
			<a href="indexws?id=<%= rs[0].trim() %>&queryType=concept&owner=Public" title="Click on name to launch Concept Explorer">
			<%= rs[1].trim() %></a> 
		</div>
		<br />
		<div class="contentType"> Concept Type : <b><%= rs[4].trim() %></b></div>
		<br />
		<div class="contentStatistics">Gene List Size : <b><%= rs[3].trim() %></b> </div>
		<br />
		<br />
		</td>
	</tr>
	<%
	} 
	%>
  </table>
  </div>
</body>
</html>