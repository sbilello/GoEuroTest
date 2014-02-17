package model;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Item {
	@XmlElement(name = "geo_position")
	private GeoPosition geoPosition;
	
	@XmlElement(name = "_id")
	private Integer id;

	private String name, type;

	@XmlElement(name = "_type")
	private String underscoreType;

	public GeoPosition getGeoPosition() {
		return geoPosition;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getUnderscoreType() {
		return underscoreType;
	}

	public void setGeoPosition(GeoPosition geoPosition) {
		this.geoPosition = geoPosition;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUnderscoreType(String underscoreType) {
		this.underscoreType = underscoreType;
	}

	@Override
	public String toString() {
		StringBuilder row = new StringBuilder();
		return row.append("ID: " + id).append(" _TYPE: " + underscoreType)
				.append(" NAME: " + name)
				.append(" LATITUDE: " + geoPosition.getLatitude())
				.append(" LONGITUDE: " + geoPosition.getLongitude())
				.append(" TYPE: " + type).toString();

	}
	public String[] getRecord(){
		String[] fields = new String[6];
		fields[0]=underscoreType;
		fields[1]=String.valueOf(id);
		fields[2]=name;
		fields[3]=type;
		fields[4]=String.valueOf(geoPosition.getLatitude());
		fields[5]=String.valueOf(geoPosition.getLongitude());
		return fields;
	}
	public String[] csvRowHeaders() {
		return new String[] { "_type", "_id", "name", "type", "latitude",
				"longitude" };
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id).append(underscoreType)
				.append(name).append(geoPosition).append(type)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final Item otherObject = (Item) obj;

		return new EqualsBuilder().append(this.id, otherObject.id)
				.append(this.underscoreType, otherObject.underscoreType)
				.append(this.name, otherObject.name)
				.append(this.geoPosition, otherObject.geoPosition)
				.append(this.type, otherObject.type).isEquals();
	}
}
