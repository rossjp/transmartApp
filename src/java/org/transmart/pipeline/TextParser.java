package org.transmart.pipeline;

/**
 * @version 1.0 10 Aug 2010
 * @author Vasudeva Mahavisno vmahavis@gmail.com
 */
public class TextParser
{
	public String removeSpecialChars(String text, String[] regex, String replacement)
	{
		for (int i = 0; i < regex.length; i++)
		{
			text = text.replaceAll(regex[i], replacement);
		}

		return text;
	}

	public String oracleSQLPrepare(String text)
	{
		text = text.replaceAll("\'", "\''");
		return text;
	}

}
