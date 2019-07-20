package edu.uga.cs.ontologycomparision.model;

public class Result<E, R> {
	
	private E element;
	private R result;
	
	public Result() {
	}
	
	public Result(E element, R result) {
		super();
		this.element = element;
		this.result = result;
	}
	public E getElement() {
		return element;
	}
	public void setElement(E element) {
		this.element = element;
	}
	public R getResult() {
		return result;
	}
	public void setResult(R result) {
		this.result = result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
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
		Result other = (Result) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Result [element=" + element + ", result=" + result + "]";
	}

}
