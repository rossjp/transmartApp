/*************************************************************************
  * tranSMART - translational medicine data mart
 * 
 * Copyright 2008-2012 Janssen Research & Development, LLC.
 * 
 * This product includes software developed at Janssen Research & Development, LLC.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software  * Foundation, either version 3 of the License, or (at your option) any later version, along with the following terms:
 * 1.	You may convey a work based on this program in accordance with section 5, provided that you retain the above notices.
 * 2.	You may convey verbatim copies of this program code as you receive it, in any medium, provided that you retain the above notices.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS    * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *
 ******************************************************************/
package org.transmart.serach

import grails.converters.JSON

import org.ncibi.metab.network.NetworkType
import org.ncibi.metab.ws.client.MetabolicNetworkService
import org.ncibi.metab.ws.encoder.json.MetabolicNetworkResponseJSONObject
import org.ncibi.ws.HttpRequestType
import org.transmart.searchapp.SearchKeyword




class MetScapeController {

    def index = {
		render(view: "inputOptions")
	}
	
	def gene = {
		def geneids = ""
		for(SearchKeyword keyword: session.searchFilter.globalFilter.getGeneFilters()) {
			geneids += keyword.uniqueId.replaceAll("[\\D]", "")
		}
		render(view: "graph",
			model:[cids:"", geneids:geneids, taxid:9606, networktype:"CREG"])
	}
	
	def graph = {
		render(view: "graph", 
			model:[cids:params?.cids, geneids:params?.geneids, taxid:params?.taxid, networktype:params?.networktype])
	}
	
	def network = {
		def cids = params?.cids?.toUpperCase()?.tokenize(" " as String)
		def geneids = params?.geneids?.tokenize(" " as String)
		def networktype = NetworkType.toNetworkType(params?.networktype) ?: NetworkType.CREG
		def taxid = params?.taxid?.toInteger() ?: 9606
		def service = new MetabolicNetworkService(HttpRequestType.POST, null)
		def serverResponse = service.retrieveNetworkOfTypeForCidsAndGeneids(networktype, cids, geneids,
			taxid)
		def jsonObject = new MetabolicNetworkResponseJSONObject(serverResponse, cids, geneids);
		render(contentType:"application/json", text: jsonObject.toString())
	}
}
