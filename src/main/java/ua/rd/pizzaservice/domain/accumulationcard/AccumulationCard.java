package ua.rd.pizzaservice.domain.accumulationcard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "accumulation_card")
public class AccumulationCard {

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
		return "AccumulationCard [id=" + id + ", amount=" + amount
				+ ", isActivated=" + isActivated + "]";
	}
}
