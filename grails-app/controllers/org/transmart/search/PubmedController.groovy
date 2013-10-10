package org.transmart.search

import org.transmart.searchapp.SearchKeyword


class PubmedController {

	def DEFAULT_LENGTH = 20
	
	def pubmedNCIBIService
	
    def index() {

		def geneids = []
		
		// check params first - for testing 
		def geneidString = params.geneids
		def lengthString = params.length

		// override with session information - from search page
		def geneidsFromSession = SearchUtils.geneidString(session)

		if (geneidsFromSession.length() > 0){
			geneidString = geneidsFromSession
		}
		
		if (geneidString && (geneidString.length() > 0)) {
			geneids = makeIntList(geneidString)
		}

		def length = convertLength(lengthString)
		if (length < 0) {
			length = DEFAULT_LENGTH
		}

		def results = []
		def int geneid = 0
		def geneSymbol = ""
		if (!geneids.empty) {
			geneid = geneids[0]
			results = pubmedNCIBIService.getPubmedResultsByGene(geneid,length)
			geneSymbol = SearchUtils.geneSymbols(session)[0]
			log.info("PubmedController - " 
				+ "length parameter: string = " + lengthString + ", value = " + length + "; " 
				+ "results size = " + results.size() + "; gene id = " + geneid + "; gene symbol = " + geneSymbol)
		} else {
			log.info("PubmedController - geneid list is empty!")
		}
		render(view: "index", model:[doclets: results, geneid: geneid, genesymbol: geneSymbol])
	}
	
	private convertLength(string) {
		int length = -1
		if (string) {
			try {
				def probe = string.trim()
				int i = Integer.parseInt(probe)
				length = i
			} catch (ignore) {}
		}
		return length
	}
	
	private makeIntList(string){
		def ret = []
		for (fragment in string.split(',')) {
			def probe = fragment.trim();
			try {
				int i = Integer.parseInt(probe)
				ret.add(i)
			} catch (ignore) {}
		}
		return ret
	}
}
