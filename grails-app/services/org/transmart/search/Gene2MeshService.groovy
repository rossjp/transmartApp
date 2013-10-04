package org.transmart.search
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

import org.transmart.SearchFilter;
import org.transmart.searchapp.G2MResult;

import java.awt.print.Printable;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.xpath.*;

class Gene2MeshService {

    boolean transactional = true
	int maxPubs = 200
	
	def getG2MResultsByGene(String geneSymbol)
	{
		def results = []
		try {
			String urlString = "http://gene2mesh.ncibi.org/fetch?genesymbol=" + URLEncoder.encode(geneSymbol);
			URL ncibiWS = new URL(urlString)
			URLConnection urlConnection = ncibiWS.openConnection()
			InputStream inputStream = urlConnection.getInputStream()

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance()
			factory.setCoalescing(true)
			factory.setNamespaceAware(true)

			Document xmlDocument = factory.newDocumentBuilder().parse(inputStream)

			inputStream.close()

			results = g2mXmlToG2MResult(xmlDocument)
			
		} catch(Exception e) {
			e.printStackTrace()
		}
		return results
	}

	def getG2MResultsByDescriptor(String descriptorName)
	{
		def results = []
		try {
			String urlString = "http://gene2mesh.ncibi.org/fetch?mesh=" + URLEncoder.encode(descriptorName)
			URL ncibiWS = new URL(urlString)
			URLConnection urlConnection = ncibiWS.openConnection()
			InputStream inputStream = urlConnection.getInputStream()

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance()
			factory.setCoalescing(true)
			factory.setNamespaceAware(true)
			Document xmlDocument = factory.newDocumentBuilder().parse(inputStream)
			
			inputStream.close()

			results = g2mXmlToG2MResult(xmlDocument)
			
		} catch(Exception e) {
			e.printStackTrace()
		}
		return results
	}
	
	def g2mXmlToG2MResult (Document xmlDocument)
	{
		def results = []
		if (xmlDocument != null) {
			XPath xpath = XPathFactory.newInstance().newXPath()
			String expression = "//Result"
			NodeList nodes = (NodeList)xpath.evaluate(expression, xmlDocument, XPathConstants.NODESET)

			for (int i = 0; i < nodes.getLength(); i++) {
				
				Node g2mNode = nodes.item(i)
				String pmids = ""
				
				NodeList docNodes = (NodeList)xpath.evaluate(".//PMID", g2mNode, XPathConstants.NODESET)
				
				pmids += docNodes.item(0).getTextContent()
				for (int j = 1; j < docNodes.getLength() & j < maxPubs; j++) {
					pmids += ',' + docNodes.item(j).getTextContent()
				}
				
				String geneSymbol = (String)xpath.evaluate("Gene/Symbol/text()", g2mNode, XPathConstants.STRING)
				String geneID = (String)xpath.evaluate("Gene/Identifier/text()", g2mNode, XPathConstants.STRING)
				String geneDescription = (String)xpath.evaluate("Gene/Description/text()", g2mNode, XPathConstants.STRING)
				String dName = (String)xpath.evaluate("MeSH/Descriptor/Name/text()", g2mNode, XPathConstants.STRING)
				String did = (String)xpath.evaluate("MeSH/Descriptor/Identifier/text()", g2mNode, XPathConstants.STRING)
				String didNum = did.replace('D', '68');
				String qualifier = (String)xpath.evaluate("MeSH/Descriptor/text()", g2mNode, XPathConstants.STRING)
				Double fover = Double.valueOf(xpath.evaluate("Fover/text()", g2mNode, XPathConstants.STRING))
				Double chiSquare = Double.valueOf(xpath.evaluate("ChiSquare/text()", g2mNode, XPathConstants.STRING))
				Double fisherExact = Double.valueOf(xpath.evaluate("FisherExact/text()", g2mNode, XPathConstants.STRING))		
				results.add(new G2MResult(geneSymbol, geneID, geneDescription, dName, did, didNum, qualifier, fover, chiSquare, fisherExact, pmids))
			}
			
		}
		return results
	}
	
	public int getCount(SearchFilter searchFilter){
		return 93
	}	

}
