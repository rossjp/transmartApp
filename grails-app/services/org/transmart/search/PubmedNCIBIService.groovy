package org.transmart.search

import org.ncibi.pubmed.RenderUsingSax
import org.transmart.SearchFilter

class PubmedNCIBIService {
	
	private int LIMIT = 20;
	
	def getPubmedResultsByGene(geneid)
	{
		def RenderUsingSax r = new RenderUsingSax()
		
		r.processDocument(geneid,LIMIT)
		
		def results = r.arrayResults()
		
		return results
	}

	def int getCount(SearchFilter searchFilter){
		def searchText = searchFilter.searchText
		if (!searchText) return 0
		
		return 92
	}
}
