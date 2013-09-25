package org.transmart.conceptgen.module;

import java.util.ArrayList;
import java.util.ResourceBundle;



import org.transmart.conceptgen.db.JDBCExecuter;
import org.transmart.conceptgen.resource.util.TextParser;

public class ConceptJsService {
	private JDBCExecuter rs = new JDBCExecuter();
	private ResourceBundle sql = ResourceBundle
			.getBundle("org.transmart.conceptgen.resource.bundle.sql");
	private TextParser tp = new TextParser();

	// ***********************************************************************************************************************************************
	// PRIVATE

	public String myConceptGeneList(String userid, String password,
			String conceptName) throws Exception {
		String query = sql.getString("getMyConceptGeneList");
		query = query.replaceFirst("\\?", conceptName);
		query = query.replaceFirst("\\?", userid);

		return tp.createJsArray(rs.select(query));
	}

	public String conceptGeneList(String conceptId) throws Exception {
		String query = sql.getString("getConceptIdGeneList");
		query = query.replaceFirst("\\?", conceptId);
		return tp.createJsArray(rs.select(query));
	}

	public String myConcepts(String userid) throws Exception {

		String query = sql.getString("getMyConceptList");
		query = query.replaceFirst("\\?", userid);
		query = query.replaceFirst("\\?", "concept_name");

		return tp.createJsArray(rs.select(query));
	}

	public String getConceptInteractionPrivate(String userid,
			String conceptName, String networkType, double pvalue, double fdr)
			throws Exception {

		String query = "";

		if (networkType.equals("complete")) {
			query = sql.getString("conceptListCompleteInteractionPrivate");
			query = query.replaceFirst("\\?", String.valueOf(fdr));
			query = query.replaceFirst("\\?", String.valueOf(pvalue));
			query = query.replaceFirst("\\?", conceptName);
			query = query.replaceFirst("\\?", userid);
		} else {
			query = sql.getString("conceptListInteractionPrivate");
			query = query.replaceFirst("\\?", String.valueOf(fdr));
			query = query.replaceFirst("\\?", String.valueOf(pvalue));
			query = query.replaceFirst("\\?", conceptName);
			query = query.replaceFirst("\\?", userid);
		}

		return tp.createJsArray(rs.select(query));

	}

	public String getConceptInteractionCountPrivate(String userid,
			String conceptName, double pvalue, double fdr) throws Exception {
		String query = "";
		query = sql.getString("conceptListInteractionCountPrivate");
		query = query.replaceFirst("\\?", String.valueOf(fdr));
		query = query.replaceFirst("\\?", String.valueOf(pvalue));
		query = query.replaceFirst("\\?", conceptName);
		query = query.replaceFirst("\\?", userid);

		return tp.createJsArray(rs.select(query));

	}

	// ***********************************************************************************************************************************************
	// PUBLIC

	public String getConceptDirectInteraction(String conceptId, double pvalue,
			double fdr) throws Exception {
		String query = "";

		query = sql.getString("getConceptListInteraction");
		query = query.replaceFirst("\\?", String.valueOf(fdr));
		query = query.replaceFirst("\\?", String.valueOf(pvalue));
		query = query.replaceFirst("\\?", conceptId);

		return tp.createJsArray(rs.select(query));
	}
	
	public String getConceptDirectInteractionNode(String conceptId, String conceptName, String conceptTypeId, String elementSize, double pvalue,
			double fdr) throws Exception {
		String query = "";

		query = sql.getString("conceptListInteractionNode");
		query = query.replaceFirst("\\?", String.valueOf(fdr));
		query = query.replaceFirst("\\?", String.valueOf(pvalue));
		query = query.replaceFirst("\\?", conceptId);

		ArrayList textArray = rs.select(query);

		//String t =  ",{ id: '"+ conceptId +"', label: 'test', conceptTypeId: '122' , elementSize: '98' }";
		String t =  ",{ id: '"+ conceptId +"', label: '"+ conceptName +"', conceptTypeId: '"+ conceptTypeId +"' , elementSize: '"+ elementSize +"' }";
		//String t = "";
		for (int i = 0; i < textArray.size(); i++)
		{
			ArrayList record = (ArrayList) textArray.get(i);
			StringBuffer text = new StringBuffer();
			
			t += ",{ id: '"+ (String)record.get(0) +"', label: '"+ (String)record.get(1) +"', conceptTypeId: '"+ (String)record.get(2) +"' , elementSize: "+ (String)record.get(3) +" }";
			
		}

		t = "[ " + t.replaceFirst(",", "") + " ]";

		return t;
	}

	public String getConceptDirectInteractionEdge(String conceptId, double pvalue,
			double fdr) throws Exception {
		String query = "";

		query = sql.getString("conceptListInteractionEdge");
		query = query.replaceFirst("\\?", String.valueOf(fdr));
		query = query.replaceFirst("\\?", String.valueOf(pvalue));
		query = query.replaceFirst("\\?", conceptId);
		
		ArrayList textArray = rs.select(query);

		String t = "";
		for (int i = 0; i < textArray.size(); i++)
		{
			ArrayList record = (ArrayList) textArray.get(i);
			StringBuffer text = new StringBuffer();
			
			t += ",{ id: '"+ (String)record.get(0) +"-" + (String)record.get(1) +"', target: '"+ (String)record.get(0) +"', source: '"+ (String)record.get(1) +"' }";	
		}

		t = "[ " + t.replaceFirst(",", "") + " ]";

		return t;
	}
	
	
	
	
	
	public String searchConceptCount(String searchTerm) throws Exception {
		String query = sql.getString("conceptSearchCount");
		query = query.replaceAll("\\?", searchTerm);
		String[] header = { "SearchResultSize" };

		return tp.createTabDelimitedTextArray(rs.select(query), header);
	}

	public String getConceptInteractionCount(String conceptType, String extId,
			double pvalue, double fdr) throws Exception {
		String query = sql.getString("conceptListInteractionCount");
		query = query.replaceFirst("\\?", String.valueOf(fdr));
		query = query.replaceFirst("\\?", String.valueOf(pvalue));
		query = query.replaceFirst("\\?", extId);
		query = query.replaceFirst("\\?", conceptType);

		return tp.createJsArray(rs.select(query));
	}

	public String getConceptTypeSize(String extId, double pvalue, double fdr)
			throws Exception {
		String query = sql.getString("getConceptTypeSize");
		query = query.replaceFirst("\\?", extId);
		query = query.replaceFirst("\\?", String.valueOf(fdr));
		query = query.replaceFirst("\\?", String.valueOf(pvalue));

		return tp.createJsArray(rs.select(query));
	}

	public String searchConcept(String searchTerm, String limit)
			throws Exception {

		String query = sql.getString("conceptSearchLimit");
		query = query.replaceFirst("\\?", searchTerm);
		query = query.replaceFirst("\\?", limit);
		return tp.createJsArray(rs.select(query));
	}

}
