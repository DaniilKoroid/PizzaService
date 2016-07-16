package ua.rd.pizzaservice.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "accumulation_card")
@NamedQueries({ 
	@NamedQuery(name = "findAllAccumulationCards", 
				query = "SELECT ac FROM AccumulationCard ac"),
	@NamedQuery(name = "findAccumulationCard",
				query = "SELECT ac FROM AccumulationCard ac "
				+ "LEFT JOIN FETCH ac.owner o " 
				+ "LEFT JOIN FETCH o.addresses adr " 
				+ "WHERE ac.id = :id"),
	@NamedQuery(name = "findCardByOwner", 
				query = "SELECT ac FROM AccumulationCard ac "
				+ "LEFT JOIN FETCH ac.owner o "
				+ "LEFT JOIN FETCH o.addresses adr "
				+ "WHERE o.id = :id")
})
public class AccumulationCard implements Serializable {

	private static final long serialVersionUID = -8189597927433999347L;

	private static final double DEFAULT_DISCOUNT_PERCENTAGE = 0.1d;
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

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "customer_card", joinColumns = @JoinColumn(name = "card_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
	private Customer owner;
	
	@Column(name = "discount_percentage")
	private Double discountPercentage;

	public AccumulationCard() {
		amount = DEFAULT_AMOUNT;
		isActivated = DEFAULT_IS_ACTIVATED;
		discountPercentage = DEFAULT_DISCOUNT_PERCENTAGE;
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
	
	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Double use(Double totalPrice) {
		double discountAmount = calculateDiscount(totalPrice);
		amount += totalPrice;
		return discountAmount;
	}

	public Double calculateDiscount(Double totalPrice) {
		double discountAmount = 0d;
		discountAmount = Math.min(amount * DEFAULT_DISCOUNT_PERCENTAGE, totalPrice * MAX_TOTAL_PRICE_DISCOUNTED_PERCENT);
		return discountAmount;
	}

	@Override
	public String toString() {
		return "AccumulationCard [id=" + id + ", amount=" + amount + ", isActivated=" + isActivated + ", owner=" + owner
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isActivated == null) ? 0 : isActivated.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AccumulationCard other = (AccumulationCard) obj;
		if (amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!amount.equals(other.amount)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (isActivated == null) {
			if (other.isActivated != null) {
				return false;
			}
		} else if (!isActivated.equals(other.isActivated)) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		return true;
	}
	
}
