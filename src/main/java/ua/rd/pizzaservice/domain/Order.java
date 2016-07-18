package ua.rd.pizzaservice.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ua.rd.pizzaservice.LocalDateTimeConverter;

@Component
@Scope("prototype")
@Entity
@Table(name = "order_table")
@NamedQueries({
    @NamedQuery(name = "findAllOrders", query = "SELECT o FROM Order o"),
    @NamedQuery(name = "findOrder",
				query = "SELECT o FROM Order o "
				+ "LEFT JOIN FETCH o.customer c " 
				+ "LEFT JOIN FETCH c.addresses adr "
				+ "LEFT JOIN FETCH o.pizzas p " 
				+ "WHERE o.id = :id") 
})
public class Order implements Serializable {

	private static final long serialVersionUID = -5981482506476614514L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_SEQ_GEN")
	@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "ORDER_SEQ_GEN", sequenceName = "order_sequence")
	private Long id;
	
	@Column(name = "order_state")
	@Enumerated(EnumType.ORDINAL)
	private OrderState state;
	
	@Column(name = "creation_date")
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime creationDate;
	
	@Column(name = "delivery_date")
	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime deliveryDate;
	
	@ManyToOne(targetEntity = Address.class, cascade = {CascadeType.MERGE})
	@JoinColumn(name = "address_id")
	private Address address;
	
	@ManyToOne(targetEntity = Customer.class, cascade = {CascadeType.MERGE})
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ElementCollection
	@CollectionTable(name = "orders_pizzas", joinColumns = @JoinColumn(name = "order_id"))
	@MapKeyJoinColumn(name = "pizza_id")
	@Column(name = "pizza_count")
	private Map<Pizza, Integer> pizzas;

	public Order() {
	}

	public Order(Long id, OrderState state, LocalDateTime creationDate, LocalDateTime deliveryDate, Address address,
			Customer customer, Map<Pizza, Integer> pizzas) {
		this.id = id;
		this.state = state;
		this.creationDate = creationDate;
		this.deliveryDate = deliveryDate;
		this.address = address;
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
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", state=" + state + ", creationDate=" + creationDate + ", deliveryDate="
				+ deliveryDate + ", address=" + address + ", customer=" + customer + ", pizzas=" + pizzas + "]";
	}

}
