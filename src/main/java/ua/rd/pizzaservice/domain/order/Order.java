package ua.rd.pizzaservice.domain.order;

import java.util.List;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.pizza.Pizza;

public class Order {

	private static Long idCounter = 0L;

	private Long id;
	private OrderState state;
	private Customer customer;
	private List<Pizza> pizzas;

	public Order() {
	}

	public Order(Customer customer, List<Pizza> pizzas) {
		this(++idCounter, OrderState.NEW, customer, pizzas);
	}

	public Order(Long id, Customer customer, List<Pizza> pizzas) {
		this(id, OrderState.NEW, customer, pizzas);
	}

	public Order(Long id, OrderState state, Customer customer, List<Pizza> pizzas) {
		this.id = id;
		this.state = state;
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

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", customer=" + customer 
				+ ", pizzas=" + pizzas + "]";
	}

	public Boolean canChange() {
		return state == OrderState.NEW;
	}

	public Boolean changeOrder(List<Pizza> newPizzas) {
		Boolean canChange = canChange();
		if (canChange) {
			pizzas = newPizzas;
		}
		return canChange;
	}

	public Double calculateFullPrice() {
		double totalPrice = 0d;
		for (Pizza pizza : pizzas) {
			totalPrice += pizza.getPrice();
		}
		return totalPrice;
	}

	public Boolean cancel() {
		return state.cancel(this);
	}

	public Boolean nextState() {
		return state.nextState(this);
	}

	public Boolean canProceedToState(OrderState proceedToState) {
		Boolean canProceedTo = state.canProceedTo(proceedToState);
		System.out.println("Can proceed from " + state + " to " 
		+ proceedToState + " -> " + canProceedTo);
		return canProceedTo;
	}
}
