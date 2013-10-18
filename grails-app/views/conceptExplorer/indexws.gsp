<%@ page contentType="text/html;charset=ISO-8859-1"  import="java.util.*; java.net.*; java.io.*; org.transmart.conceptgen.*; org.transmart.conceptgen.module.*; org.transmart.conceptgen.model.*" %>
<%
String conceptId = request.getParameter("id");
String queryType = "conceptPublic";
String conceptTypeId = "0";
String owner = request.getParameter("owner");
double fdr = 0.05;
double pValue = 0.05;
String service = "cws";

if(owner.equals("Private"))
{
   service = "cts";
}

URL u1 = new URL("http://conceptgen.ncibi.org/ConceptWeb/"+ service +"?qt=conceptName&conceptId=" + conceptId);
BufferedReader inp1 = new BufferedReader(new InputStreamReader(u1.openStream()));
String conceptName = inp1.readLine();

URL u2 = new URL("http://conceptgen.ncibi.org/ConceptWeb/"+ service +"?qt=enrichment&conceptId=" + conceptId);
BufferedReader inp2 = new BufferedReader(new InputStreamReader(u2.openStream()));
String data = inp2.readLine();

URL u3 = new URL("http://conceptgen.ncibi.org/ConceptWeb/"+ service +"?qt=conceptGeneList&conceptId=" + conceptId);
BufferedReader inp3 = new BufferedReader(new InputStreamReader(u3.openStream()));
String data2 = inp3.readLine();

URL u4 = new URL("http://conceptgen.ncibi.org/ConceptWeb/"+ service +"?qt=conceptType&conceptTypeId=0&conceptId=" + conceptId);
BufferedReader inp4 = new BufferedReader(new InputStreamReader(u4.openStream()));
String pieInfo = inp4.readLine();


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ConceptGen > Concept Viewer</title>


<!--  CSS --------------------------------------------------------------------------------------------------------------------------------->

<link rel="stylesheet" href="${resource(dir:'css',file:'conceptgen.css')}" />
<link rel="stylesheet" href="${resource(dir:'js/datatables/media/css',file:'demo_table_jui.css')}" />
<link rel="stylesheet" href="${resource(dir:'js/datatables/extras/TableTools/media/css',file:'TableTools_JUI.css')}" />
<link rel="stylesheet" href="${resource(dir:'js/jquery-ui/css/smoothness',file:'jquery-ui-1.8.16.custom.css')}" />

<!--  JS SCRIPT --------------------------------------------------------------------------------------------------------------------------->
<script>
var data = <%= data %>;
var data2 = <%= data2 %>;
</script>

<g:javascript src="jquery.min.js"/>
<g:javascript src="datatables/media/js/jquery.dataTables.js"/>
<g:javascript src="datatables/extras/TableTools/media/js/TableTools.js"/>
<g:javascript src="datatables/media/js/jquery.dataTables.js"/>
<g:javascript src="datatables/extras/TableTools/media/js/ZeroClipboard.js"/>


<g:javascript src="datatables/enrichment.js"/>
<g:javascript src="datatables/genelist.js"/>

<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() 
      {
        var datapie = google.visualization.arrayToDataTable([
         <%= pieInfo %>
        ]);

        var options = {
          title: 'Concept Types'
        };

        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(datapie, options);

        if(data.length > 250)
        {
			document.getElementById("drawNetworkButton").style.display = 'none';
        }
      }

      function drawNetwork()
      {
    	  
    	window.location.href = "graphws?id=<%= conceptId %>&networkType=direct&conceptName=<%= conceptName %>";
          
      }
</script>



</head>
<body>
<div id="mainFrame">
	<div id="conceptTitle">
		<%= conceptName %>
	</div>
	
	<div id="concepts" class="playout">
		<table width="100%">
		<tr>
			<td width="50%">
				<div id="genelist"></div>
			</td>
			<td width="50%">
				<div id="chart_div" style="width: 600px; height: 400px;"></div>
			</td>
		</tr>
		<tr>
			<td>
				
				<input id="drawNetworkButton" type="button" value="Draw Network" onclick="drawNetwork()">
			</td>
		</tr>
		</table>
		<div class="lineFull"></div>
		<div id="container"></div>
	
	</div>
</div>
</body>
</html>
