package ua.rd.pizzaservice.domain.order;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.pizza.Pizza;

@Component
@Scope("prototype")
public class Order {

	private Long id;
	private OrderState state;
	private Customer customer;
	private Map<Pizza, Integer> pizzas;

	public Order() {
	}

	public Order(Long id, OrderState state, Customer customer, Map<Pizza, Integer> pizzas) {
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

	public Map<Pizza, Integer> getPizzas() {
		return pizzas;
	}

	public void setPizzas(Map<Pizza, Integer> pizzas) {
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
		return "Order [id=" + id + ", customer=" + customer + ", pizzas=" + pizzas + "]";
	}

	public Boolean canChange() {
		return state == OrderState.NEW;
	}

	public Boolean changeOrder(Map<Pizza, Integer> newPizzas) {
		Boolean canChange = canChange();
		if (canChange) {
			pizzas = newPizzas;
		}
		return canChange;
	}

	public Double calculateFullPrice() {
		double totalPrice = 0d;
		for (Entry<Pizza, Integer> entrySet : pizzas.entrySet()) {
			double curPrice = 0d;
			double price = entrySet.getKey().getPrice();
			Integer count = entrySet.getValue();
			curPrice = price * count;
			totalPrice += curPrice;
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
		System.out.println("Can proceed from " + state + " to " + proceedToState + " -> " + canProceedTo);
		return canProceedTo;
	}
}
