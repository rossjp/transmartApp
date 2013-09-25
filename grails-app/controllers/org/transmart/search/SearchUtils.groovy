package org.transmart.search

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

}
