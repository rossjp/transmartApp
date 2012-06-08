package org.ncibi.metab.ws.encoder.json

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.ncibi.metab.network.MetabolicNetwork
import org.ncibi.metab.network.edge.MetabolicEdge
import org.ncibi.metab.network.node.CompoundNode
import org.ncibi.metab.network.node.GeneNode
import org.ncibi.metab.network.node.MetabolicNode

class MetabolicNetworkJSONObject extends JSONObject {

	MetabolicNetworkJSONObject(MetabolicNetwork network, Collection<String> cids, Collection<Integer> geneids) {
		def dataSchema = new JSONObject()
		put("dataSchema", dataSchema)
		def nodesSchema = new JSONArray()
		def nodesSchemaSet = [] as Set
		dataSchema.put("nodes", nodesSchema)
		def edgesSchema = new JSONArray()
		def edgesSchemaSet = [] as Set
		dataSchema.put("edges", edgesSchema)
		
		def data = new JSONObject()
		put("data", data)
		def nodes = new JSONArray()
		data.put("nodes", nodes)
		def edges = new JSONArray()
		data.put("edges", edges)
		
		for(MetabolicNode node: network.getAllNodes()) {
			MetabolicNodeJSONObject nodeJson = new MetabolicNodeJSONObject(node)
			def category = nodeJson.optString("Type", "")
			if(cids?.contains(CompoundNode.getCid(node)) || geneids?.contains(GeneNode.getOrganismGeneid(node)?.toString())) {
				category = "Input " + category
			}
			nodeJson.put("Category", category)
			nodes.put(nodeJson)
			nodesSchemaSet.addAll(nodeJson.keySet())
		}
		
		for(MetabolicEdge edge: network.getEdges()) {
			MetabolicEdgeJSONObject edgeJson = new MetabolicEdgeJSONObject(edge)
			edges.put(edgeJson)
			edgesSchemaSet.addAll(edgeJson.keySet())
		}
		
		for(String attr: nodesSchemaSet) {
			def nodesSchemaEntry = new JSONObject()
			nodesSchemaEntry.put("name", attr)
			nodesSchemaEntry.put("type","object")
			nodesSchema.put(nodesSchemaEntry)
		}
		for(String attr: edgesSchemaSet) {
			def edgesSchemaEntry = new JSONObject()
			edgesSchemaEntry.put("name", attr)
			edgesSchemaEntry.put("type","object")
			edgesSchema.put(edgesSchemaEntry)
		}
	}
}
