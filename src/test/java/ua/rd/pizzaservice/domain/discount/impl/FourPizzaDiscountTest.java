package ua.rd.pizzaservice.domain.discount.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.discount.impl.FourPizzaDiscount;

@RunWith(MockitoJUnitRunner.class)
public class FourPizzaDiscountTest {

	Discount discount;

	double discountPercentage;

	@Mock
	Order mockedOrder;

	@Mock
	Pizza pizzaOne;

	@Mock
	Pizza pizzaTwo;

	@Mock
	Pizza pizzaThree;

	@Mock
	Pizza pizzaFour;

	@Before
	public void setUpVariables() {
		discount = new FourPizzaDiscount();
		discountPercentage = FourPizzaDiscount.DISCOUNT_PERCENTAGE_FOR_MAX_PRICED_PIZZA;
	}

	@Test
	public void testDiscountIsAppliableWithAppropriateOrder() {
		System.out.println("test discount is appliable with appropriate order");
		when(mockedOrder.getPizzas()).thenReturn(getDiscountablePizzas());
		assertTrue(discount.isAppliable(mockedOrder));
	}

	@Test
	public void testDiscountIsNotAppliableWithNotAppropriateOrder() {
		System.out.println("test discount is appliable with appropriate order");
		when(mockedOrder.getPizzas()).thenReturn(getUndiscountablePizzas());
		assertFalse(discount.isAppliable(mockedOrder));
	}

	@Test
	public void testDiscountNotToWorkWithLessThanNeededPizzaCount() {
		System.out.println("test discount not to work with less than needed pizza count");
		when(mockedOrder.getPizzas()).thenReturn(getUndiscountablePizzas());
		double expectedDiscount = 0d;
		double eps = 1E-5;
		double calculateDiscount = discount.calculateDiscount(mockedOrder);
		assertEquals(expectedDiscount, calculateDiscount, eps);
	}

	@Test
	public void testDiscountWithFourPizzasOfDifferentPrice() {
		System.out.println("test discount with four pizzas of different price");
		when(mockedOrder.getPizzas()).thenReturn(getDiscountablePizzas());
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		when(pizzaFour.getPrice()).thenReturn(80d);
		double maxPizzaPrice = 80d;
		double eps = 1E-5;
		double expectedDiscount = maxPizzaPrice * discountPercentage;
		double actualDiscount = discount.calculateDiscount(mockedOrder);
		assertEquals(expectedDiscount, actualDiscount, eps);
	}

	@Test
	public void testDiscountWithFourPizzasOfEqualPrice() {
		System.out.println("test discount with four pizzas of equal price");
		when(mockedOrder.getPizzas()).thenReturn(getDiscountablePizzas());
		double equalPrice = 100d;
		when(pizzaOne.getPrice()).thenReturn(equalPrice);
		when(pizzaTwo.getPrice()).thenReturn(equalPrice);
		when(pizzaThree.getPrice()).thenReturn(equalPrice);
		when(pizzaFour.getPrice()).thenReturn(equalPrice);
		double eps = 1E-5;
		double expectedDiscount = equalPrice * discountPercentage;
		double actualDiscount = discount.calculateDiscount(mockedOrder);
		assertEquals(expectedDiscount, actualDiscount, eps);
	}

	private Map<Pizza, Integer> getDiscountablePizzas() {
		Map<Pizza, Integer> discountablePizzas = new HashMap<Pizza, Integer>() {
			private static final long serialVersionUID = -8644030995186868572L;

			{
				put(pizzaOne, 1);
				put(pizzaTwo, 1);
				put(pizzaThree, 1);
				put(pizzaFour, 1);
			}
		};
		return discountablePizzas;
	}

	private Map<Pizza, Integer> getUndiscountablePizzas() {
		Map<Pizza, Integer> undiscountablePizzas = new HashMap<Pizza, Integer>() {
			private static final long serialVersionUID = 1023848264106081126L;

			{
				put(pizzaOne, 1);
				put(pizzaTwo, 1);
				put(pizzaThree, 1);
			}
		};
		return undiscountablePizzas;
	}
}
