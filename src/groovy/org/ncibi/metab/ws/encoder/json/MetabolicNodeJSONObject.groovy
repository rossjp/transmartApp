package org.ncibi.metab.ws.encoder.json

import org.codehaus.groovy.grails.web.json.JSONObject
import org.ncibi.metab.network.node.CompoundNode
import org.ncibi.metab.network.node.EnzymeNode
import org.ncibi.metab.network.node.GeneNode
import org.ncibi.metab.network.node.MetabolicNode
import org.ncibi.metab.network.node.NodeType
import org.ncibi.metab.network.node.ReactionNode

class MetabolicNodeJSONObject extends JSONObject {

	MetabolicNodeJSONObject(MetabolicNode node) {
		def name
		switch(node.getType()) {
			case NodeType.COMPOUND:
				name =  CompoundNode.getName(node)
				break;
			case NodeType.REACTION:
				name =  ReactionNode.getRid(node)
				break;
			case NodeType.ENZYME:
				name =  EnzymeNode.getName(node)
				break;
			case NodeType.GENE:
				name =  GeneNode.getSymbol(node)
				break;
			default:
				name = node.getName()
		}
		put("canonicalName", name)
		put("id", node.getId())
		put("Type", node.getType().toDisplayName())
		for(String key: node.getAttributes().keySet()) {
			put(key,node.getAttributes().get(key))
		}
	}
}
