package org.transmart.pipeline;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.transmart.pipeline.model.Dataset;
import org.transmart.pipeline.model.Sample;

/**
 * @author Vasudeva Mahavisno
 * @email vmahavis@gmail.com
 */
public class DatasetParser implements PipelineConstant
{
	private String initalUploadStatus = "Initial Upload";
	private String geoDatasource = "GEO";
	private FileReader reader = new FileReader();
	private TextParser tp = new TextParser();
	private String fileName;

	private Dataset datasetInfo;
	private List<Sample> sampleList;
	private ArrayList<String> probeIdList;
	private List<String> errorReport;
	private int dataCount;

	private double[][] data;

	public DatasetParser(String fileName)
	{
		this.fileName = fileName;
		this.errorReport = new ArrayList<String>();
		this.dataCount = 0;
		getDataset();
		getSamples();
		getDatasetProbeId();
		getDatasetValues(probeIdList.size(), sampleList.size());
	}

	private void getDataset()
	{
		System.out.println("GETTING DATASET INFO");
		ArrayList<String> searchTerms = new ArrayList<String>();
		searchTerms.add("Series_title");
		searchTerms.add("Series_pubmed_id");
		searchTerms.add("Series_geo_accession");
		searchTerms.add("Series_summary");
		searchTerms.add("Series_submission_date");
		searchTerms.add("Series_type");
		searchTerms.add("Series_contact_name");
		searchTerms.add("Series_contact_email");
		searchTerms.add("Series_platform_id");

		HashMap<String, String> map = reader.readFileMap(fileName, searchTerms, TAB_DELIMITER, true);

		Dataset d = new Dataset();
		d.setContactEmail(map.get("Series_contact_email"));
		d.setContactName(map.get("Series_contact_name"));
		d.setDatasetName(map.get("Series_title"));
		d.setDatasourceId(map.get("Series_geo_accession"));
		d.setDescription(map.get("Series_summary"));
		d.setPlatformId(map.get("Series_platform_id"));
		d.setPubmedId(map.get("Series_pubmed_id"));
		d.setStatus(initalUploadStatus);
		d.setDatasource(geoDatasource);
		d.setSubmissionDate(new Date());

		setDatasetInfo(d);
	}

	private void getSamples()
	{
		System.out.println("GETTING SAMPLES");
		Sample s;
		List<Sample> list = new ArrayList<Sample>();
		ArrayList<String> searchTerms = new ArrayList<String>();
		searchTerms.add("Sample_title");
		searchTerms.add("Sample_geo_accession");
		searchTerms.add("Sample_type");

		HashMap<String, String> map = reader.readFileMapNoDelimit(fileName, searchTerms);

		String sTmp = map.get("Sample_title");
		sTmp = sTmp.replaceAll("\"", "");
		sTmp = tp.removeSpecialChars(sTmp, REMOVE_SPECIAL_CHARS, "").trim();

		String sgTmp = map.get("Sample_geo_accession");
		sgTmp = sgTmp.replaceAll("\"", "");
		sgTmp = tp.removeSpecialChars(sgTmp, REMOVE_SPECIAL_CHARS, "").trim();

		String stTmp = map.get("Sample_type");
		stTmp = stTmp.replaceAll("\"", "");
		stTmp = tp.removeSpecialChars(stTmp, REMOVE_SPECIAL_CHARS, "").trim();

		String[] sampleName = sTmp.split("\t");
		String[] sampleAccession = sgTmp.split("\t");
		String[] sampleType = stTmp.split("\t");

		if (sampleName.length != sampleAccession.length || sampleName.length != sampleType.length
				|| sampleAccession.length != sampleType.length)
		{
			errorReport.add(MISSING_SAMPLE_INFO);
		}

		for (int i = 0; i < sampleName.length; i++)
		{
			s = new Sample();
			s.setSampleName(sampleName[i]);
			s.setSampleType(sampleType[i]);
			s.setDatasourceSampleId(sampleAccession[i]);
			list.add(s);
		}

		setSampleList(list);
	}

	private void getDatasetProbeId()
	{
		System.out.println("GETTING PROBESET IDS");
		try
		{
			boolean startRead = false;
			boolean isHeader = true;
			ArrayList<String> probeId = new ArrayList<String>();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{
				if (strLine.startsWith(GEO_DATA_STARTKEY))
				{
					startRead = true;
				}
				else if (startRead && !strLine.startsWith(GEO_DATA_ENDKEY))
				{
					if (isHeader)
					{
						isHeader = false;
					}
					else
					{
						String[] value = strLine.split(TAB_DELIMITER);
						probeId.add(value[0]);
					}
				}
			}
			in.close();
			setProbeIdList(probeId);
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

	private void getDatasetValues(int probeIdSize, int sampleSize)
	{
		System.out.println("GETTING DATASET VALUES");
		data = new double[probeIdSize][sampleSize];

		try
		{
			boolean startRead = false;
			boolean isHeader = true;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int row = 0;

			while ((strLine = br.readLine()) != null)
			{
				if (strLine.startsWith(GEO_DATA_STARTKEY))
				{
					startRead = true;
				}
				else if (startRead && (!strLine.startsWith(GEO_DATA_ENDKEY)))
				{
					if (isHeader)
					{
						isHeader = false;
					}
					else
					{
						//System.out.println(strLine);
						String[] values = strLine.split(TAB_DELIMITER);
						
						if ((values.length -1) != sampleSize)
						{
							errorReport.add(MISSING_DATA_VALUES);
						}
						else
						{
							for (int i = 0; i < sampleSize; i++)
							{
								try
								{
									double v = Double.parseDouble(values[i + 1]);
	
									data[row][i] = v;
									dataCount++;
	
								}
								catch (NumberFormatException e)
								{
									data[row][i] = MISSING_VALUE_KEY;
								}
							}
						}
						row++;
					}
				}
			}
			in.close();
		}
		catch (IOException e)
		{
			System.out.println("Error: " + e.getMessage());
		}	
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	public double[][] getData()
	{
		return data;
	}

	public void setData(double[][] data)
	{
		this.data = data;
	}

	public Dataset getDatasetInfo()
	{
		return datasetInfo;
	}

	public void setDatasetInfo(Dataset datasetInfo)
	{
		this.datasetInfo = datasetInfo;
	}

	public List<Sample> getSampleList()
	{
		return sampleList;
	}

	public void setSampleList(List<Sample> sampleList)
	{
		this.sampleList = sampleList;
	}

	public ArrayList<String> getProbeIdList()
	{
		return probeIdList;
	}

	public void setProbeIdList(ArrayList<String> probeIdList)
	{
		this.probeIdList = probeIdList;
	}

	public List<String> getErrorReport()
	{
		return errorReport;
	}

	public void setErrorReport(List<String> errorReport)
	{
		this.errorReport = errorReport;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
}
