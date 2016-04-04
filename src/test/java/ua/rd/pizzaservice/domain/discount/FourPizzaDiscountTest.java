package ua.rd.pizzaservice.domain.discount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;

@RunWith(MockitoJUnitRunner.class)
public class FourPizzaDiscountTest {

	Discount discount;

	double discountPercentage;

	@Mock
	Order mockedOrder;

	@Mock
	List<Pizza> pizzaList;

	@Mock
	Pizza pizzaOne;

	@Mock
	Pizza pizzaTwo;

	@Mock
	Pizza pizzaThree;

	@Mock
	Pizza pizzaFour;

	@Before
	public void setUpDiscount() {
		discount = new FourPizzaDiscount();
		discountPercentage = FourPizzaDiscount.DISCOUNT_PERCENTAGE_FOR_MAX_PRICED_PIZZA;
	}

	@Test
	public void testDiscountIsAppliableWithAppropriateOrder() {
		System.out.println("test discount is appliable with appropriate order");
		Integer pizzaListSize = 4;
		when(mockedOrder.getPizzas()).thenReturn(pizzaList);
		when(pizzaList.size()).thenReturn(pizzaListSize);
		assertTrue(discount.isAppliable(mockedOrder));
	}

	@Test
	public void testDiscountIsNotAppliableWithNotAppropriateOrder() {
		System.out.println("test discount is appliable with appropriate order");
		Integer pizzaListSize = 3;
		when(mockedOrder.getPizzas()).thenReturn(pizzaList);
		when(pizzaList.size()).thenReturn(pizzaListSize);
		assertFalse(discount.isAppliable(mockedOrder));
	}

	@Test
	public void testDiscountNotToWorkWithLessThanNeededPizzaCount() {
		System.out.println("test discount not to work with less than needed pizza count");
		Integer lowPizzaListSize = 3;
		when(mockedOrder.getPizzas()).thenReturn(pizzaList);
		when(pizzaList.size()).thenReturn(lowPizzaListSize);
		double expectedDiscount = 0d;
		double eps = 1E-5;
		double calculateDiscount = discount.calculateDiscount(mockedOrder);
		assertEquals(expectedDiscount, calculateDiscount, eps);
	}

	@Test
	public void testDiscountWithFourPizzasOfDifferentPrice() {
		System.out.println("test discount with four pizzas of different price");
		when(mockedOrder.getPizzas()).thenReturn(getMockedPizzas());
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
		when(mockedOrder.getPizzas()).thenReturn(getMockedPizzas());
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

	private List<Pizza> getMockedPizzas() {
		@SuppressWarnings("serial")
		List<Pizza> mockedPizzasList = new ArrayList<Pizza>() {
			{
				add(pizzaOne);
				add(pizzaTwo);
				add(pizzaThree);
				add(pizzaFour);
			}
		};
		return mockedPizzasList;
	}
}
