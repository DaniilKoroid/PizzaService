package ua.rd.pizzaservice.service.discount;

import java.util.List;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.discount.DiscountRepository;
import ua.rd.pizzaservice.repository.discount.InMemDiscountRepository;

public class DiscountServiceImpl implements DiscountService {
	
	private static final Double DISCOUNT_AMOUNT_WITHOUT_ACCUMULATION_CARD = 0d;
	
	private DiscountRepository discountRepository = new InMemDiscountRepository();
	
	@Override
	public Double calculateFinalDiscountAmount(Order order) {
		Double discountsAmount = calculateDiscountsAmount(order);
		Double orderPriceWithDiscounts = order.calculateFullPrice() - discountsAmount;
		Double cardDiscountAmount = calculateAccumulationCardDiscountAmount(order.getCustomer(), orderPriceWithDiscounts);
		Double totalDiscountAmount = discountsAmount + cardDiscountAmount;
		return totalDiscountAmount;
	}
	
	private Double calculateDiscountAmount(Order order, List<Discount> discounts) {
		Double discountAmount = 0d;
		for (Discount discount : discounts) {
			discountAmount += discount.calculateDiscount(order);
		}
		return discountAmount;
	}
	
	private Double calculateAccumulationCardDiscountAmount(Customer customer, Double orderPriceWithDiscounts) {
		if (!customer.isAccumulationCardPresent()) {
			return DISCOUNT_AMOUNT_WITHOUT_ACCUMULATION_CARD;
		}
		AccumulationCard card = customer.getAccumulationCard();
		Double cardDiscountAmount = card.calculateDiscount(orderPriceWithDiscounts);
		return cardDiscountAmount;
	}

	@Override
	public Double calculateDiscountsAmount(Order order) {
		List<Discount> appliableDiscounts = discountRepository.getAppliableDiscounts(order);
		Double discountAmount = calculateDiscountAmount(order, appliableDiscounts);
		return discountAmount;
	}

	@Override
	public Double calculatePriceWithDiscounts(Order order) {
		Double orderPrice = order.calculateFullPrice();
		Double discountsAmount = calculateDiscountsAmount(order);
		Double priceWithDiscounts = orderPrice - discountsAmount;
		return priceWithDiscounts;
	}

}
