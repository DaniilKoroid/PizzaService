package ua.rd.pizzaservice.service.discount;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ua.rd.pizzaservice.domain.AccumulationCard;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.service.DiscountProvider;
import ua.rd.pizzaservice.service.DiscountService;
import ua.rd.pizzaservice.service.OrderPriceCalculatorService;
import ua.rd.pizzaservice.service.impl.AccumulationCardServiceImpl;
import ua.rd.pizzaservice.service.impl.DiscountServiceImpl;
import ua.rd.pizzaservice.service.impl.InMemDiscountProvider;

@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceImplTest {

	DiscountService discountService;

	@Mock
	AccumulationCardServiceImpl accCardService;
	
	@Mock
	OrderPriceCalculatorService orderPriceCalculatorService;
	
	@Mock
	Order order;

	@Mock
	Customer customerWithCard;

	@Mock
	Customer customerWithoutCard;

	@Spy
	AccumulationCard activatedCard;

	@Mock
	AccumulationCard notActivatedCard;

	@Mock
	Pizza pizzaOne;

	@Mock
	Pizza pizzaTwo;

	@Mock
	Pizza pizzaThree;

	@Mock
	Pizza pizzaFour;

	@BeforeClass
	public static void setUpAccumulationCardService() {
		
	}
	
	@Before
	public void setUpDiscountService() {
		DiscountProvider discountProvider = new InMemDiscountProvider();
		((InMemDiscountProvider) discountProvider).determineDiscounts();
		discountService = new DiscountServiceImpl(accCardService, discountProvider, orderPriceCalculatorService);
		double cardAmount = 100d;
		activatedCard.setAmount(cardAmount);
		activatedCard.setIsActivated(true);
		when(accCardService.hasAccumulationCard(customerWithCard)).thenReturn(true);
		when(accCardService.hasAccumulationCard(customerWithoutCard)).thenReturn(false);
		when(accCardService.getAccumulationCardByCustomer(customerWithCard)).thenReturn(activatedCard);
	}

	@Test
	public void testCalculateDiscountsAmountOnOrderWithoutDiscounts() {
		System.out.println("test calculateDiscountsAmount on order without discounts");
		when(order.getPizzas()).thenReturn(getUndiscountablePizzas());
		double discountsAmount = discountService.calculateDiscountsAmount(order);
		double expectedDiscountAmount = 0d;
		double eps = 1E-5;
		assertEquals(expectedDiscountAmount, discountsAmount, eps);
	}

	@Test
	public void testCalculateDiscountsAmountOnOrderWithDiscounts() {
		System.out.println("test calculateDiscountsAmount on order with discounts");
		when(order.getPizzas()).thenReturn(getDiscountablePizzas());
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		when(pizzaFour.getPrice()).thenReturn(100d);
		double eps = 1E-5;
		double expectedDiscount = 30d;
		double actualDiscount = discountService.calculateDiscountsAmount(order);
		assertEquals(expectedDiscount, actualDiscount, eps);
	}

	@Test
	public void testCalculatePriceWithDiscountsOnOrderWithoutDiscounts() {
		System.out.println("test calculate price with discounts on order without discounts");
		when(order.getPizzas()).thenReturn(getUndiscountablePizzas());
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice();
		when(orderPriceCalculatorService.getFullPrice(order)).thenReturn(sum);
		double priceWithDiscounts = discountService.calculatePriceWithDiscounts(order);
		double expectedPriceWithDiscounts = 205d;
		double eps = 1E-5;
		assertEquals(expectedPriceWithDiscounts, priceWithDiscounts, eps);
	}

	@Test
	public void testCalculatePriceWithDiscountsOnOrderWithDiscounts() {
		System.out.println("test calculate price with discounts on order without discounts");
		when(order.getPizzas()).thenReturn(getDiscountablePizzas());
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		when(pizzaFour.getPrice()).thenReturn(100d);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice() + pizzaFour.getPrice();
		when(orderPriceCalculatorService.getFullPrice(order)).thenReturn(sum);
		double priceWithDiscounts = discountService.calculatePriceWithDiscounts(order);
		double expectedPriceWithDiscounts = 275d;
		double eps = 1E-5;
		assertEquals(expectedPriceWithDiscounts, priceWithDiscounts, eps);
	}

	@Test
	public void testCalculateFinalDiscountAmountOnOrderWithoutDiscountsAndCustomerWithoutAccumulationCard() {
		System.out.println("test calculateFinalDiscountAmount on order without discounts "
				+ "and customer without accumulation card");
		when(order.getPizzas()).thenReturn(getUndiscountablePizzas());
		when(order.getCustomer()).thenReturn(customerWithoutCard);
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice();
		when(orderPriceCalculatorService.getFullPrice(order)).thenReturn(sum);
		double finalDiscountAmount = discountService.calculateFinalDiscountAmount(order);
		double expectedDiscountAmount = 0d;
		double eps = 1E-5;
		assertEquals(expectedDiscountAmount, finalDiscountAmount, eps);
	}

	@Test
	public void testCalculateFinalDiscountAmountOnOrderWithoutDiscountsAndCustomerWithAccumulationCard() {
		System.out.println("test calculateFinalDiscountAmount on order without discounts "
				+ "and customer with accumulation card");
		when(order.getPizzas()).thenReturn(getUndiscountablePizzas());
		when(order.getCustomer()).thenReturn(customerWithCard);
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice();
		when(orderPriceCalculatorService.getFullPrice(order)).thenReturn(sum);
		Double amountBefore = activatedCard.getAmount();
		double cardDiscount = calculateAccumulationCardServiceDiscount(activatedCard, sum);
		when(accCardService.calculateDiscount(activatedCard, sum)).thenReturn(cardDiscount);
		double finalDiscountAmount = discountService.calculateFinalDiscountAmount(order);
		double expectedDiscountAmount = 10d;
		double eps = 1E-5;
		assertEquals(expectedDiscountAmount, finalDiscountAmount, eps);
		activatedCard.setAmount(amountBefore);
	}

	@Test
	public void testCalculateFinalDiscountAmountOnOrderWithDiscountsAndCustomerWithoutAccumulationCard() {
		System.out.println("test calculateFinalDiscountAmount on order with discounts "
				+ "and customer without accumulation card");
		when(order.getPizzas()).thenReturn(getDiscountablePizzas());
		when(order.getCustomer()).thenReturn(customerWithoutCard);
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		when(pizzaFour.getPrice()).thenReturn(100d);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice() + pizzaFour.getPrice();
		when(orderPriceCalculatorService.getFullPrice(order)).thenReturn(sum);
		double finalDiscountAmount = discountService.calculateFinalDiscountAmount(order);
		double expectedDiscountAmount = 30d;
		double eps = 1E-5;
		assertEquals(expectedDiscountAmount, finalDiscountAmount, eps);
	}

	@Test
	public void testCalculateFinalDiscountAmountOnOrderWithDiscountsAndCustomerWithAccumulationCard() {
		System.out.println(
				"test calculateFinalDiscountAmount on order with discounts " + "and customer with accumulation card");
		Double amountBefore = activatedCard.getAmount();
		when(order.getPizzas()).thenReturn(getDiscountablePizzas());
		when(order.getCustomer()).thenReturn(customerWithCard);
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		when(pizzaFour.getPrice()).thenReturn(100d);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice() + pizzaFour.getPrice();
		when(orderPriceCalculatorService.getFullPrice(order)).thenReturn(sum);
		double fourPizzaDiscountPercentage = 0.3;
		double sumWithFourPizzaDiscount = sum - (pizzaFour.getPrice() * fourPizzaDiscountPercentage);
		double cardDiscount = calculateAccumulationCardServiceDiscount(activatedCard, sumWithFourPizzaDiscount);
		when(accCardService.calculateDiscount(activatedCard, sumWithFourPizzaDiscount)).thenReturn(cardDiscount);
		double finalDiscountAmount = discountService.calculateFinalDiscountAmount(order);
		double expectedDiscountAmount = 40d;
		double eps = 1E-5;
		assertEquals(expectedDiscountAmount, finalDiscountAmount, eps);
		activatedCard.setAmount(amountBefore);
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
	
	private double calculateAccumulationCardServiceDiscount(AccumulationCard card, Double orderPriceWithDiscounts) {
		double discountAmount = 0d;
		discountAmount = Math.min(calculateCardDiscount(card), calculateMaxDiscountWithGivenTotalPrice(orderPriceWithDiscounts));
		return discountAmount;
	}
	
	private double calculateCardDiscount(AccumulationCard card) {
		return card.getAmount() * card.getDiscountPercentage();
	}
	
	private double calculateMaxDiscountWithGivenTotalPrice(double orderTotalPrice) {
		return 0.3d * orderTotalPrice;
	}
	
}
