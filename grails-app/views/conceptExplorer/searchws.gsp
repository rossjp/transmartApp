<%@ page contentType="text/html;charset=ISO-8859-1" import="java.util.*; java.net.*; java.io.*; org.transmart.conceptgen.module.*" %>
<%

def URL u

if (gene) {
	if ((user == null) || (user=="") || (user == 'guest')) {
		u = new URL("http://conceptgen.ncibi.org/ConceptWeb/cts?qt=public&st=" + URLEncoder.encode(conceptKeyword,"UTF-8"));
	} else {
		u = new URL("http://conceptgen.ncibi.org/ConceptWeb/cts?qt=private&st=" + URLEncoder.encode(conceptKeyword,"UTF-8"));
	}
} else {
	u = new URL("http://conceptgen.ncibi.org/ConceptWeb/cws?qt=search&st=" + URLEncoder.encode(conceptKeyword,"UTF-8"));
}
BufferedReader inp = new BufferedReader(new InputStreamReader(u.openStream()));

ArrayList<String[]> list = new ArrayList<String[]>();
String[] line = inp.readLine().split("\\]\\, \\[");


for(String tmp : line)
{
	tmp = tmp.trim();
	tmp = tmp.replaceAll("\\[", "");
	tmp = tmp.replaceAll("\\]", "");
	tmp = tmp.replaceAll("\"", "");
	tmp = tmp.trim();
	if (tmp.length() > 0)
		list.add(tmp.split(","));
}

if (list.size == 1){
	idString = "id=" + list[0][0].trim()
	urlString = request.contextPath + "/conceptExplorer/indexws?" + idString
	response.sendRedirect(urlString)
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

<div class="textLarge">
Your search, <b><%= conceptKeyword %></b>, returns more then one match.
</div>
<div class="textMedium">
Please select from the results, below...
</div>

<%
if(list.size() < 1)
{

%>	
  <div class="content">
	<div class="resultText"> 
     There were no results for your search!
    </div>
  </div>
<%
}
else
{
%>
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
<%
}
%>
</body>
</html>
