package org.transmart.search

import org.transmart.SearchFilter;

class ConceptGenCountService {
	
	def int getCount(SearchFilter searchFilter){

        def searchText = searchFilter.searchText

        StringBuffer stringBuffer = new StringBuffer();
        try
        {
            u = new URL("http://conceptgen.ncibi.org/ConceptWeb/conceptservice?type=count&search=" + URLEncoder.encode(searchText, "UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
            String line = "";
            int lineNum = 0;

            while ((line = br.readLine()) != null)
            {
                if (lineNum != 0)
                {
                    stringBuffer.append(line);
                }
                lineNum++;
            }

            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return Integer.parseInt(stringBuffer.toString());
    }
}
