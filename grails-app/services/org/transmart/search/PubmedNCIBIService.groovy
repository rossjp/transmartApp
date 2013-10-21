package org.transmart.search

import org.ncibi.pubmed.RenderUsingSax
import org.transmart.SearchFilter

class PubmedNCIBIService {
	
	def DEFAULT_COUNT = 20
	def moreDocumentsAvailable = false
	
	def getPubmedResultsByGene(int geneid) {
		getPubmedResultsByGene(geneid, DEFAULT_COUNT)
	}

	def getPubmedResultsByGene(int geneid,int count)
	{
		moreDocumentsAvailable = false
		def RenderUsingSax r = new RenderUsingSax()
		
		//NOTE: count is ment to be number of articles; 
		// but limit is actually on number of sentances
		// so we overshoot to get the number of articles
		// intended
		def limit = count * 3 
		
		r.processDocument(geneid,limit)
		
		def results = r.getArrayResults(count)
		moreDocumentsAvailable = r.moreAvailable()
		
		return results
	}
	
	def int getCount(SearchFilter searchFilter) {
		return getCount(searchFilter,DEFAULT_COUNT)
	}
	
	def int getCount(SearchFilter searchFilter,int limit){
		if (searchFilter == null) return -1
		def searchText = searchFilter.searchText
		if (searchText == null) return -1

		def geneid = SearchUtils.firstGeneId(searchFilter)
		if (geneid == null) return -1
		def value = -1
		try {
			def docs = getPubmedResultsByGene(geneid)
			value = docs.size()
		} catch (Throwable t) {
			log.info("Failure in Pubmed count.", t);
		}
 
		return value
	}
}
