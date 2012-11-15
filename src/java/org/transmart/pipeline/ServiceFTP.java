package org.transmart.pipeline;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @author Vasudeva Mahavisno
 */
public class ServiceFTP {

	public void download(String pathName, String fileName, String localDirectory) {
		int base = 0;
		boolean storeFile = false;
		boolean binaryTransfer = true;
		boolean error = false;
		
		String server = "ftp.ncbi.nih.gov";
		String username = "anonymous";
		String password = "ccmb@umich.edu";
		//String pathname = "/geo/platforms/GPL10nnn/GPL10000/soft/"; 
		//String remote = "GPL10000_family.soft.gz";
		//String local = "/Users/vmahavis/data/GPL10000_family.soft.gz";
		
		FTPClient ftp;
		ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

		try 
		{
			int reply;
			ftp.connect(server);
			ftp.login(username, password);
			System.out.println("Connected to " + server + ".");

			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply)) 
			{
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				System.exit(1);
			}
		} 
		catch (IOException e) 
		{
			if (ftp.isConnected()) 
			{
				try 
				{
					ftp.disconnect();
				} catch (IOException f) {}
			}
			System.err.println("Could not connect to server.");
			e.printStackTrace();
			System.exit(1);
		}

		try 
		{

			if (binaryTransfer)
				ftp.setFileType(FTP.BINARY_FILE_TYPE);

			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(pathName);

			if (storeFile) 
			{
				InputStream input = new FileInputStream(localDirectory);
				ftp.storeFile(fileName, input);
				input.close();
			} 
			else 
			{
				OutputStream output = new FileOutputStream(localDirectory);
				ftp.retrieveFile(fileName, output);
				output.close();
			}

			ftp.logout();
		} 
		catch (FTPConnectionClosedException e) 
		{
			error = true;
			System.err.println("Server closed connection.");
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			error = true;
			e.printStackTrace();
		} 
		finally 
		{
			if (ftp.isConnected()) 
			{
				try 
				{
					ftp.disconnect();
				} 
				catch (IOException f) {}
			}
		}
		System.exit(error ? 1 : 0);
	} 

}
