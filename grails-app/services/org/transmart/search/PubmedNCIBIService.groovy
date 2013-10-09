package org.transmart.search

import org.ncibi.pubmed.RenderUsingSax
import org.transmart.SearchFilter

class PubmedNCIBIService {
	
	def getPubmedResultsByGene(int geneid,int limit)
	{
		def RenderUsingSax r = new RenderUsingSax()
		
		r.processDocument(geneid,limit)
		
		def results = r.getArrayResults()
		
		return results
	}

	def int getCount(SearchFilter searchFilter,int limit){
		def searchText = searchFilter.searchText
		if (!searchText) return 0
		
		return 92
	}
}
