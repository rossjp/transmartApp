package org.ncibi.metab.ws.encoder.json

import org.codehaus.groovy.grails.web.json.JSONObject
import org.ncibi.metab.network.edge.MetabolicEdge
import org.ncibi.metab.network.attribute.EnzymeReactionAttribute
import org.ncibi.metab.network.edge.EdgeType
import org.ncibi.metab.network.edge.EnzymeReactionEdge
import org.ncibi.metab.network.edge.ReactionEdge

class MetabolicEdgeJSONObject extends JSONObject {
	
	MetabolicEdgeJSONObject(MetabolicEdge edge) {
		def id
		def label = ""
		if(edge.getType() == EdgeType.REACTION) {
			id = ReactionEdge.getRid(edge)
			label = id
		}
		else if(edge.getType() == EdgeType.ENZYME_REACTION) {
			id = EnzymeReactionEdge.getEnzymeReactionEdgeAttribute(edge, EnzymeReactionAttribute.ECNUMS) + " - " +
					EnzymeReactionEdge.getEnzymeReactionEdgeAttribute(edge, EnzymeReactionAttribute.RIDS)
		}
		else {
			id = edge.getSource().getType().toDisplayName() + "-" +
			edge.getTarget().getType().toDisplayName()
		}
		id = edge.getSource().getId() + " (" + id + ") " + edge.getTarget().getId()
		put("id", id)
		put("label", label)
		put("source", edge.getSource().getId())
		put("target", edge.getTarget().getId())
		put("Type", edge.getType().toDisplayName())
		put("direction", edge.getDirection().toDirectionAttribute())
		for(String key: edge.getAttributes().keySet()) {
			put(key,edge.getAttributes().get(key))
		}
	}

}
