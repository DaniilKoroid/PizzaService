package ua.rd.pizzaservice.domain.accumulationcard;

import ua.rd.pizzaservice.domain.customer.Customer;

public class AccumulationCard {

	private static final double DISCOUNT_PRECENTAGE = 0.1d;
	private static final double MAX_TOTAL_PRICE_DISCOUNTED_PERCENT = 0.3d;
	
	private static int idCounter = 0;
	
	private Integer id;
	private Double amount;
	private Customer owner;
	
	public AccumulationCard(Integer id, Double amount, Customer owner) {
		this.id = id;
		this.amount = amount;
		this.owner = owner;
	}
	
	public AccumulationCard(Double amount, Customer owner) {
		this.id = ++idCounter;
		this.amount = amount;
		this.owner = owner;
	}

	public AccumulationCard(Integer id, Double amount) {
		this.id = id;
		this.amount = amount;
	}

	public AccumulationCard() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Customer getOwner() {
		return owner;
	}

	public void setOwner(Customer owner) {
		this.owner = owner;
	}
	
	public Double use(Double totalPrice) {
		double discountAmount = calculateDiscount(totalPrice);
		amount += totalPrice;
		return discountAmount;
	}
	
	public Double calculateDiscount(Double totalPrice) {
		double discountAmount = 0d;
		discountAmount = Math.min(amount * DISCOUNT_PRECENTAGE, totalPrice * MAX_TOTAL_PRICE_DISCOUNTED_PERCENT);
		return discountAmount;
	}

	@Override
	public String toString() {
		return "AccumulationCard [id=" + id + ", amount=" + amount + ", owner=" + owner + "]";
	}
}
