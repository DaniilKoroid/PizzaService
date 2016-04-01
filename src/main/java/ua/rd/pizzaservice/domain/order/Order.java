package ua.rd.pizzaservice.domain.order;

import java.util.List;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.pizza.Pizza;

public class Order {

	private static Long idCounter = 0L;
	
	private Long id;
	private Customer customer;
	private List<Pizza> pizzas;
	
	public Order() {
	}
	
	public Order(Customer customer, List<Pizza> pizzas) {
		this(++idCounter, customer, pizzas);
	}
	
	public Order(Long id, Customer customer, List<Pizza> pizzas) {
		this.id = id;
		this.customer = customer;
		this.pizzas = pizzas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Pizza> getPizzas() {
		return pizzas;
	}

	public void setPizzas(List<Pizza> pizzas) {
		this.pizzas = pizzas;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", customer=" + customer + ", pizzas=" + pizzas + "]";
	}

}
