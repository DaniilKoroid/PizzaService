package ua.rd.pizzaservice.domain.accumulationcard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ua.rd.pizzaservice.domain.customer.Customer;

@Entity
@Table(name = "accumulation_card")
public class AccumulationCard implements Serializable {

	private static final long serialVersionUID = -8189597927433999347L;
	
	private static final double DISCOUNT_PERCENTAGE = 0.1d;
	private static final double MAX_TOTAL_PRICE_DISCOUNTED_PERCENT = 0.3d;
	private static final double DEFAULT_AMOUNT = 0d;
	private static final boolean DEFAULT_IS_ACTIVATED = false;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACC_CARD_SEQ_GEN")
	@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "ACC_CARD_SEQ_GEN", sequenceName = "accumulation_card_sequence")
	private Integer id;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "is_activated")
	private Boolean isActivated;

	@ManyToOne
	@JoinTable(name = "customer_card", joinColumns = @JoinColumn(name = "card_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
	private Customer owner;
	
	public AccumulationCard() {
		amount = DEFAULT_AMOUNT;
		isActivated = DEFAULT_IS_ACTIVATED;
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

	public Boolean getIsActivated() {
		return isActivated;
	}

	public void setIsActivated(Boolean isActivated) {
		this.isActivated = isActivated;
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
		discountAmount = Math.min(
				amount * DISCOUNT_PERCENTAGE,
				totalPrice * MAX_TOTAL_PRICE_DISCOUNTED_PERCENT);
		return discountAmount;
	}

	@Override
	public String toString() {
		return "AccumulationCard [id=" + id + ", amount=" + amount + ", isActivated=" + isActivated + ", owner=" + owner
				+ "]";
	}

}
