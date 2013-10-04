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
  

import groovy.time.*

import org.transmart.SearchFilter
import org.transmart.SearchNCIBIResult
import org.transmart.search.ConceptGenCountService
import org.transmart.search.Gene2MeshService
import org.transmart.search.Metab2MeshService
import org.transmart.search.MetscapeService

/**
  * $Id: SearchService.groovy 10098 2011-10-19 18:39:32Z mmcduffie $
  * @author $Author: mmcduffie $
  * @version $Revision: 10098 $
  *
  */
public class SearchNcibiService{
	
	def metscapeService
	def conceptGenCountService
	def gene2MeshService
	def metab2MeshService

	def doResultCount(SearchNCIBIResult sResult, SearchFilter searchFilter){
		
		// Closure to measure the time performance
		def benchmark = { closure ->
			def start = new Date()
			closure.call()
			return TimeCategory.minus(new Date(), start)
		}
	
<<<<<<< HEAD
		def duration = 0;
=======
//		duration = benchmark {sResult.documentCount = documentService.documentCount(searchFilter)}
//		log.info("Document Count Duration: ${duration}")

		sResult.litJubOncAltCount = 0
		sResult.litJubOncInhCount = 0
		sResult.litJubOncIntCount = 0
		sResult.litJubAsthmaAltCount = 0
		sResult.litJubAsthmaInhCount = 0
		sResult.litJubAsthmaIntCount = 0
		sResult.litJubAsthmaPECount = 0
		sResult.experimentCount = 0
		sResult.trialCount = 0
		sResult.analysisCount = 0
		sResult.mRNAAnalysisCount = 0
		sResult.allAnalysiCount = 0
		sResult.documentCount = 0
		sResult.profileCount = 0
		sResult.documentCount = 10
//		duration = benchmark {sResult.documentCount = documentService.documentCount(searchFilter)}
//		log.info("Document Count Duration: ${duration}")
		sResult.metScapeCount = 91
//		duration = benchmark {sResult.documentCount = documentService.documentCount(searchFilter)}
//		log.info("Document Count Duration: ${duration}")
		sResult.conceptCount = 92
//		duration = benchmark {sResult.documentCount = documentService.documentCount(searchFilter)}
//		log.info("Document Count Duration: ${duration}")
		sResult.gene2MeshCount = 93
//		duration = benchmark {sResult.documentCount = documentService.documentCount(searchFilter)}
//		log.info("Document Count Duration: ${duration}")
		sResult.metab2MeshCount = 94
//		duration = benchmark {sResult.documentCount = documentService.documentCount(searchFilter)}
//		log.info("Document Count Duration: ${duration}")
		
>>>>>>> 120b25ed84f19d9c481d4cc0f1366d5c61a3bf07
		
		duration = benchmark {sResult.metscapeCount = metscapeService.getCount(searchFilter)}
		log.info("Metscape Count Duration: ${duration}")

		duration = benchmark {sResult.conceptGenCount = conceptGenCountService.getCount(searchFilter)}
		log.info("Metscape Count Duration: ${duration}")

		duration = benchmark {sResult.gene2MeshCount = gene2MeshService.getCount(searchFilter)}
		log.info("Metscape Count Duration: ${duration}")

		duration = benchmark {sResult.metab2MeshCount = metab2MeshService.getCount(searchFilter)}
		log.info("Metscape Count Duration: ${duration}")
	}

	def createPagingParamMap(params, defaultmax, defaultoffset){
		def paramMap =[:]
		def max = params.max
		def offset = params.offset
		if(max==null && defaultmax!=null)
			max=defaultmax
		if(offset==null&&defaultoffset!=null)
			offset=defaultoffset
			// dynamic typing sucks here..
		if(max!=null)
			paramMap["max"]=Integer.valueOf(String.valueOf(max))
		if(offset!=null)
			paramMap["offset"]=Integer.valueOf(String.valueOf(offset))
		return paramMap;
	}
}