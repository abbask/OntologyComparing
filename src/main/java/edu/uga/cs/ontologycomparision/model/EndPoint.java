package edu.uga.cs.ontologycomparision.model;

import java.util.List;

public class EndPoint {
	
	private int id;
	private String name;
	private String url;
	private List<DataSet> datasets;
	
	public EndPoint() {		
	}
	
	public EndPoint(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public EndPoint(int id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}	
	
	public EndPoint(int id, String name, String url, List<DataSet> datasets) {		
		this.id = id;
		this.name = name;
		this.url = url;
		this.datasets = datasets;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;		
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	

	public List<DataSet> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DataSet> datasets) {
		this.datasets = datasets;
	}

	@Override
	public String toString() {
		return "EndPoint [id=" + id + ", name=" + name + ", url=" + url + ", datasets=" + datasets + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datasets == null) ? 0 : datasets.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EndPoint other = (EndPoint) obj;
		if (datasets == null) {
			if (other.datasets != null)
				return false;
		} else if (!datasets.equals(other.datasets))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
	
	
	
}
