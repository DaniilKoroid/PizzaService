package ua.rd.pizzaservice.domain.accumulationcard;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;

public class AccumulationCardTest {

	AccumulationCard accumulationCard;
	
	@Before
	public void setUpAccumulationCard() {
		double baseAmount = 100d;
		Customer owner = null;
		int id = 0;
		accumulationCard = new AccumulationCard(id, baseAmount, owner);
	}
	
	@Test
	public void testUseDiscountWithCardPercentage() {
		System.out.println("test use discount with card percentage");
		double totalPrice = 100d;
		double expectedDiscount = 10d;
		double eps = 1E-5;
		double newCardAmount = accumulationCard.getAmount() + totalPrice;
		double discount = accumulationCard.use(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
		assertEquals(newCardAmount, accumulationCard.getAmount(), eps);
	}
	
	@Test
	public void testUseDiscountWithTotalPricePercentage() {
		System.out.println("test use discount with total price percentage");
		double totalPrice = 30d;
		double expectedDiscount = 9d;
		double eps = 1E-5;
		double newCardAmount = accumulationCard.getAmount() + totalPrice;
		double discount = accumulationCard.use(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
		assertEquals(newCardAmount, accumulationCard.getAmount(), eps);
	}
	
	@Test
	public void testUseDiscountWhenTotalPriceAndCardPercentagesAreEqual() {
		System.out.println("test use discount when total price and "
				+ "card percentages are equal");
		double cardAmount = 300d;
		accumulationCard.setAmount(cardAmount);
		double totalPrice = 100d;
		double expectedDiscount = 30d;
		double eps = 1E-5;
		double newCardAmount = accumulationCard.getAmount() + totalPrice;
		double discount = accumulationCard.use(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
		assertEquals(newCardAmount, accumulationCard.getAmount(), eps);
	}

	@Test
	public void testCalculateDiscountWithCardPercentage() {
		System.out.println("test calculate discount with card percentage");
		double totalPrice = 100d;
		double expectedDiscount = 10d;
		double eps = 1E-5;
		double discount = accumulationCard.calculateDiscount(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
	}
	
	@Test
	public void testCalculateDiscountWithTotalPricePercentage() {
		System.out.println("test calculate discount with total price percentage");
		double totalPrice = 30d;
		double expectedDiscount = 9d;
		double eps = 1E-5;
		double discount = accumulationCard.calculateDiscount(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
	}
	
	@Test
	public void testCalculateDiscountWhenTotalPriceAndCardPercentagesAreEqual() {
		System.out.println("test calculate discount when total price and "
				+ "card percentages are equal");
		double cardAmount = 300d;
		accumulationCard.setAmount(cardAmount);
		double totalPrice = 100d;
		double expectedDiscount = 30d;
		double eps = 1E-5;
		double discount = accumulationCard.calculateDiscount(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
	}

}
