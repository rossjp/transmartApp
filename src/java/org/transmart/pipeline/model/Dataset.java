package org.transmart.pipeline.model;

import java.util.Date;

public class Dataset {
	private Integer datasetId;
	private String datasetName;
	private String datasource;
	private String pubmedId;
	private String description;
	private String datasourceId;
	private String contactName;
	private String contactEmail;
	private String platformId;
	private String status;
	private String qcStatus;
	private Date submissionDate;

	public Integer getDatasetId()
	{
		return datasetId;
	}

	public void setDatasetId(Integer datasetId)
	{
		this.datasetId = datasetId;
	}

	public String getDatasetName()
	{
		return datasetName;
	}

	public void setDatasetName(String datasetName)
	{
		this.datasetName = datasetName;
	}

	public String getDatasource()
	{
		return datasource;
	}

	public void setDatasource(String datasource)
	{
		this.datasource = datasource;
	}

	public String getPubmedId()
	{
		return pubmedId;
	}

	public void setPubmedId(String pubmedId)
	{
		this.pubmedId = pubmedId;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDatasourceId()
	{
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId)
	{
		this.datasourceId = datasourceId;
	}

	public String getContactName()
	{
		return contactName;
	}

	public void setContactName(String contactName)
	{
		this.contactName = contactName;
	}

	public String getContactEmail()
	{
		return contactEmail;
	}

	public void setContactEmail(String contactEmail)
	{
		this.contactEmail = contactEmail;
	}

	public String getPlatformId()
	{
		return platformId;
	}

	public void setPlatformId(String platformId)
	{
		this.platformId = platformId;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getQcStatus()
	{
		return qcStatus;
	}

	public void setQcStatus(String qcStatus)
	{
		this.qcStatus = qcStatus;
	}

	public Date getSubmissionDate()
	{
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate)
	{
		this.submissionDate = submissionDate;
	}

}
