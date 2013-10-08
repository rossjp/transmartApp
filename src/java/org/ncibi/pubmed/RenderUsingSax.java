package org.ncibi.pubmed;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RenderUsingSax {
	
	private static final String BASE_URL = "http://nlp.ncibi.org/fetch";
	
	private List <Abstract> resultList = new ArrayList<Abstract>();
	private int inputValueLimit = 0;

	@SuppressWarnings("deprecation")
	private static String makeURLString(int geneId, int limit) {
		String geneIdString = "" + geneId;
		return BASE_URL + "?tagger=nametagger&type=gene&id=" + URLEncoder.encode(geneIdString)
				+ "&limit=" + limit;
	}
	
	public void processDocument(int geneId, int limit) throws Exception {
		inputValueLimit = limit;
		resultList = parseToResutlList(geneId, limit);
	}
	
	public String getHtmlResults() {
		
		int limit = inputValueLimit;
		
		// make formatted XML output		
		StringBuffer outBuffer = new StringBuffer();
		int outCount = 0;
		outBuffer.append("<div class='document'>");
		for (Abstract a : resultList) {
			if (++outCount > limit) break;
			outBuffer.append("\n" + a.toHtml());
		}
		outBuffer.append("\n</div>\n");
		
		return outBuffer.toString();		
	}
	
	public Object[] getArrayResults() {
		
		int limit = inputValueLimit;
		
		// make array of hash-table values (for Grails)
		ArrayList<Map<String,String>> holder = new ArrayList<Map<String,String>>();
		int outCount = 0;
		for (Abstract a: resultList) {
			if (++outCount > limit) break;
			holder.add(a.toMap());
		}
		return (Object[]) holder.toArray();
	}

	private List<Abstract> parseToResutlList(int geneId, int limit) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		SAXListAbstractsHandler handler = new SAXListAbstractsHandler();
		String urlString = RenderUsingSax.makeURLString(geneId, limit);
		URL ncibiWS = new URL(urlString);
		URLConnection urlConnection = ncibiWS.openConnection();
		InputStream inputStream = urlConnection.getInputStream();

		parser.parse(inputStream,handler);
		
		// for debugging!
//		System.out.println(urlString);
//		System.out.println("Count = " + handler.count);
//		System.out.println("Limit = " + limit);

		return handler.list;
	}
	
	private class SAXListAbstractsHandler extends DefaultHandler{
		List<Abstract> list = new ArrayList<Abstract>();
		
		private Abstract currentAbstract;
		private StringBuffer paragraph;
		private String content= "";

		@Override
		// Triggered when the start of tag is found.
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			if (qName.equals("Article")) {
				currentAbstract = new Abstract();
				currentAbstract.pmidString = attributes.getValue("pmid");
			} else if (qName.equals("Paragraph")) {
				paragraph = new StringBuffer();
				paragraph.append("\n");
			} else if (qName.equals("Gene"))
			{
				paragraph.append(content);
				content="";
				String id = attributes.getValue("id");
				paragraph.append(" <a class='gene' href='" + makeHref(id) +"'>");
			}
		}

		private String makeHref(String id) {
			return "pubmedSite?geneid=" + id;
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			
			if (qName.equals("Result")) {
				paragraph.append(content);
				content="";
				currentAbstract.html = paragraph.toString();
				list.add(currentAbstract);
			} else if (qName.equals("Sentence")) {
				paragraph.append(content);
				content="";
			} else if (qName.equals("Paragraph")) {
				paragraph.append(content);
				content="";
				paragraph.append("\n");
			} else if (qName.equals("Gene")) {
				paragraph.append(content);
				content="";
				paragraph.append("</a> ");				
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			content = String.copyValueOf(ch, start, length).trim();
		}

	}

	
	class Abstract {
		String pmidString = "";
		String html = "";
		
		public Map<String, String> toMap() {
			Map<String, String> ret = new HashMap<String, String>();
			ret.put("pmid", pmidString);
			ret.put("pargraph", html);
			return ret;
		}
		
		public String toHtml() { 
			return "<div class='abstract' pmid='" + pmidString + "'>\n" 
			 + "<div class='paragraph'>"
			 + html + "</div></div>";
		}
	}
}
