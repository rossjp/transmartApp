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
  

package transmart

import org.transmart.searchapp.SearchKeyword
import org.transmart.conceptgen.module.DataAssembler

class ConceptExplorerController {

    def index = { 

	}
	
	def graph = {
	
	}
	
	def search = {
		def conceptKeyword = "test222"
		for(SearchKeyword keyword: session.searchFilter.globalFilter.getAllFilters()) {
			conceptKeyword = keyword.keyword
		}
		
		render(view: "search", model:[conceptKeyword:conceptKeyword, taxid:9606])
	}
	
	def graph2 = {
		render(view: "graph2",
			model:[cids:params?.cids, geneids:params?.geneids, taxid:params?.taxid, networktype:params?.networktype])
	}
}
