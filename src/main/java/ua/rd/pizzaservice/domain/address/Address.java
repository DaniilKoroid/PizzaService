package ua.rd.pizzaservice.domain.address;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

	@Column(name="ADDR_ID")
	private Integer id;
	private String city;

	public Address() {
		super();
	}

	public Address(Integer id, String city) {
		super();
		this.id = id;
		this.city = city;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
