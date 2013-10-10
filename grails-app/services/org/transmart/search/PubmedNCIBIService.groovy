package org.transmart.search

import org.ncibi.pubmed.RenderUsingSax
import org.transmart.SearchFilter

class PubmedNCIBIService {
	
	def DEFAULT_COUNT = 50;
	
	def getPubmedResultsByGene(int geneid,int limit)
	{
		def RenderUsingSax r = new RenderUsingSax()
		
		r.processDocument(geneid,limit)
		
		def results = r.getArrayResults()
		
		return results
	}

	def int getCount(SearchFilter searchFilter) {
		return getCount(searchFilter,DEFAULT_COUNT)
	}
	
	def int getCount(SearchFilter searchFilter,int limit){
		def searchText = searchFilter.searchText
		if (!searchText) return 0
		
		return 92
	}
}
