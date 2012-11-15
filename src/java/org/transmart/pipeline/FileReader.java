package org.transmart.pipeline;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @version 1.0 10 August 2011
 * @author Vasudeva Mahavisno vmahavis@gmail.com
 */

public class FileReader
{
	private String[] regex = { "\"", "\\!" };
	private TextParser tp = new TextParser();

	public ArrayList<String> readFile(String filePath, String searchTerm, String delimiter)
	{
		ArrayList<String> data = new ArrayList<String>();
		try
		{
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null)
			{
				if (strLine.indexOf(searchTerm) > 0)
				{
					String[] value = strLine.split(delimiter);
					data.add(value[1].trim());
				}
			}
			in.close();
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}

		return data;
	}

	public ArrayList<ArrayList<String>> readFileBasic(String filePath, String delimiter, boolean headerOffset)
	{
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		try
		{
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int lineNum = 0;

			if (headerOffset)
			{
				lineNum = 0;
			}

			while ((strLine = br.readLine()) != null)
			{
				if (lineNum != 0)
				{
					String[] value = strLine.split(delimiter);
					ArrayList<String> row = new ArrayList<String>();
					for (int i = 0; i < value.length; i++)
					{
						row.add(value[i].trim());
					}
					list.add(row);
				}
				lineNum++;
			}
			in.close();
		}
		catch (IOException e)
		{
			System.err.println("Error: " + e.getMessage());
		}

		return list;
	}

	public HashMap<String, String> readFileMap(String filePath, ArrayList<String> searchTerms, String delimiter, boolean cleanText)
	{
		HashMap<String, String> data = new HashMap<String, String>();
		try
		{
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String tmp = "";
			String key = "";
			String val = "";
			try
			{
				for (int i = 0; i < searchTerms.size(); i++)
				{
					data.put((String) searchTerms.get(i), "");
				}

				while ((strLine = br.readLine()) != null)
				{
					for (int i = 0; i < searchTerms.size(); i++)
					{

						tmp = (String) searchTerms.get(i);
						if (strLine.indexOf(tmp) > 0)
						{

							String[] value = strLine.split(delimiter);
							key = value[0].trim();
							val = value[1].trim();

							if (cleanText)
							{
								key = tp.removeSpecialChars(value[0].trim(), regex, "");
								val = tp.removeSpecialChars(value[1].trim(), regex, "");
							}
							data.put(key, val);

						}
					}
				}
			}
			catch (NullPointerException e)
			{
				System.err.println("NullPointerException: " + e.getMessage());
			}
			finally
			{
				in.close();
			}

		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e.getMessage());
		}

		return data;
	}

	public HashMap<String, String> readFileMapNoDelimit(String filePath, ArrayList<String> searchTerms)
	{
		HashMap<String, String> data = new HashMap<String, String>();
		try
		{
			FileInputStream fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String tmp = "";
			try
			{
				for (int i = 0; i < searchTerms.size(); i++)
				{
					data.put((String) searchTerms.get(i), "");
				}

				while ((strLine = br.readLine()) != null)
				{
					for (int i = 0; i < searchTerms.size(); i++)
					{

						tmp = (String) searchTerms.get(i);
						if (strLine.indexOf(tmp) > 0)
						{
							String value = strLine.replaceFirst(tmp, "");
							data.put(tmp, value);
						}
					}
				}
			}
			catch (NullPointerException e)
			{
				System.err.println("NullPointerException: " + e.getMessage());
			}
			finally
			{
				in.close();
			}

		}
		catch (IOException e)
		{
			System.err.println("IOException: " + e.getMessage());
		}

		return data;
	}
}
