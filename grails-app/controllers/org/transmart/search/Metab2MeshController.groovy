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
  

package org.transmart.search

import org.transmart.searchapp.SearchKeyword

class Metab2MeshController {
	def metab2MeshService
	def index = {
		def m2mResultList = []
		def searchTerms = []
		for (SearchKeyword keyword: session.searchFilter.globalFilter.getAllFilters())
		{
			if(keyword != null & (keyword.dataCategory == 'TEXT' | keyword.dataCategory == 'DISEASE')) 
			{
				searchTerms.add(keyword.keyword)
			}
		}
		if (searchTerms[0] != null)
		{
			m2mResultList = metab2MeshService.getM2MResults(searchTerms[0], 'search')
		}
		render(view: "index", model:[m2mResultList: m2mResultList])
	}
}
