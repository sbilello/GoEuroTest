package model;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class SearchResult {

	private List<Item> results;

	public List<Item> getResults() {
		return results;
	}

	public void setResults(List<Item> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		for (Item i : results) 
			build.append(i.toString());
		return build.toString();
	}
	
	
	public List<String[]> getRecords() {
		List<String[]> records = new LinkedList<String[]>();
		if (results.get(0)!=null)
			records.add(results.get(0).csvRowHeaders());
		for (Item i: results)
			records.add(i.getRecord());
		return records;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(results)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SearchResult == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final SearchResult otherObject = (SearchResult) obj;

		return new EqualsBuilder().append(this.results, otherObject.results)
				.isEquals();
	}
}
