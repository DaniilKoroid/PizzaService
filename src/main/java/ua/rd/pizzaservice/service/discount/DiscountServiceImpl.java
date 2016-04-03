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
	public Double calculateDiscountAmount(Order order) {
		List<Discount> appliableDiscounts = discountRepository.getAppliableDiscounts(order);
		Double discountAmount = calculateDiscountAmount(order, appliableDiscounts);
		Double orderPriceWithDiscounts = order.calculateTotalPrice() - discountAmount;
		Double cardDiscountAmount = calculateAccumulationCardDiscountAmount(order.getCustomer(), orderPriceWithDiscounts);
		Double totalDiscountAmount = discountAmount + cardDiscountAmount;
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

}
