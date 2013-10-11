package org.transmart.search

import org.transmart.SearchFilter
import org.transmart.searchapp.SearchKeyword
import javax.servlet.http.HttpSession

class SearchUtils {
	
	public static String geneidString(HttpSession httpSession){
		def geneids = ""
		if (httpSession == null) return geneids
		
		return geneidString(httpSession.searchFilter)
	}
	
	public static String geneidString(SearchFilter searchFilter, String seperator=" ") {
		def geneids = ""
		for(SearchKeyword keyword: searchFilter.globalFilter.getGeneFilters()) {
			if (geneids != "") {
				geneids += seperator
			}
			geneids += keyword.uniqueId.replaceAll("[\\D]", "")
		}
		geneids
	}

	public static geneidList(geneidString) {
		def ret = []
		for (fragment in geneidString.split(',')) {
			def probe = fragment.trim();
			try {
				int i = Integer.parseInt(probe)
				ret.add(i)
			} catch (ignore) {}
		}
		return ret
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
		
		return geneSymbols(httpSession.searchFilter)
	}
}
