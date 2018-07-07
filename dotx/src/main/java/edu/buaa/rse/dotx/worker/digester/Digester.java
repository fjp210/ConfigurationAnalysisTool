package edu.buaa.rse.dotx.worker.digester;

import com.google.gson.JsonElement;
public abstract class Digester {
	protected String type;
	protected String basePath;
	protected String jobPath;
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	public String getJobPath() {
		return jobPath;
	}
	public void setJobPath(String jobPath) {
		this.jobPath = jobPath;
	}
	public abstract JsonElement degest();
}
