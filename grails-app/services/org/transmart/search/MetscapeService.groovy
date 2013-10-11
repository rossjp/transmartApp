package org.transmart.search

import org.transmart.SearchFilter;

class MetscapeService {
	
	def int getCount(SearchFilter searchFilter){
		if (searchFilter == null) return 0
		def searchText = searchFilter.searchText
		if (searchText == null) return 0

		def geneidString = SearchUtils.geneidString(searchFilter)
		def geneidList = SearchUtils.geneidList(geneidString)
		if (geneidList == null) return 0;
		if (geneidList.isEmpty()) return 0;
		
		def geneid = geneidList[0]

		return 1
	}
	

}
