package org.ncibi.metab.ws.encoder.json

import org.ncibi.metab.network.MetabolicNetwork
import org.ncibi.ws.Response
import org.codehaus.groovy.grails.web.json.JSONObject


class MetabolicNetworkResponseJSONObject extends JSONObject {
	
	MetabolicNetworkResponseJSONObject(Response<MetabolicNetwork> response, Collection<String> cids, Collection<Integer> geneids) {
		def responseValue = new MetabolicNetworkJSONObject(response.getResponseValue(), cids, geneids)
		put("responseValue", responseValue)
		def success = response.isSuccess()
		put("success", success)
	}

}
