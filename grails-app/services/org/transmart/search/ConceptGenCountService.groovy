package org.transmart.search

import org.transmart.SearchFilter;

class ConceptGenCountService {
	
	def int getCount(SearchFilter searchFilter){
		
		if (searchFilter == null) return 0
		if (searchFilter.searchText == null) return 0

        def searchText = searchFilter.searchText
		
		def value = 0;
		
		// log.info("ConceptGenCountService: searchText = " + searchText)

        StringBuffer stringBuffer = new StringBuffer();
        try
        {
			def urlString = "http://conceptgen.ncibi.org/ConceptWeb/conceptservice?type=count&search=" + URLEncoder.encode(searchText, "UTF-8")
			// log.info("ConceptGenCountService: urlString = " + urlString)
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
