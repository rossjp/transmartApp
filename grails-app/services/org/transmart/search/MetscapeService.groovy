package org.transmart.search

import org.junit.Assert
import org.ncibi.metab.network.MetabolicNetwork
import org.ncibi.metab.network.NetworkType
import org.ncibi.metab.network.node.NodeType
import org.ncibi.metab.ws.client.MetabolicNetworkService
import org.ncibi.ws.HttpRequestType
import org.ncibi.ws.Response
import org.ncibi.ws.ResponseStatus
import org.transmart.SearchFilter

class MetscapeService {
	
	def getCount(SearchFilter searchFilter){
		def counts = [gene: 0, enzyme: 0, reaction: 0, compound: 0]
		
		if (searchFilter == null) return counts
		def searchText = searchFilter.searchText
		if (searchText == null) return counts

		def geneidString = SearchUtils.geneidString(searchFilter)
		def geneidList = SearchUtils.geneidList(geneidString)
		if (geneidList == null) return counts
		if (geneidList.isEmpty()) return counts
		
		def cids = []
		def networktype = NetworkType.CREG
		def taxid = 9606
		def service = new MetabolicNetworkService(HttpRequestType.POST, null)
		Response<MetabolicNetwork> serverResponse = 
			service.retrieveNetworkOfTypeForCidsAndGeneids(networktype, 
				(Collection)cids, (Collection)geneidList,
				taxid)
			
		if (serverResponse == null) return counts
		ResponseStatus rs = serverResponse.getResponseStatus();
		if (rs == null) return counts
		if (!rs.isSuccess()) return counts
		
		MetabolicNetwork network = serverResponse.getResponseValue();
		def genes = network.getNodesOfType(NodeType.GENE)
		def enzymes = network.getNodesOfType(NodeType.ENZYME)
		def reactions = network.getNodesOfType(NodeType.REACTION)
		def compounds = network.getNodesOfType(NodeType.COMPOUND)
		counts.gene = genes.size()
		counts.enzyme = enzymes.size()
		counts.reaction = reactions.size()
		counts.compound = compounds.size()
		log.info("counts: " + counts)
		return counts
	}
	
}
