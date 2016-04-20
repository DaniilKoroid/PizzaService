package ua.rd.pizzaservice.domain.customer;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.factory.FactoryBean;

import ua.rd.pizzaservice.domain.address.Address;

@Entity
public class Customer implements FactoryBean<Customer> {

	private static int idCounter = 0;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	private String name;
	@ElementCollection
	private List<Address> address;
	@ElementCollection
	private List<String> phones;
	
	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public Customer() {
	}

	public Customer(String name) {
		id = ++idCounter;
		this.name = name;
	}

	public Customer(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + "]";
	}

	@Override
	public Customer getObject() throws Exception {
		return new Customer(1, "Abz");
	}

	@Override
	public Class<?> getObjectType() {
		return Customer.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
