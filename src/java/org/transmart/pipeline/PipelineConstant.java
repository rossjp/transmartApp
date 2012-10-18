package org.transmart.pipeline;

public interface PipelineConstant {
	
	public final String TAB_DELIMITER = "\t";
	public final String NEW_LINE = "\n";
	public final String ID_LABEL = "ID_REF";
	public String[] REMOVE_SPECIAL_CHARS = { "\"", "\\!", "\\;" };

	public String GEO_DATA_STARTKEY = "!series_matrix_table_begin";
	public String GEO_DATA_ENDKEY = "!series_matrix_table_end";

	public double MISSING_VALUE_KEY = 99999999;

	public final String MISSING_DATA_VALUES = "Missing values in dataset";
	public final String MISSING_SAMPLE_INFO = "Missing sample info";
}
