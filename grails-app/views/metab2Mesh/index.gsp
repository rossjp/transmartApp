<%@ page contentType="text/html;charset=ISO-8859-1" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="layout" content="main"/>
<title>Metab2MeSH Results</title>
</head>
<%--<body>--%>
<%--  <div class="body">--%>
<%--  <g:each in="${session.searchFilter.globalFilter.getAllFilters()}" var="keyword" status="i">--%>
<%--  <ul>--%>
<%--  	<li>Keyword: ${keyword.keyword}</li>--%>
<%--  	<li>BioDataID: ${keyword.bioDataId}</li>--%>
<%--  	<li>UniqueID: ${keyword.uniqueId}</li>--%>
<%--  	<li>ID: ${keyword.id}</li>--%>
<%--  	<li>Data Category: ${keyword.dataCategory}</li>--%>
<%--  	<li>Data Source: ${keyword.dataSource}</li>--%>
<%--  	<li>Display Data Category: ${keyword.displayDataCategory}</li>--%>
<%--  	<li>Owner Auth User ID: ${keyword.ownerAuthUserId}</li>--%>
<%--  </ul>--%>
<%--  </g:each>--%>
<%--  </div>--%>
<%--</body>--%>
<%--</html>--%>
<%----%>
<%--<%@ page import="transmartapp.M2MResult" %>--%>
<%--<html>--%>
<%--<head>--%>
<%--</head>--%>
<body>
<g:if test="${m2mResultList.size() == 0}">
	Sorry, no results found.
</g:if>
<g:else>
	<table class="trborderbottom" width="100%" border="2">
		<thead>
		<tr>
			<th>Compound Name</th>		
			<th>Compound ID</th>
			<th>Descriptor Name</th>
<%--			<th>Descriptor ID</th>--%>
			<th>Fold Change</th>
			<th>ChiSquare</th>
			<th>P-Value</th>
			<th>Q-Value</th>
		</tr>
		</thead>
	
		<tbody>
		<g:each in="${m2mResultList}" var="m" status="i">
		<tr style="border-bottom:1px solid #CCCCCC;padding-botton:2px;">		
	  		<td><a href="http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?cid=${m.compoundID}">${m.compoundName}</a></td>	
	  		<td><a href="http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?cid=${m.compoundID}">${m.compoundID}</a></td>	
	  		<td><a href="http://www.ncbi.nlm.nih.gov/mesh/${m.descriptorIDNum}">${m.descriptorName}</td>	
<%--	  		<td>${m.descriptorID}</td>--%>
	  		<td><g:formatNumber number="${m.fover}" maxFractionDigits="1" /></td>	
	  		<td><g:formatNumber number="${m.chiSquare}" maxFractionDigits="1" /></td>	
	  		<td><g:formatNumber number="${m.fisherExact}" format="00.00E00" /></td>	
	  		<td><g:formatNumber number="${m.qValue}" format="00.00E00" /></td>		  			
		</tr>
		</g:each>		
		</tbody>		
	</table>
</g:else>
</body>
</html>