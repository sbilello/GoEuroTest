package model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/*
 * These attributes could be replaced with
 * just one property: Map<String,String> myAttributeMap
 *
 */
public class Row {

	private String _type, name, type;
	private Float latitude, longitude;
	private Integer _id;

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String get_type() {
		return _type;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void set_type(String _type) {
		this._type = _type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder row = new StringBuilder();
		return row.append("ID: " + _id).append(" _TYPE: " + _type)
				.append(" NAME: " + name).append(" LATITUDE: " + latitude)
				.append(" LONGITUDE: " + longitude).append(" TYPE: " + type)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(_id).append(_type)
				.append(name).append(latitude).append(longitude).append(type)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Row == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final Row otherObject = (Row) obj;

		return new EqualsBuilder().append(this._id, otherObject._id)
				.append(this._type, otherObject._type)
				.append(this.name, otherObject.name)
				.append(this.latitude, otherObject.latitude)
				.append(this.longitude, otherObject.longitude)
				.append(this.type, otherObject.type).isEquals();
	}

	public String[] csvRowValues() {
		return new String[] { _type, String.valueOf(_id), name, type,
				String.valueOf(latitude), String.valueOf(longitude) };
	}

	public String[] csvRowHeaders() {
		return new String[] { "_type", "_id", "name", "type", "latitude",
				"longitude" };
	}
}
