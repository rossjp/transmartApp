package org.transmart.pipeline.model;

public class SampleData implements Comparable<Object>
{
	private String id;
	private String sampleName;
	private double value;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getSampleName()
	{
		return sampleName;
	}

	public void setSampleName(String sampleName)
	{
		this.sampleName = sampleName;
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}

	@Override
	public int compareTo(Object o1)
	{
		if (this.value == ((SampleData) o1).value)
		{
			return 0;
		}
		else if ((this.value) < ((SampleData) o1).value)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}

	public String toString()
	{
		return "id " + id + "  SampleName " + sampleName + " value " + value;
	}

}
