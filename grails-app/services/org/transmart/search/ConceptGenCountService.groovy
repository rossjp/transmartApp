package org.transmart.search

import org.transmart.SearchFilter
import org.transmart.searchapp.SearchKeyword;

class ConceptGenCountService {
	
	def int getCount(SearchFilter searchFilter){
		
		if (searchFilter == null) return 0
		if (searchFilter.searchText == null) return 0

        def searchText = searchFilter.searchText
        def urlString = "";
        StringBuffer stringBuffer = new StringBuffer();



        for (SearchKeyword keyword: searchFilter.globalFilter.getAllFilters())
        {
            if(keyword != null)
            {
                if (keyword.dataCategory == 'GENE')
                {
                    if ((user == null) || (user=="") || (user == 'guest'))
                    {
                        urlString = "http://conceptgen.ncibi.org/ConceptWeb/conceptservice?type=count&search=" + URLEncoder.encode(searchText, "UTF-8")
                    }
                    else
                    {
                        urlString = "http://conceptgen.ncibi.org/ConceptWeb/conceptservice?type=countPrivate&search=" + URLEncoder.encode(searchText, "UTF-8")
                    }
                }
                else
                {
                    urlString = "http://conceptgen.ncibi.org/ConceptWeb/conceptservice?type=count&search=" + URLEncoder.encode(searchText, "UTF-8")
                }
            }

        }
		
		def value = 0;
		
		// log.info("ConceptGenCountService: searchText = " + searchText)


        try
        {
            def u = new URL(urlString)
            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
            String line = "";
            int lineNum = 0;

            while ((line = br.readLine()) != null)
            {
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

        return value
    }
}
