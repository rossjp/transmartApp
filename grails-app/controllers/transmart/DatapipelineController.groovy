/*************************************************************************
 * tranSMART - translational medicine data mart
 * 
 * Copyright 2008-2012 Janssen Research & Development, LLC.
 * 
 * This product includes software developed at Janssen Research & Development, LLC.
 * 
 * This" program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
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

import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.transmart.pipeline.DatasetParser;
import org.transmart.pipeline.model.Sample;

class DatapipelineController {

    def index = { }
	
	def dataUpload = { 
		
		def gse, description, username, sampleSize, probesetIdSize, dataSize;
		
		
		if(request)
		{
			MultipartHttpServletRequest mpr = (MultipartHttpServletRequest)request;
			gse = mpr.getParameter("gse");
			description = mpr.getParameter("description");
			username = mpr.getParameter("username");
			
			CommonsMultipartFile uploadedFile = (CommonsMultipartFile)mpr.getFile("uploadFile");
			if(!uploadedFile.empty){
				println "helloworld"
				println "Class: ${uploadedFile.class}"
				println "Name: ${uploadedFile.name}"
				println "OriginalFileName: ${uploadedFile.originalFilename}"
				println "Size: ${uploadedFile.size}"
				println "ContentType: ${uploadedFile.contentType}"
				def fileIn = "/Users/vmahavis/data/${uploadedFile.originalFilename}";

				uploadedFile.transferTo(new File(fileIn));
				DatasetParser dp = new DatasetParser(fileIn);
				List<Sample> sampleList = dp.getSampleList();
				
				sampleSize = sampleList.size();
				probesetIdSize = dp.getProbeIdList().size();
				dataSize = dp.getDataCount();

			}
			else{
				flash.message = "file cannot be empty"
			}

		}
		
		render(view: "dataUpload", model:[gse:gse, description:description, username:username, sampleSize:sampleSize, dataSize:dataSize, probesetIdSize:probesetIdSize])
		
		
	}
	
	
}
