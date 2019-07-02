package edu.uga.cs.ontologycomparision.model;

public class DataSet {
	
	private int id;
	private String name;
	private String graph_name;
	
	public DataSet() {
	
	}
	
	public DataSet(int id, String name, String graph_name) {
		
		this.id = id;
		this.name = name;
		this.graph_name = graph_name;
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
	public String getGraph_name() {
		return graph_name;
	}
	public void setGraph_name(String graph_name) {
		this.graph_name = graph_name;
	}

	@Override
	public String toString() {
		return "DataSet [id=" + id + ", name=" + name + ", graph_name=" + graph_name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((graph_name == null) ? 0 : graph_name.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		DataSet other = (DataSet) obj;
		if (graph_name == null) {
			if (other.graph_name != null)
				return false;
		} else if (!graph_name.equals(other.graph_name))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	

}
