/**
* $Id: $
* @author $Author: $
* @version $Revision: 14836 $
*/

import org.json.*

import fm.FmFile;
import fm.FmFolder;
import groovy.xml.StreamingMarkupBuilder

import javax.xml.transform.TransformerFactory
import javax.xml.transform.Transformer
import javax.xml.transform.OutputKeys
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

//import bio.BioAnalysisAttribute
import bio.Experiment
import bio.BioMarkerCorrelationMV

import org.apache.commons.codec.binary.Base64

//import RWGVisualizationDAO

import grails.converters.*				// so we can render as JSON

import search.SearchKeyword
import search.SearchTaxonomy
import search.SearchTaxonomyRels
import search.GeneSignature
import search.GeneSignatureItem

import groovy.time.TimeCategory;
import groovy.time.TimeDuration;
import groovyx.net.http.HTTPBuilder

class RWGController {
	def trialQueryService
	def searchService
	def searchKeywordService	
	def springSecurityService
	def formLayoutService
	def fmFolderService
	def ontologyService
	def solrFacetService
	
    def index = {
		
		def exportList = session['export'];
		
		def rwgSearchFilter = session['rwgSearchFilter'];
		if (rwgSearchFilter) {
			rwgSearchFilter = rwgSearchFilter.join(",,,")
		}
		else {
			rwgSearchFilter = "";
		}
		
		def rwgSearchOperators = session['rwgSearchOperators'];
		if (rwgSearchOperators) {
			rwgSearchOperators = rwgSearchOperators.join(";")
		}
		else {
			rwgSearchOperators = "";
		}
		
		def globalOperator = session['globalOperator'];
		def searchCategory = session['searchCategory'];
		
		return [rwgSearchFilter: rwgSearchFilter, rwgSearchOperators: rwgSearchOperators, globalOperator: globalOperator, rwgSearchCategory: searchCategory, exportCount: exportList?.size(), debug: params.debug];
	}
	
	def ajaxWelcome = {
		render (template: 'welcome');
	}
	
	def searchLog = {
		render ([log: session['searchLog']] as JSON)
	}
	
	def updateSearchCategory = {
		session['searchCategory'] = params.id
		render(status: 200, text: "OK")
	}

	/**
	 * START: Methods for the faceted search filter
	 */
	
	
   /*
   * Retrieve the SOLR field name equivalent for a term name 
   */
   private String getSOLRCategoryName(String field)  {
	   // set to uppercase and replace spaces with underscores
	   return field.toUpperCase().replace(' ', '_')
   }	
   
   /**
   * Add a new node to the taxonomy Dynatree (and recursively add children if any exist).
   * parentNode: Node to add to tree
   * json: JSON array containing the "children" of the jQuery dynaTree
   * isCategory: boolean indicating whether the node being added is a category
   * categoryName: name of the category (i.e. as stored in database and displayed in tree)
   * uniqueTreeId: unique identifier for the node being added. This ill be a concatenation of the parent's unique id + the index of this child's index in children list
   *     e.g. category nodes will be 1,2,3; their children will be 1:1, 1:2, 1:3, 2:1, ...; their children 1:1:1, 1:1:2, ...
   * initialFacetCounts: JSONObject containing the initial facet counts for the nodes in the tree
   */
  // note: use a function instead of a closure - this is being called hundreds of times, and being a fn makes a big difference
  //def addDynaNode = {SearchTaxonomy parentNode, JSONArray json, boolean isCategory, String categoryName  ->
  public void addDynaNode(SearchTaxonomy parentNode, JSONArray json, boolean isCategory, 
	                      String categoryName, String uniqueTreeId, JSONObject initialFacetCounts) {
	  JSONArray children = new JSONArray()
	  
	  // create map for attributes of node
	  def parent = [:]
	  
	  // create a custom attribute for term name
	  parent["termName"] = parentNode.termName
	  
	  // generate the id for use in tree and for link to active terms
	  // if there is a link to an active term, use that as id (i.e. search_keyword_id)
	  // if not, use the id from the search_taxonomy table prepended with a letter (to ensure that there are no id collisions)
      def id 
	  if (parentNode.searchKeywordId)  {
		  id = parentNode.searchKeywordId
	  }
	  else  {
		  id = 'X' + parentNode.id
	  }
	  parent["id"] = id
	  
	  // create the key that matches what we use in javascript to identify search terms
	  // assuming for now that the category and the category display are the same (with category being all caps); may
	  // need to break this out into separate fields	  
	  parent["key"] = categoryName + "|" + categoryName.toUpperCase() + ";" + parentNode.termName + ";" + id	  
	  
	  // if category, then display as folder and don't show checkbox; other levels, not a folder and show checkbox
	  parent["isFolder"] = isCategory 
	  parent["hideCheckbox"] = isCategory

	  // add custom attributes for each node 
	  parent["isCategory"] = isCategory
	  parent["categoryName"] = categoryName + "|" + categoryName.toUpperCase()

	  // create a uniqueTreeId for each node so we can identify it from it's copies
	  //  (id and key are not unique amongst copies)
	  parent["uniqueTreeId"] = uniqueTreeId


	  // Create custom attributes for the facet count for this node, and one for the initial facet
	  //   count which will be used to save the value when the tree gets cleared so we don't have to requery 
	  // Set to -1 for category nodes
	  if (isCategory)  {		 
		  parent["facetCount"] = -1
		  parent["initialFacetCount"] = -1
		  
		  //title is same as term name for categories
          parent["title"] = parentNode.termName
	  }
	  else  {
		  // get the json object for the category	  
		  JSONObject jo = (JSONObject)initialFacetCounts.get(getSOLRCategoryName(categoryName))
		  
		  // convert the term id to a string
          String idString = id.toString()

		  // retrieve the count for the term id if it exists in the json object, otherwise
		  //  none found so it's zero
		  int count		  
		  if (jo.has(idString))  {
		     count =  jo.getInt(idString)
		  }
	      else {
			  count = 0
		  } 

		  parent["facetCount"] = count
		  parent["initialFacetCount"] = count
		  
		  
		  // if the initial count is zero, don't add to tree
		  if (count == 0)  {
			  return
		  }
		  
		  
		  // include facet count in title for non-category nodes
		  parent["title"] = /${parentNode.termName} (${count})/ 
	  }
	  
	  def childIndex = 1
	  if (parentNode.children) {
		  // recursively add each child
		  for (childNode in parentNode.children)  {			 
			  addDynaNode(childNode, children, false, categoryName, uniqueTreeId + ";" + childIndex, initialFacetCounts)
			  childIndex++
		  }
	  }

	  // don't add categories without children to tree
	  if (isCategory && (children.length() == 0))  {
		  //Removing this for now, we won't have any children in our tree. We are doing browse popups.
		  //return
	  }
	  
	  // add children to parent map
	  parent["children"] = children
	  
	  // add parent map to json array
	  json.put(parent)
  }

   /*
   * Create the JSON string used as the "children" of the taxonomy DynaTree
   */
  def getDynatree = {
	  
	  render("Not implemented");

   }

   /**
   * Replace any gene lists or signatures in the query parameters with a list of individual genes
   * @param params list of query params
   * @param genesField SOLR search field that gene searches are executed against (i.e. ALLGENE or SIGGENE)
   * @return string containing the new query parameters
   */
   def replaceGeneLists = {  params, genesField  ->
	   def newParams = []
	   def genesList = []  

	   // loop through each regular query parameter
	   for (p in params)  {
		   
		   // each queryParam is in form cat1:term1|term2|term3
		   String category = p.split(";")[0]
		   String termList = p.split(";")[1]

		   // add all the genes from a gene list/sig to the List of genes		   
		   if (category == 'GENELIST' || category == 'GENESIG')  {
			   for (t in termList.tokenize("|"))  {

				   // create the paramter list for the hibernate query (need to convert the id explicitly to long) 
				   def queryParams = [:]
				   Long l = t.toLong()
				   queryParams["tid"] = l

				   def geneKeywords = SearchKeyword.executeQuery("select k_gsi.id " +
						            " from search.SearchKeyword k_gs, search.GeneSignature gs," +
									" search.GeneSignatureItem gsi, search.SearchKeyword k_gsi " +
									" where k_gs.bioDataId = gs.id " +
									" and gs.id = gsi.geneSignature " +
									" and gsi.bioMarker = k_gsi.bioDataId" + 
                                    " and k_gs.id = :tid ", queryParams)
				   
				   // loop through each keyword for the gene list items and add to list 
				   geneKeywords.each {
					   // don't add duplicates
					   if (genesList.indexOf(it)<0)   {
						   genesList.add it
					   }
				   }			 
				   
			   }
		   }
		   // add all the genes from a pathway to the List of genes		   
		   else if (category == 'PATHWAY')  {
			   for (t in termList.tokenize("|"))  {
				   
				   // create the parameter list for the hibernate query (need to convert the id explicitly to long) 
				   def queryParams = [:]
				   Long l = t.toLong()
				   queryParams["tid"] = l
				   def geneKeywords = SearchKeyword.executeQuery("select k_gene.id " +
						            " from search.SearchKeyword k_pathway, bio.BioMarkerCorrelationMV b," +
									" search.SearchKeyword k_gene " +
									" where b.correlType = 'PATHWAY_GENE' " +  
                                    " and b.bioMarkerId = k_pathway.bioDataId " + 
									" and k_pathway.dataCategory = 'PATHWAY' " +
									" and b.assoBioMarkerId = k_gene.bioDataId " +
									" and k_gene.dataCategory = 'GENE' " +
                                    " and k_pathway.id = :tid ", queryParams) 
 
				   // loop through each keyword for the gene list items and add to list 
				   geneKeywords.each {
					   // don't add duplicates
					   if (genesList.indexOf(it)<0)   {
						   genesList.add it
					   }
				   }			 
				   
			   }
		   }
		   // add all the individual genes to the List of genes
		   else if (category == 'PROTEIN' )  {
			   for (t in termList.tokenize("|"))  {
				   genesList.add t				   
			   }
		   }
		   // add all the individual genes to the List of genes
		   else if (category == 'GENE' )  {
			   for (t in termList.tokenize("|"))  {
				   genesList.add t				   
			   }
		   }
		   else  {
			   // create the new params with everything that is not a gene or list
			   newParams.add p
		   }
	   }
	   
	   // create the new string to be used for genes and lists/sigs and add back to params 
	   def newGeneString = ""
	   
	   if (genesList.size > 0)  {
		   newGeneString = /${genesField}:${genesList.join('|')}/
		   //Commenting this out, we don't have to worry about signifigance just yet.
           //newParams.add newGeneString
	   }
	   
	   log.info("Gene parameter: ${newParams}")
	   return newParams
   }
   
   def expandGeneList = { geneListUid ->
	   
	   def genesList = []
	   
		   
	  def geneSig = GeneSignature.findByUniqueId(geneListUid)
	  def geneKeywords = GeneSignatureItem.createCriteria().list {
		  eq('geneSignature', geneSig)
	  }
	  
	  // loop through each keyword for the gene list items and add to list
	  geneKeywords.each {
		  // don't add duplicates
		  if (it.bioDataUniqueId && genesList.indexOf(it.bioDataUniqueId)<0)   {
			  genesList.add it.bioDataUniqueId
		  }
	  }
	  
	   
	   return genesList
	   
   }
   
   def expandPathway = { pathwayUid ->
	   
	   def genesList = []
		   
	   def geneKeywords = SearchKeyword.executeQuery("""
		   				select k_gene.uniqueId
						from search.SearchKeyword k_pathway, bio.BioMarkerCorrelationMV b,
						search.SearchKeyword k_gene
						where b.correlType = 'PATHWAY_GENE'
						and b.bioMarkerId = k_pathway.bioDataId
						and k_pathway.dataCategory = 'PATHWAY'
						and b.assoBioMarkerId = k_gene.bioDataId
						and k_gene.dataCategory = 'GENE'
						and k_pathway.uniqueId = :pathwayUid """, [pathwayUid: pathwayUid])

	   // loop through each keyword for the gene list items and add to list
	   geneKeywords.each {
		   // don't add duplicates
		   if (genesList.indexOf(it)<0) {
			   genesList.add it
		   }
	   }
	   
	   return genesList
   }
   
   //Just clear the search filter and render non-null back
   def clearSearchFilter = {
	   session['rwgSearchFilter'] = [:];
	   session['rwgSearchOperators'] = [:];
	   render(text: "OK")
   }
   
   /**
    * 
    */
   def getFacetResults = {
	   session['folderSearchList'] = [[], []]; //Clear the folder search list
	   
	   /*
	    * Record this as the latest search and store it in the session
	    */
	   def paramMap = params
	   //Search string is saved in session (for passing between RWG and Dataset Explorer pages)
	   def searchString = params.searchTerms
	   def searchOperatorsString = params.searchOperators
	   def globalOperator = params.globaloperator
	   
	   def searchTerms = searchString?.split(",,,")
	   if (searchTerms != null && searchTerms[0] == "") {searchTerms = null;}
	   
	   def searchOperators = searchOperatorsString?.split(";")
	   if (searchOperators != null && searchOperators[0] == "") {searchOperators = null;}
	   
	   session['rwgSearchFilter'] = searchTerms
	   session['rwgSearchOperators'] = searchOperators
	   session['globalOperator'] = globalOperator
	   def searchLog = ["Starting a new search"]
	   
	   /*
	    * Pre-processing
	    */
	   
	   //Convert gene categories into unified category. Our operator will be the last one processed.
	   def geneGroups = []
	   def categorizedSearchTerms = request.getParameterValues('q') as List
	   def processedSearchTerms = []
	   
	   //Separate gene-related search terms into gene groups.
	   //Always set geneOperator if this is a gene search term - the last one is the one we want to use.
	   def geneOperator = "or"
	   for (categoryLine in categorizedSearchTerms) {
		   def operator = ((String) categoryLine).split("::")[1].toUpperCase()
		   def category = ((String) categoryLine).split("::")[0]
		   
		   def categoryName = ((String) category).split(":", 2)[0]
		   def termList = ((String) category).split(":", 2)[1].split("\\|")
		   
		   if (categoryName.equals("GENE")) {
			   //Easy - get each term and add them as gene groups of 1
			   for (term in termList) {
				   geneGroups.add([term])
			   }
			   geneOperator = operator
		   }
	       else if (categoryName.equals("GENELIST") || categoryName.equals("GENESIG")) {
			   for (t in termList) {
				   def expandedList = expandGeneList(t)
				   if (expandedList) {
					   geneGroups += [expandedList]
				   }
			   }
			   geneOperator = operator
		   }
		   else if (categoryName.equals("PATHWAY")) {
			   for (t in termList) {
				   def expandedList = expandPathway(t)
				   if (expandedList) {
					   geneGroups += [expandedList + t] //Retain pathways
				   }
			   }
			   geneOperator = operator
			   
		   }
		   else {
			   processedSearchTerms.add(categoryLine)
		   }
	   }
	   
	   //Now create a new GENE category with the computed groups and the latest operator.
	   if (geneGroups) {
		   def geneGroupStrings = []
		   for (group in geneGroups) {
			   geneGroupStrings += group.join("/")
		   }
		   def newGeneCategory = "GENE:" + geneGroupStrings.join("|") + "::" + geneOperator
		   processedSearchTerms.add(newGeneCategory)
	   }
	   
	   //If we have no search terms and this is for RWG, just return the top level
	   if ((processedSearchTerms == null || processedSearchTerms.size() == 0) && params.page.equals('RWG')) {
		   searchLog += "No search terms found - returning all programs"
		   session['searchLog'] = searchLog
		   render(template:'/fmFolder/folders', model: [folders: fmFolderService.getFolderContents(null)])
		   return
	   }
	   
	   /*
	    * Run the search!
	    */
	   def combinedResult = solrFacetService.getCombinedResults(processedSearchTerms, params.page, globalOperator, searchLog)
	   session['searchLog'] = combinedResult.searchLog
	   
	   /**
	    * Organize and display
	    */
	   if (params.page.equals('RWG')) {
		   def folderContents = fmFolderService.getFolderContents(null)
		   
		   if (combinedResult.paths) {
			   def pathLists = finalizePathLists(combinedResult.paths)
			   session['folderSearchList'] = pathLists
			   def folderSearchString = pathLists[0].join(",") + "," //Extra , - used to identify search results
			   def uniqueLeavesString = pathLists[1].join(",") + ","
			   session['searchLog'] += "Final folder string: " + folderSearchString
			   render (template:'/fmFolder/folders', model: [folders: folderContents, folderSearchString: folderSearchString, uniqueLeavesString: uniqueLeavesString, auto: true])
		   }
		   else {
			   session['folderSearchList'] = [[],[]]
			   render(template:'/fmFolder/noResults')
		   }
	   }
	   else {
		   def pathLists = finalizePathLists(combinedResult.paths)
		   def jsonArrays = [:]
		   jsonArrays.put("searchResults", pathLists[0])
		   jsonArrays.put("uniqueLeaves", pathLists[1])
		   render jsonArrays as JSON
	   }	   
   }
   
   def finalizePathLists(pathList) {
	   def uniquePaths = []
	   def uniqueLeaves = []
	   for (path in pathList) {
		   if (!uniquePaths.contains(path)) {
			   uniquePaths.push(path)
		   }
		   
		   if (!uniqueLeaves.contains(path)) {
			   //If no other path in the path list starts with this path, it's uniquely a leaf
			   def childPathFound = false
			   for (otherPath in pathList) {
				   if (otherPath.startsWith(path) && !otherPath.equals(path)) {
					   childPathFound = true
					   break
				   }
			   }
			   if (!childPathFound) {
				   uniqueLeaves.push(path)
			   }
		   }
	   }
	   
	   return [uniquePaths, uniqueLeaves]
   }

   /**
   * Determine field to be used for genes within the SOLR queries and set session parameter
   * @param showSigGenesOnly boolean indicating whether analysis for all genes or only significant genes (default) will be shown
   * @return SOLR field to be used for gene searches
   */
   def setSOLRGenesField = {
	   showSigGenesOnly = true ->

	   def solrGenesField = ""  // name of SOLR search field to be used for gene queries (SIGGENE or ALLGENE)
	   if (showSigGenesOnly)  {
		   solrGenesField = 'SIGGENE'
	   }
	   else  {
		   solrGenesField = 'ALLGENE'
	   }
	   
	   session['solrGenesField'] = solrGenesField

	   return solrGenesField
   }

      
   /**
   * Load the initial facet results for the tree (no filters)
   * @return JSON object containing facet counts
   */
   def getInitialFacetResults = {List categoriesList  ->
	   // initial state of the significant field is checked, so need to add the search field to the SOLR query to get the initial facet coutns
	   //  and save the search term to the session variable so that is applied to the query to get the analysis list 
	   //def queryParams = ["ANY_SIGNIFICANT_GENES:1"]
	   def queryParams = []
	   session['solrSearchFilter'] = queryParams
	   log.info("Initial facet search: " + queryParams)

	   // set session var for SOLR genes field (no param passed so default will be used)
	   setSOLRGenesField() 
	   	   	   
	   // build the SOLR query
	   
	   // get the base query string (i.e. "q=(*:*)" since no filters for initial search
	   def nonfacetedQueryString = createSOLRNonfacetedQueryString(queryParams)
	   def facetedQueryString = ""
	   def facetedFieldsString = createSOLRFacetedFieldsString(categoriesList)

	   String solrRequestUrl = createSOLRQueryPath()
	   String solrQueryString = createSOLRQueryString(nonfacetedQueryString, facetedQueryString, facetedFieldsString)

	   JSONObject facetCounts = executeSOLRFacetedQuery(solrRequestUrl, solrQueryString, false)
		
       return facetCounts
	   
   }

      
   /**
   * START: Methods for the keyword search
   */   
   // Sets the search filter for the new search.  
   def newSearch =	{
	   session['solrSearchFilter'] = []
	   render(status: 200)
   }

   // Return search categories for the drop down 
   def getSearchCategories = {
	   render searchKeywordService.findSearchCategories() as JSON	   
   }
   
   def getFilterCategories = {
	   render searchKeywordService.findFilterCategories() as JSON
   }
   
   // Return search keywords
   def searchAutoComplete = {
	   def category = params.category == null ? "ALL" : params.category
	   def max = params.long('max') ?: 15
	   render searchKeywordService.findSearchKeywords(category, params.term, max) as JSON	   
   }
        
   // Load the search results for the given search terms using the new annotation tables
   // return the html string to be rendered for the results panel
   def loadSearchResults = { studyCounts, startTime ->	   
	   def exprimentAnalysis = [:]						// Map of the trial objects and the number of analysis per trial
	   def total = 0								// Running total of analysis to show in the top banner

	   def studyWithResultsFound = false   
	   
   	   for (studyId in studyCounts.keys().sort()) {
		   def c = studyCounts[studyId].toInteger()

		   if (c > 0)  {
			   studyWithResultsFound = true
		   
			   Long expNumber= Long.parseLong(studyId)

			   def exp = Experiment.createCriteria()
			   def experiment = exp.get	{
				   eq("id", expNumber)
			   }
			   if (experiment == null)	{
				   log.warn "Unable to find an experiment for ${expNumber}"
			   }
			   else  {
				   exprimentAnalysis.put((experiment), c)
				   total += c
			   }
		   }
	   }
	   // capture html as a string that will be passed back in JSON object
	   def html
	   if (!studyWithResultsFound)	{
		   html = g.render(template:'/search/noResult').toString()
	   } else {
	       html = g.render(template:'/RWG/experiments',model:[experiments:exprimentAnalysis, analysisCount:total, duration:TimeCategory.minus(new Date(), startTime)]).toString()
	   }
	   
	   return html
   }
      
   // Load the trial analysis for the given trial
   def getTrialAnalysis = {	   
	   new AccessLog(username: springSecurityService.getPrincipal().username,
		   event:"Loading trial analysis", eventmessage:params.trialNumber, accesstime:new Date()).save()

	   def analysisList = trialQueryService.querySOLRTrialAnalysis(params, session.solrSearchFilter)
	   render(template:'/RWG/analysis', model:[aList:analysisList])
   }
   
   def getFileDetails = {
	   def layout = formLayoutService.getLayout('file')
	   render(template:'/fmFolder/fileMetadata', model:[layout: layout, file: FmFile.get(params.id)])
   }
   
   def solrQuery = {
	   
   }
   
   //Execute arbitrary SOLR query, because SOLR's web interface doesn't work
   def executeSolrQuery = {
   	   // submit request
	   def solrRequestUrl = createSOLRQueryPath()
	   def solrConnection = new URL(solrRequestUrl).openConnection()
	   solrConnection.requestMethod= "POST"
	   solrConnection.doOutput = true

	   // add params to request 	   	   
	   def dataWriter = new OutputStreamWriter(solrConnection.outputStream)
	   dataWriter.write(params.q)
	   dataWriter.flush()
	   dataWriter.close()
	   
	   def slurper = new XmlSlurper()
	   
	   // process response
	   if (solrConnection.responseCode == solrConnection.HTTP_OK)  {
		   def xml
		   
		   solrConnection.inputStream.withStream {
			   xml = slurper.parse(it)
		   }

		   def result = new StreamingMarkupBuilder().bind{
			   mkp.yield xml
		   }
		   
		   render(contentType: "application/xml", text: result);
	   }
	   else {
		   render(contentType: "text/plain", text: "SOLR Request failed! Request url:" + solrRequestUrl + "  Response code:" + solrConnection.responseCode + "  Response message:" + solrConnection.responseMessage)
	   }
	   
	   solrConnection.disconnect()
   }
   
}