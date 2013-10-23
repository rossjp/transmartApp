package org.transmart.search

import org.transmart.SearchFilter
import org.transmart.searchapp.SearchKeyword
import org.transmart.searchapp.AuthUser

class ConceptGenCountService {
	
	def springSecurityService
	
	def int getCount(SearchFilter searchFilter){
		
		log.info("In ConceptGenCountService: getCount")
		
		if (searchFilter == null) return 0
		if (searchFilter.searchText == null) return 0

        def searchText = searchFilter.searchText
        def urlString = "";
        StringBuffer stringBuffer = new StringBuffer();

		def user = AuthUser.findByUsername(springSecurityService.getPrincipal().username)

        for (SearchKeyword keyword: searchFilter.globalFilter.getAllFilters())
        {
            if(keyword != null)
            {
//				log.info("In ConceptGenCountService: category: " + keyword.dataCategory)
				
                if (keyword.dataCategory == 'GENE')
                {
//					log.info("In ConceptGenCountService: user: " + user)
                    if ((user == null) || (user=="") || (user.toString().contains('guest')))
                    {
//						log.info("In ConceptGenCountService: guest user")
                        urlString = "http://conceptgen.ncibi.org/ConceptWeb/cts?qt=count&st=" + URLEncoder.encode(searchText, "UTF-8")
                    }
                    else
                    {
//						log.info("In ConceptGenCountService: private user")
                        urlString = "http://conceptgen.ncibi.org/ConceptWeb/cts?qt=countPrivate&st=" + URLEncoder.encode(searchText, "UTF-8")
                    }
                }
                else
                {
//					log.info("In ConceptGenCountService: not a GENE")
                    urlString = "http://conceptgen.ncibi.org/ConceptWeb/conceptservice?type=count&search=" + URLEncoder.encode(searchText, "UTF-8")
                }
            }

        }
		
		def value = 0;
		
//		log.info("ConceptGenCountService: searchText = " + searchText)
//		log.info("URL: " + urlString)

        try
        {
            def u = new URL(urlString)
            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
            String line = "";
            int lineNum = 0;

            while ((line = br.readLine()) != null)
            {
				log.info("Line (" + lineNum + "): " + line)

                if (lineNum != 0)
                {
                    stringBuffer.append(line)
                }
                lineNum++
            }

            br.close()
			
			value = Integer.parseInt(stringBuffer.toString().trim())
        }
        catch (Exception e)
        {
            e.printStackTrace()
        }

//		log.info("ConceptGenCountService: count = " + value)

		return value
    }
}
