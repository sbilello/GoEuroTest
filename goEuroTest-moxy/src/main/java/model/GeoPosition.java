package model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class GeoPosition {
	private Float latitude, longitude;

	public Float getLatitude() {
		return latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(latitude).append(longitude)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GeoPosition == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final GeoPosition otherObject = (GeoPosition) obj;

		return new EqualsBuilder().append(this.latitude, otherObject.latitude)
				.isEquals();
	}
}
