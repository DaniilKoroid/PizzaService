package ua.rd.pizzaservice.web.controller;

import java.util.Map;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;

public class OrderForm {
	
	private Customer customer;
	private Address deliveryAddress;
	private Map<Integer, String> pizzas;

	public Map<Integer, String> getPizzas() {
		return pizzas;
	}

	public void setPizzas(Map<Integer, String> pizzas) {
		this.pizzas = pizzas;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	@Override
	public String toString() {
		return "OrderForm [pizzas=" + pizzas + ", customer=" + customer + ", deliveryAddress=" + deliveryAddress + "]";
	}
	
}
