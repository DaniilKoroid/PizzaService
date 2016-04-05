package ua.rd.pizzaservice.domain.accumulationcard;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AccumulationCardTest {

	AccumulationCard card;

	@Before
	public void setUpAccumulationCard() {
		Double baseAmount = 100d;
		card = new AccumulationCard();
		card.setAmount(baseAmount);

	}

	@Test
	public void testUseDiscountWithCardPercentage() {
		System.out.println("test use discount with card percentage");
		double totalPrice = 100d;
		double expectedDiscount = 10d;
		double eps = 1E-5;
		double newCardAmount = card.getAmount() + totalPrice;
		double discount = card.use(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
		assertEquals(newCardAmount, card.getAmount(), eps);
	}

	@Test
	public void testUseDiscountWithTotalPricePercentage() {
		System.out.println("test use discount with total price percentage");
		double totalPrice = 30d;
		double expectedDiscount = 9d;
		double eps = 1E-5;
		double newCardAmount = card.getAmount() + totalPrice;
		double discount = card.use(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
		assertEquals(newCardAmount, card.getAmount(), eps);
	}

	@Test
	public void testUseDiscountWhenTotalPriceAndCardPercentagesAreEqual() {
		System.out.println("test use discount when total price and " + "card percentages are equal");
		double cardAmount = 300d;
		card.setAmount(cardAmount);
		double totalPrice = 100d;
		double expectedDiscount = 30d;
		double eps = 1E-5;
		double newCardAmount = card.getAmount() + totalPrice;
		double discount = card.use(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
		assertEquals(newCardAmount, card.getAmount(), eps);
	}

	@Test
	public void testCalculateDiscountWithCardPercentage() {
		System.out.println("test calculate discount with card percentage");
		double totalPrice = 100d;
		double expectedDiscount = 10d;
		double eps = 1E-5;
		double discount = card.calculateDiscount(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
	}

	@Test
	public void testCalculateDiscountWithTotalPricePercentage() {
		System.out.println("test calculate discount with total price percentage");
		double totalPrice = 30d;
		double expectedDiscount = 9d;
		double eps = 1E-5;
		double discount = card.calculateDiscount(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
	}

	@Test
	public void testCalculateDiscountWhenTotalPriceAndCardPercentagesAreEqual() {
		System.out.println("test calculate discount when total price and " + "card percentages are equal");
		double cardAmount = 300d;
		card.setAmount(cardAmount);
		double totalPrice = 100d;
		double expectedDiscount = 30d;
		double eps = 1E-5;
		double discount = card.calculateDiscount(totalPrice);
		assertEquals(expectedDiscount, discount, eps);
	}

}
