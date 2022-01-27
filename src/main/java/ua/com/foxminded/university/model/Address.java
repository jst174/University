package ua.com.foxminded.university.model;

import javax.persistence.*;
import java.util.Objects;

@NamedQueries({
    @NamedQuery(name = "Address_delete", query = "DELETE FROM Address where id = :id"),
    @NamedQuery(name = "Address_getAll", query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address_countAllRows", query = "SELECT COUNT (a) FROM Address a")
})
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String country;
    private String city;
    private String street;
    @Column(name = "house_number")
    private String houseNumber;
    @Column(name = "apartment_number")
    private String apartmentNumber;
    private String postcode;

    public Address() {

    }

    public Address(String country, String city, String street, String houseNumber, String apartmentNumber,
                   String postcode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
        this.postcode = postcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return country + ", " + city + ", " + street + ", " + houseNumber + ", " + apartmentNumber + ", " + postcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) && Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(houseNumber, address.houseNumber) && Objects.equals(apartmentNumber, address.apartmentNumber) && Objects.equals(postcode, address.postcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, houseNumber, apartmentNumber, postcode);
    }

    public static class Builder {

        private Address address;

        public Builder() {
            address = new Address();
        }

        public Builder setId(int id) {
            address.setId(id);
            return this;
        }

        public Builder setCountry(String country) {
            address.setCountry(country);
            return this;
        }

        public Builder setCity(String city) {
            address.setCity(city);
            return this;
        }

        public Builder setStreet(String street){
           address.setStreet(street);
            return this;
        }

        public Builder setHouseNumber(String houseNumber){
            address.setHouseNumber(houseNumber);
            return this;
        }

        public Builder setApartmentNumber(String apartmentNumber){
            address.setApartmentNumber(apartmentNumber);
            return this;
        }

        public Builder setPostcode(String postcode){
            address.setPostcode(postcode);
            return this;
        }

        public Address build(){
            return address;
        }
    }
}
