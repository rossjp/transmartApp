package org.transmart.search

import org.transmart.searchapp.SearchKeyword

class PubmedController {

	def pubmedNCIBIService
	
    def index() { 
		
		def results = []
		
		def geneids = SearchUtils.geneidString(session)
		if (geneids && List.iscase(geneids) && genesids[0]) {
			def int geneid = geneids[0] 
			resutls = pubmedNCIBIService.getPubmedResultsByGene(geneid)
			log.info(results.size())
		}
		
		render(view: "index", model:[results: results])
	}
}
