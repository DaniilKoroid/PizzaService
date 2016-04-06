package ua.rd.pizzaservice.domain.address;

public class Address {

	private String country;
	private String city;
	private String street;
	private String building;

	public Address(String country, String city, String street, String building) {
		this.country = country;
		this.city = city;
		this.street = street;
		this.building = building;
	}

	public Address() {
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	@Override
	public String toString() {
		return "Address [country=" + country + ", city=" + city 
				+ ", street=" + street + ", building=" 
				+ building + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((building == null) ? 0 : building.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;			
		}
		if (obj == null) {
			return false;			
		}
		if (getClass() != obj.getClass()) {
			return false;			
		}
		Address other = (Address) obj;
		if (building == null) {
			if (other.building != null) {
				return false;
			}
		} else if (!building.equals(other.building)) {
			return false;			
		}
		if (city == null) {
			if (other.city != null){
				return false;				
			}
		} else if (!city.equals(other.city)) {
			return false;			
		}
		if (country == null) {
			if (other.country != null) {
				return false;				
			}
		} else if (!country.equals(other.country)) {
			return false;			
		}
		if (street == null) {
			if (other.street != null) {
				return false;				
			}
		} else if (!street.equals(other.street)) {
			return false;			
		}
		return true;
	}

}
