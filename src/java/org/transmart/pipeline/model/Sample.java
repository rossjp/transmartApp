package org.transmart.pipeline.model;

public class Sample {

		private Integer sampleId;
		private Integer datasetId;
		private Integer columnIndex;
		private String sampleName;
		private String sampleType;
		private String datasourceSampleId;


		public Integer getSampleId()
		{
			return sampleId;
		}

		public void setSampleId(Integer sampleId)
		{
			this.sampleId = sampleId;
		}

		public String getSampleName()
		{
			return sampleName;
		}

		public void setSampleName(String sampleName)
		{
			this.sampleName = sampleName;
		}

		public String getSampleType()
		{
			return sampleType;
		}

		public void setSampleType(String sampleType)
		{
			this.sampleType = sampleType;
		}

		public String getDatasourceSampleId()
		{
			return datasourceSampleId;
		}

		public void setDatasourceSampleId(String datasourceSampleId)
		{
			this.datasourceSampleId = datasourceSampleId;
		}

		public Integer getDatasetId()
		{
			return datasetId;
		}

		public void setDatasetId(Integer datasetId)
		{
			this.datasetId = datasetId;
		}

		public Integer getColumnIndex()
		{
			return columnIndex;
		}

		public void setColumnIndex(Integer columnIndex)
		{
			this.columnIndex = columnIndex;
		}

}
