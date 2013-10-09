package org.transmart.search

import org.transmart.searchapp.SearchKeyword

class PubmedController {

	def DEFAULT_LENGTH = 20
	
	def pubmedNCIBIService
	
    def index() { 
		def geneidString = params.geneids
		def lengthString = params.length

		def geneids = []
		if (geneidString && (geneidString.length() > 0)) {
			geneids = makeIntList(geneidString)
		}
		def length = convertLength(lengthString)
		if (length < 0) {
			length = DEFAULT_LENGTH
		}
		
		def results = []
		if (!geneids.empty) {
			def int geneid = geneids[0]
			results = pubmedNCIBIService.getPubmedResultsByGene(geneid)
			log.info("PubmedNCIBIService - " 
				+ " length parameter: string = " + lengthString + ", value = " + length + "; " 
				+ "results size = " + results.size())
		}
		render(view: "index", model:[doclets: results])
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
