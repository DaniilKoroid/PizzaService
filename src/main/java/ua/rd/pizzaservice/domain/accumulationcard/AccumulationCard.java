package ua.rd.pizzaservice.domain.accumulationcard;

public class AccumulationCard {

	private static final double DISCOUNT_PRECENTAGE = 0.1d;
	private static final double MAX_TOTAL_PRICE_DISCOUNTED_PERCENT = 0.3d;
	private static final double DEFAULT_AMOUNT = 0d;
	private static final Boolean DEFAULT_IS_ACTIVATED = Boolean.FALSE;

	private static int idCounter = 0;

	private Integer id;
	private Double amount;
	private Boolean isActivated;

	public AccumulationCard() {
		id = ++idCounter;
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
		discountAmount = Math.min(amount * DISCOUNT_PRECENTAGE, totalPrice * MAX_TOTAL_PRICE_DISCOUNTED_PERCENT);
		return discountAmount;
	}

	@Override
	public String toString() {
		return "AccumulationCard [id=" + id + ", amount=" + amount + ", isActivated=" + isActivated + "]";
	}
}
