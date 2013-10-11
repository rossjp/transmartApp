package org.ncibi.metscape

import static org.junit.Assert.*

import org.junit.*
import org.ncibi.metab.network.MetabolicNetwork
import org.ncibi.metab.network.NetworkType
import org.ncibi.metab.network.node.NodeType
import org.ncibi.metab.ws.client.MetabolicNetworkService
import org.ncibi.ws.HttpRequestType
import org.ncibi.ws.Response
import org.ncibi.ws.ResponseStatus

class MetscapeCountTests {

	@Before
	void setUp() {
		// Setup logic here
	}

	@After
	void tearDown() {
		// Tear down logic here
	}

	@Test
	void testSomething() {
		def cids = []
		def geneids = [1436]
		def networktype = NetworkType.CREG
		def taxid = 9606
		def service = new MetabolicNetworkService(HttpRequestType.POST, null)
		Response<MetabolicNetwork> serverResponse = service.retrieveNetworkOfTypeForCidsAndGeneids(networktype, (Collection)cids, (Collection)geneids,
				taxid)
		ResponseStatus rs = serverResponse.getResponseStatus();
		Assert.assertTrue(rs.isSuccess());
		MetabolicNetwork network = serverResponse.getResponseValue();
		def genes = network.getNodesOfType(NodeType.GENE)
		def enzymes = network.getNodesOfType(NodeType.ENZYME)
		def reactions = network.getNodesOfType(NodeType.REACTION)
		def compounds = network.getNodesOfType(NodeType.COMPOUND)
		def geneCount = genes.size()
		def enzymeCount = enzymes.size()
		def reactionCount = reactions.size()
		def compoundCount = compounds.size()
		println(geneCount)
		println(enzymeCount)
		println(reactionCount)
		println(compoundCount)
	}
}
