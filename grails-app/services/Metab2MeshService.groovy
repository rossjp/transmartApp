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

import org.transmart.searchapp.M2MResult;

import java.awt.print.Printable;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.xpath.*;

class Metab2MeshService {

    boolean transactional = true

    def getDescriptorsByCompoundName(String compoundName) {
		def descList = []
		try {
			String urlString = "http://metab2mesh.ncibi.org/fetch?compound=" + compoundName;
			URL ncbiWS = new URL(urlString);
			URLConnection urlConnection = ncbiWS.openConnection();
			InputStream inputStream = urlConnection.getInputStream();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setCoalescing(true);
			factory.setNamespaceAware(true);

			Document xmlDocument = factory.newDocumentBuilder().parse(inputStream);

			inputStream.close();

			if (xmlDocument != null) {
				XPath xpath = XPathFactory.newInstance().newXPath();

				String expression = "//MeSH/Descriptor/Name";

				NodeList nodes = (NodeList)xpath.evaluate(expression, xmlDocument, XPathConstants.NODESET);

				for (int i = 0; i < nodes.getLength(); i++) {
					Node m2mCompoundNode = nodes.item(i);
					descList.add(m2mCompoundNode.getTextContent());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return descList;
    }
	
	def getCompoundsByDescriptorName(String descriptorName) {
		def compoundList = []
		try {
			String urlString = "http://metab2mesh.ncibi.org/fetch?mesh=" + descriptorName;
			URL ncbiWS = new URL(urlString);
			URLConnection urlConnection = ncbiWS.openConnection();
			InputStream inputStream = urlConnection.getInputStream();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setCoalescing(true);
			factory.setNamespaceAware(true);

			Document xmlDocument = factory.newDocumentBuilder().parse(inputStream);

			inputStream.close();

			if (xmlDocument != null) {
				XPath xpath = XPathFactory.newInstance().newXPath();

				String expression = "//Compound/Name";

				NodeList nodes = (NodeList)xpath.evaluate(expression, xmlDocument, XPathConstants.NODESET);

				for (int i = 0; i < nodes.getLength(); i++) {
					Node m2mCompoundNode = nodes.item(i);
					compoundList.add(m2mCompoundNode.getTextContent());
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return compoundList;
	}
	
	def getM2MResultsByCompound(String compoundName)
	{
		def results = []
		try {
			String urlString = "http://metab2mesh.ncibi.org/fetch?compound=" + compoundName;
			URL ncbiWS = new URL(urlString);
			URLConnection urlConnection = ncbiWS.openConnection();
			InputStream inputStream = urlConnection.getInputStream();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setCoalescing(true);
			factory.setNamespaceAware(true);

			Document xmlDocument = factory.newDocumentBuilder().parse(inputStream);

			inputStream.close();

			results = m2mXmlToM2mResult(xmlDocument);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	def getM2MResultsByDescriptor(String descriptorName)
	{
		def results = []
		try {
			String urlString = "http://metab2mesh.ncibi.org/fetch?mesh=" + descriptorName;
			URL ncbiWS = new URL(urlString);
			URLConnection urlConnection = ncbiWS.openConnection();
			InputStream inputStream = urlConnection.getInputStream();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setCoalescing(true);
			factory.setNamespaceAware(true);

			Document xmlDocument = factory.newDocumentBuilder().parse(inputStream);

			inputStream.close();

			results = m2mXmlToM2mResult(xmlDocument);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	def m2mXmlToM2mResult (Document xmlDocument)
	{
		def results = []
		if (xmlDocument != null) {
			XPath xpath = XPathFactory.newInstance().newXPath();

			String expression = "//Result";

			NodeList nodes = (NodeList)xpath.evaluate(expression, xmlDocument, XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++) {
				
				Node m2mNode = nodes.item(i);
				
				String cName = (String)xpath.evaluate("Compound/Name/text()", m2mNode, XPathConstants.STRING);
				String cid = (String)xpath.evaluate("Compound/CompoundID/text()", m2mNode, XPathConstants.STRING);
				String dName = (String)xpath.evaluate("MeSH/Descriptor/Name/text()", m2mNode, XPathConstants.STRING);
				String did = (String)xpath.evaluate("MeSH/Descriptor/Identifier/text()", m2mNode, XPathConstants.STRING);
				Double fover = Double.valueOf(xpath.evaluate("Fover/text()", m2mNode, XPathConstants.STRING));
				Double chiSquare = Double.valueOf(xpath.evaluate("ChiSquare/text()", m2mNode, XPathConstants.STRING));
				Double fisherExact = Double.valueOf(xpath.evaluate("FisherExact/text()", m2mNode, XPathConstants.STRING));
				Double qValue = Double.valueOf(xpath.evaluate("Q-Value/text()", m2mNode, XPathConstants.STRING));
				
				results.add(new M2MResult(cName, cid, dName, did, fover, chiSquare, fisherExact, qValue));
			}
			
				
		}
		return results;
	}
}
