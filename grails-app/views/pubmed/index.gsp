<%@ page contentType="text/html;charset=ISO-8859-1" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="layout" content="main"/>
<title>Pubmed Search Results
<g:if test="${geneid > 0}">
(${genesymbol})
</g:if>
<g:else>
(No GeneID given!)
</g:else>
</title>
</head>
<body>
<g:if test="${doclets.size() == 0}">
	Sorry, no results found.
</g:if>
<g:else>
	<g:if test="${more }">
		<div class="note">Showing only ${doclets.size()} documents. Additional documents are available.
			See: up to <a href="${createLink(controller:'pubmed',action:'index',params:[geneids:geneid,length:50])}" target="_blank">50</a>, 
			up to <a href="${createLink(controller:'pubmed',action:'index',params:[geneids:geneid,length:100])}" target="_blank">100</a>, 
			<a href="${createLink(controller:'pubmed',action:'index',params:[geneids:geneid,length:100000])}" target="_blank">all</a>
		</div>
	</g:if>
	<g:else>
		<div class="note">Showing ${doclets.size()} documents. </div>
	</g:else>
	<h1>Matches for gene search: ${genesymbol}</h1>
	<table class="trborderbottom" width="100%" border="2">
		<thead>
		<tr>
			<th>PMID</th>		
			<th>Abstract from Atricle (gene match annotated)</th>
		</tr>
		</thead>
	
		<tbody>
		<g:each in="${doclets}" var="doc" status="i">
		<tr style="border-bottom:1px solid #CCCCCC;padding-botton:2px;">		
	  		<td><a href="http://www.ncbi.nlm.nih.gov/pubmed/${doc.pmid}" target="_blank">${doc.pmid}</a></td>	
	  		<td>${doc.pargraph}</td>
		</tr>
		</g:each>		
		</tbody>		
	</table>
</g:else>
</body>
</html>