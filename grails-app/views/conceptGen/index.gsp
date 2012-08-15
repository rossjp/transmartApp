<%@ page contentType="text/html;charset=ISO-8859-1" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="layout" content="main"/>
<title>Insert title here</title>
</head>
<body>
  <div class="body">
  <g:each in="${session.searchFilter.globalFilter.getAllFilters()}" var="keyword" status="i">
  <ul>
  	<li>Keyword: ${keyword.keyword}</li>
  	<li>BioDataID: ${keyword.bioDataId}</li>
  	<li>UniqueID: ${keyword.uniqueId}</li>
  	<li>ID: ${keyword.id}</li>
  	<li>Data Category: ${keyword.dataCategory}</li>
  	<li>Data Source: ${keyword.dataSource}</li>
  	<li>Display Data Category: ${keyword.displayDataCategory}</li>
  	<li>Owner Auth User ID: ${keyword.ownerAuthUserId}</li>
  </ul>
  </g:each>
  </div>
</body>
</html>