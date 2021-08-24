package ua.com.foxminded.model;

public class Adress {

	private String country;
	private String city;
	private String street;
	private int houseNumber;
	private int apartamentNumber;
	private int postcode;

	public Adress(String country, String city, String street, int houseNumber, int apartamentNumber, int postcode) {
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

	public int getHouseNumber() {
		return houseNumber;
	}

	public int getApartamentNumber() {
		return apartamentNumber;
	}

	public int getPostcode() {
		return postcode;
	}

}
