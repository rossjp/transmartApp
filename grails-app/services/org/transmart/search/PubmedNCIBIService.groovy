package org.transmart.search

import org.ncibi.service.util.Util
import org.transmart.SearchFilter
import org.w3c.dom.Document

class PubmedNCIBIService {
	
	def getPubmedResultsByGene(String geneSymbol)
	{
		def results = []
		try {
			String urlString = "http://nlp.ncibi.org/fetch?tagger=nametagger&type=gene&id=" + URLEncoder.encode(geneSymbol);
			URL ncibiWS = new URL(urlString)
			URLConnection urlConnection = ncibiWS.openConnection()
			InputStream inputStream = urlConnection.getInputStream()
			
			Document xmlDocument = Util.xmlDocumentFrom(inputStream)

			results = g2mXmlToG2MResult(xmlDocument)
			
		} catch(Exception e) {
			e.printStackTrace()
		}
		return results
	}

	def int getCount(SearchFilter searchFilter){
		def searchText = searchFilter.searchText
		if (!searchText) return 0
		
		return 92
	}
}
