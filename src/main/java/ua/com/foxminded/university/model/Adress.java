package ua.com.foxminded.university.model;

public class Adress {

	private String country;
	private String city;
	private String street;
	private String houseNumber;
	private String apartamentNumber;
	private String postcode;

	public Adress(String country, String city, String street, String houseNumber, String apartamentNumber,
			String postcode) {
		this.country = country;
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
		this.apartamentNumber = apartamentNumber;
		this.postcode = postcode;
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

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getApartamentNumber() {
		return apartamentNumber;
	}

	public void setApartamentNumber(String apartamentNumber) {
		this.apartamentNumber = apartamentNumber;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Override
	public String toString() {
		return country + ", " + city + ", " + street + ", " + houseNumber + ", " + apartamentNumber + ", " + postcode;
	}

}
