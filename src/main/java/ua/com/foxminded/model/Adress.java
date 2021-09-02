package ua.com.foxminded.model;

public class Adress {

	private String country;
	private String city;
	private String street;
	private String houseNumber;
	private String apartamentNumber;
	private String postcode;

	public Adress(String country, String city, String street, String houseNumber, String apartamentNumber, String postcode) {
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

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public String getApartamentNumber() {
		return apartamentNumber;
	}

	public String getPostcode() {
		return postcode;
	}
	
	

}
