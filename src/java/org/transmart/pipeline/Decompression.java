package org.transmart.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class Decompression
{
	public void decompress(String fileName, String outputDir) throws IOException
	{
		if (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.lastIndexOf(".") + 3).equalsIgnoreCase("gz"))
		{
			System.out.println("Creating an GZIPInputStream for the file");
			gzDecompress(outputDir);

		}
		else
		{
			System.out.println("Creating an InputStream for the file");
			//TODO add unpack and cleanfiles methods
			//unpack(fileName, outputDir);
			//cleanFiles(outputDir);
		}
	}

	private void gzDecompress(String directoryName)
	{
		File dir = new File(directoryName);
		String[] dirFiles = dir.list();

		int sChunk = 8192;

		for (int i = 0; i < dirFiles.length; i++)
		{

			String zipname = directoryName + "/" + dirFiles[i];
			if (zipname.substring(zipname.lastIndexOf(".") + 1, zipname.lastIndexOf(".") + 3).equalsIgnoreCase("gz"))
			{
				String source = zipname.substring(0, zipname.length() - 3);
				GZIPInputStream zipin = null;

				try
				{
					FileInputStream in = new FileInputStream(zipname);
					zipin = new GZIPInputStream(in);
				}
				catch (IOException e)
				{
					System.out.println("Couldn't open " + zipname + ".");
					return;
				}
				byte[] buffer = new byte[sChunk];

				try
				{
					FileOutputStream out = new FileOutputStream(source);
					int length;
					while ((length = zipin.read(buffer, 0, sChunk)) != -1)
						out.write(buffer, 0, length);
					out.close();
				}
				catch (IOException e)
				{
					System.out.println("Couldn't decompress ");
				}
			}
		}

	}
}
