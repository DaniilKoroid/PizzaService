package ua.rd.pizzaservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.AccumulationCard;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.service.AccumulationCardService;
import ua.rd.pizzaservice.service.DiscountProvider;
import ua.rd.pizzaservice.service.DiscountService;
import ua.rd.pizzaservice.service.OrderPriceCalculatorService;

@Service
public class DiscountServiceImpl implements DiscountService {

	private static final Double DISCOUNT_AMOUNT_WITHOUT_ACCUMULATION_CARD = 0d;

	private DiscountProvider discountProvider;
	private AccumulationCardService accCardService;
	private OrderPriceCalculatorService orderPriceCalculatorService;

	@Autowired
	public DiscountServiceImpl(	AccumulationCardService accCardService,
								DiscountProvider discountProvider,
								OrderPriceCalculatorService orderPriceCalculatorService) {
		this.accCardService = accCardService;
		this.discountProvider = discountProvider;
		this.orderPriceCalculatorService = orderPriceCalculatorService;
	}

	@Override
	public Double calculateFinalDiscountAmount(Order order) {
		checkOrderExistance(order);
		Double discountsAmount = calculateDiscountsAmount(order);
		System.out.println("Discounts amount: " + discountsAmount);
		Double orderPriceWithDiscounts = orderPriceCalculatorService.getFullPrice(order)
				- discountsAmount;
		System.out.println("Order price with discounts: " + orderPriceWithDiscounts);
		Double cardDiscountAmount = calculateAccumulationCardDiscountAmount(order.getCustomer(),
				orderPriceWithDiscounts);
		System.out.println("Card discount amount: " + cardDiscountAmount);
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

		if (!accCardService.hasAccumulationCard(customer)) {
			System.out.println("Order has no customer");
			return DISCOUNT_AMOUNT_WITHOUT_ACCUMULATION_CARD;
		}
		System.out.println("Order has customer");
		AccumulationCard card = accCardService.getAccumulationCardByCustomer(customer);
		System.out.println("Card: " + card);
		Double cardDiscount = accCardService.calculateDiscount(card, orderPriceWithDiscounts);
		System.out.println("Card discount: " + cardDiscount);
		return cardDiscount;
	}

	@Override
	public Double calculateDiscountsAmount(Order order) {
		checkOrderExistance(order);
		List<Discount> appliableDiscounts = discountProvider.getAppliableDiscounts(order);
		Double discountAmount = calculateDiscountAmount(order, appliableDiscounts);
		return discountAmount;
	}

	@Override
	public Double calculatePriceWithDiscounts(Order order) {
		checkOrderExistance(order);
		Double orderPrice = orderPriceCalculatorService.getFullPrice(order);
		Double discountsAmount = calculateDiscountsAmount(order);
		Double priceWithDiscounts = orderPrice - discountsAmount;
		return priceWithDiscounts;
	}
	
	private void checkOrderExistance(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("Can't process discounts with unexistant order.");
		}
	}

}
