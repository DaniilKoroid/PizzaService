package ua.rd.pizzaservice.domain.order;

import java.util.List;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.pizza.Pizza;

public class Order {

	public enum OrderStatus {
		NEW,
		IN_PROGRESS,
		DONE,
		CANCELLED,
		;
	}
	
	private static Long idCounter = 0L;
	
	private Long id;
	private OrderStatus status;
	private Customer customer;
	private List<Pizza> pizzas;
	private Discount discount;
	
	public Order() {
	}
	
	public Order(Customer customer, List<Pizza> pizzas, Discount discount) {
		this(++idCounter, OrderStatus.NEW, customer, pizzas, discount);
	}

	public Order(Long id, OrderStatus status, Customer customer, List<Pizza> pizzas) {
		this(id, status, customer, pizzas, null);
	}

	public Order(Long id, Customer customer, List<Pizza> pizzas) {
		this(id, OrderStatus.NEW, customer, pizzas, null);
	}
	
	public Order(Customer customer, List<Pizza> pizzas) {
		this(++idCounter, OrderStatus.NEW, customer, pizzas, null);
	}
	
	public Order(Long id, OrderStatus status, Customer customer, List<Pizza> pizzas, Discount discount) {
		this.id = id;
		this.status = status;
		this.customer = customer;
		this.pizzas = pizzas;
		this.discount = discount;
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", customer=" + customer + ", pizzas=" + pizzas + "]";
	}
	
	public boolean changeOrder(List<Pizza> newPizzas) {
		if(!checkOrderStatusToBe(OrderStatus.NEW)) {
			return false;
		}
		pizzas = newPizzas;
		return true;
	}
	
	public Double calculateTotalPrice() {
		double totalPrice = 0d;
		for (Pizza pizza : pizzas) {
			totalPrice += pizza.getPrice();
		}
		return totalPrice;
	}
	
	public boolean cancel() {
		if(checkOrderStatusToBe(OrderStatus.CANCELLED)
				|| checkOrderStatusToBe(OrderStatus.DONE)) {
			
			return false;
		}
		status = OrderStatus.CANCELLED;
		return true;
	}
	
	public boolean setInProgress() {
		if(!checkOrderStatusToBe(OrderStatus.NEW)) {
			return false;
		}
		status = OrderStatus.IN_PROGRESS;
		return true;
	}
	
	public boolean setDone() {
		if(!checkOrderStatusToBe(OrderStatus.IN_PROGRESS)) {
			return false;
		}
		status = OrderStatus.DONE;
		return true;
	}
	
	private boolean checkOrderStatusToBe(OrderStatus status) {
		return status == this.status;
	}
}
