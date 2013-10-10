package org.transmart.search

import org.transmart.SearchFilter
import org.transmart.searchapp.SearchKeyword
import javax.servlet.http.HttpSession

class SearchUtils {
	
	public static String geneidString(HttpSession httpSession, String seperator=" "){
		def geneids = ""
		for(SearchKeyword keyword: httpSession.searchFilter.globalFilter.getGeneFilters()) {
			if (geneids != "") {
				geneids += seperator
			}
			geneids += keyword.uniqueId.replaceAll("[\\D]", "")
		}
		geneids
	}


	public static geneSymbols(SearchFilter searchFilter) {
		def geneSearchTerms = []
		
		try {
			for (SearchKeyword keyword: searchFilter.globalFilter.getGeneFilters())
			{
				geneSearchTerms.add(keyword.keyword)
			}
		} catch (ignore) {}
		
		geneSearchTerms

	}
		
	public static geneSymbols(HttpSession httpSession) {
		
		def geneSearchTerms = []
		
		if (httpSession == null) return geneSearchTerms
		
		
		println("In SearchUtils - " + httpSession?.searchFilter.class.name)
		try {
			for (SearchKeyword keyword: httpSession.searchFilter.globalFilter.getGeneFilters())
			{
				println("In SearchUtils - keyword = " + keyword)
				geneSearchTerms.add(keyword.keyword)
			}
		} catch (ignore) {}
		
		geneSearchTerms
	}
}
