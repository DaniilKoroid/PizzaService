package ua.rd.pizzaservice.domain.order;

import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.pizza.Pizza;

@Component
@Scope("prototype")
@Entity
@Table(name = "order_table")
public class Order {

	private static Long idCounter = 0L;

	@Id
	@SequenceGenerator(initialValue = 1, name = "order_id_gen", sequenceName = "order_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_gen")
	private Long id;
	@Enumerated(EnumType.ORDINAL)
	private OrderState state;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="customer_id")
	private Customer customer;
	
	@ElementCollection
	private Map<Pizza, Integer> pizzas;

	public Order() {
//		id = ++idCounter;
	}

	public Order(Customer customer, Map<Pizza, Integer> pizzas) {
		this(++idCounter, OrderState.NEW, customer, pizzas);
	}

	public Order(Long id, Customer customer, Map<Pizza, Integer> pizzas) {
		this(id, OrderState.NEW, customer, pizzas);
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

	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
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
