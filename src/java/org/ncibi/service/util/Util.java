package org.ncibi.service.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Util {
	
	public static Document xmlDocumentFrom(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException{

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setCoalescing(true);
		factory.setNamespaceAware(true);

		Document xmlDocument = factory.newDocumentBuilder().parse(inputStream);

		inputStream.close();

		return xmlDocument;
		
	}

}
