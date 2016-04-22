package ua.rd.pizzaservice.repository.discount;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.service.discount.DiscountProvider;
import ua.rd.pizzaservice.service.discount.InMemDiscountProvider;

@RunWith(MockitoJUnitRunner.class)
public class InMemDiscountRepositoryTest {

	@Mock
	Order order;

	@Mock
	Pizza pizzaOne;

	@Mock
	Pizza pizzaTwo;

	@Mock
	Pizza pizzaThree;

	@Mock
	Pizza pizzaFour;

	DiscountProvider discountRepository;

	@Before
	public void setUpDiscountRepository() {
		discountRepository = new InMemDiscountProvider();
		((InMemDiscountProvider) discountRepository).determineDiscounts();
	}

	@Test
	public void testGetAppliableDiscountsReturnsForOrderWithAppliableDiscountsListWithDiscounts() {
		System.out.println(
				"test getAppliableDiscounts returns for order with appliable discounts " + "list with discounts");
		when(order.getPizzas()).thenReturn(getDiscountablePizzas());
		List<Discount> appliableDiscounts = discountRepository.getAppliableDiscounts(order);
		int expectedSize = 1;
		int actualSize = appliableDiscounts.size();
		assertEquals(expectedSize, actualSize);
	}

	@Test
	public void testGetAppliableDiscountsReturnsEmptyListWhenThereAreNoAppliableDiscounts() {
		System.out.println("test getAppliableDiscounts returns empty list when there are no " + "appliable discounts");
		when(order.getPizzas()).thenReturn(getUndiscountablePizzas());
		List<Discount> appliableDiscounts = discountRepository.getAppliableDiscounts(order);
		int expectedSize = 0;
		int actualSize = appliableDiscounts.size();
		assertEquals(expectedSize, actualSize);
		assertEquals(Collections.emptyList(), appliableDiscounts);
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
