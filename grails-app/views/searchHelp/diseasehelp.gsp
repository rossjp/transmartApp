<!--
  tranSMART - translational medicine data mart
  
  Copyright 2008-2012 Janssen Research & Development, LLC.
  
  This product includes software developed at Janssen Research & Development, LLC.
  
  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
  as published by the Free Software  * Foundation, either version 3 of the License, or (at your option) any later version, along with the following terms:
  1.	You may convey a work based on this program in accordance with section 5, provided that you retain the above notices.
  2.	You may convey verbatim copies of this program code as you receive it, in any medium, provided that you retain the above notices.
  
  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS    * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
  
 
-->

<html>
<head>
<title>${grailsApplication.config.com.recomdata.searchtool.appTitle}</title>
<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />

<script type="text/javascript">

	function refreshParent(newurl){
     parent.window.close();
	 if(parent!=null && parent.window.opener!=null && !parent.window.opener.closed){
		parent.window.opener.location =newurl;
		}
	}

</script>
</head>
<body>  <g:javascript src="tracking/track.js" />
<div id="summary">

<p class="Title"><span class="Title"> </span></p>
<div id="SummaryHeader"><span class="SummaryHeader">Available Disease Search Terms</span></div>
<table class="trborderbottom" width="100%">
	<g:each in="${diseases}" status="i" var="keyword">
		<tr style="border-bottom: 1px solid #CCCCCC;">
			<td>${createKeywordSearchLink(popup:true, jsfunction:'refreshParent', keyword:keyword)}</td>
		</tr>
	</g:each>
</table>
<br/>
</div>
</body>
</html>
