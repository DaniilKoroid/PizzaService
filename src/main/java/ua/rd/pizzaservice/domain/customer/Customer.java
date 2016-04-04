package ua.rd.pizzaservice.domain.customer;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.address.Address;

public class Customer {

	private static int idCounter = 0;

	private Integer id;
	private String name;
	private Address address;
	private AccumulationCard accumulationCard;

	public Customer(String name) {
		id = ++idCounter;
		this.name = name;
		accumulationCard = new AccumulationCard(this);
	}

	public Integer getId() {
		return id;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public AccumulationCard getAccumulationCard() {
		return accumulationCard;
	}

	public void setAccumulationCard(AccumulationCard accumulationCard) {
		this.accumulationCard = accumulationCard;
	}

	public Boolean isAccumulationCardPresent() {
		return accumulationCard.getIsActivated();
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", address=" + address + ", accumulationCard="
				+ accumulationCard + "]";
	}

}
