package org.transmart.pipeline;

import java.util.List;

import org.transmart.pipeline.PipelineConstant;


public class DatasetValidation implements PipelineConstant
{
	private double[] values;

	public boolean checkDataRowValues(List<String> val, int size)
	{
		boolean isValid = false;

		if (val.size() == size)
		{
			isValid = true;
			values = new double[size];

			for (int i = 0; i < val.size(); i++)
			{
				try
				{
					values[i] = Double.parseDouble(val.get(i));
				}
				catch (NumberFormatException e)
				{
					values[i] = MISSING_VALUE_KEY;
				}
			}
		}
		return isValid;
	}

	public double[] getValues()
	{
		return values;
	}

	public void setValues(double[] values)
	{
		this.values = values;
	}

}