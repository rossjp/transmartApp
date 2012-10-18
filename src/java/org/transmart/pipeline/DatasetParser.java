package org.transmart.pipeline;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.transmart.pipeline.model.Sample;


public class DatasetParser implements PipelineConstant {
	private String fileName;

	private List<Sample> sampleList;
	private ArrayList<String> probeIdList;

	private double[][] data;

	public void DataParser(String fileName)
	{
		this.fileName = fileName;
		getDataInfo();
		getDatasetValues(probeIdList.size(), sampleList.size());
	}

	private void getDataInfo()
	{
		System.out.println("GETTING PROBESET IDS");
		try
		{
			boolean isHeader = true;
			ArrayList<String> probeId = new ArrayList<String>();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			Sample s;
			List<Sample> list = new ArrayList<Sample>();

			while ((strLine = br.readLine()) != null)
			{
				if (isHeader)
				{
					isHeader = false;
					String[] sampleName = strLine.split(TAB_DELIMITER);

					for (int i = 1; i < sampleName.length; i++)
					{
						s = new Sample();
						s.setSampleName(sampleName[i]);
						s.setSampleType("na");
						s.setDatasourceSampleId("na");
						list.add(s);
					}
				}
				else
				{
					String[] value = strLine.split(TAB_DELIMITER);
					probeId.add(value[0]);
				}

			}
			in.close();
			setSampleList(list);
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
			boolean isHeader = true;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int row = 0;

			while ((strLine = br.readLine()) != null)
			{
				if (isHeader)
				{
					isHeader = false;
				}
				else
				{
					String[] values = strLine.split(TAB_DELIMITER);

					for (int i = 0; i < sampleSize; i++)
					{
						try
						{
							double v = Double.parseDouble(values[i + 1]);

							data[row][i] = v;

						}
						catch (NumberFormatException e)
						{
							data[row][i] = MISSING_VALUE_KEY;
						}
					}
					row++;
				}
			}

			in.close();
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
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

}
