<%@ page contentType="text/html;charset=ISO-8859-1" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="layout" content="main"/>
<title>Insert title here</title>
</head>
<body>
  <div class="body">
  <table border="1">
  <tr>
  	  <th>Keyword</th>
      <th>BioDataID</th>
      <th>UniqueID</th>
      <th>ID</th>
      <th>Data Category</th>
      <th>Data Source</th>
      <th>Display Data Category</th>
      <th>Owner Auth User ID</th>
  </tr>
  <g:each in="${session.searchFilter.globalFilter.getAllFilters()}" var="keyword" status="i">
  <tr>
	  <td>${keyword.keyword}</td>
	  <td>${keyword.bioDataId}</td>
	  <td>${keyword.uniqueId}</td>
	  <td>${keyword.id}</td>
	  <td>${keyword.dataCategory}</td>
	  <td>${keyword.dataSource}</td>
	  <td>${keyword.displayDataCategory}</td>
	  <td>${keyword.ownerAuthUserId}</td>
  </tr>
  </g:each>	
  </table>
  </div>
</body>
</html>