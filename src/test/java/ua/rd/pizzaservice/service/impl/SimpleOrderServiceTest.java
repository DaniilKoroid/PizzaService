package ua.rd.pizzaservice.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ua.rd.pizzaservice.domain.AccumulationCard;
import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.OrderState;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.repository.OrderRepository;
import ua.rd.pizzaservice.repository.inmem.impl.InMemOrderRepository;
import ua.rd.pizzaservice.service.AccumulationCardService;
import ua.rd.pizzaservice.service.CustomerService;
import ua.rd.pizzaservice.service.DiscountProvider;
import ua.rd.pizzaservice.service.DiscountService;
import ua.rd.pizzaservice.service.OrderPriceCalculatorService;
import ua.rd.pizzaservice.service.OrderService;
import ua.rd.pizzaservice.service.impl.PizzaServiceImpl;
import ua.rd.pizzaservice.service.impl.DiscountServiceImpl;
import ua.rd.pizzaservice.service.impl.InMemDiscountProvider;
import ua.rd.pizzaservice.service.impl.OrderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SimpleOrderServiceTest {

	Customer customer;
	Address address;
	OrderService orderService;
	DiscountService discountService;

	@Mock
	PizzaServiceImpl pizzaService;
	
	@Mock
	AccumulationCardService accCardService;

	@Mock
	CustomerService customerService;
	
	@Mock
	Customer customerWithCard;

	@Mock
	Customer customerWithoutCard;

	@Spy
	AccumulationCard activatedCard;

	@Mock
	AccumulationCard notActivatedCard;
	
	@Mock
	OrderPriceCalculatorService orderPriceCalculatorService;

	@Mock
	Order undiscountedOrder;

	@Mock
	Order discountedOrder;

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

	@Mock
	OrderServiceImpl mockedSimpleOrderService;

	@Before
	public void setUpVariables() {
		when(pizzaService.getPizzaByID(1)).thenReturn(pizzaOne);
		when(pizzaService.getPizzaByID(2)).thenReturn(pizzaTwo);
		when(pizzaService.getPizzaByID(3)).thenReturn(pizzaThree);
		when(pizzaService.getPizzaByID(4)).thenReturn(pizzaFour);
		when(customerService.proposeAddress(any(Address.class), any(Customer.class))).thenReturn(customer);
		DiscountProvider discountProvider = new InMemDiscountProvider();
		((InMemDiscountProvider) discountProvider).determineDiscounts();
		OrderRepository orderRepository = new InMemOrderRepository();
		discountService = new DiscountServiceImpl(accCardService, discountProvider, orderPriceCalculatorService);
		orderService = new OrderServiceImpl(discountService, accCardService, pizzaService, orderRepository, customerService, orderPriceCalculatorService);
		double cardAmount = 100d;
		activatedCard.setAmount(cardAmount);
		when(accCardService.hasAccumulationCard(customerWithCard)).thenReturn(true);
		when(accCardService.hasAccumulationCard(customerWithoutCard)).thenReturn(false);
		when(accCardService.getAccumulationCardByCustomer(customerWithCard)).thenReturn(activatedCard);
		String name = "";
		customer = new Customer(name);
		address = new Address();
		address.setCountry("Ukraine");
		when(pizzaOne.getPrice()).thenReturn(60d);
		when(pizzaTwo.getPrice()).thenReturn(70d);
		when(pizzaThree.getPrice()).thenReturn(75d);
		when(pizzaFour.getPrice()).thenReturn(100d);
		double discountedSum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice() + pizzaFour.getPrice();
		double undiscountedSum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice();
		when(orderPriceCalculatorService.getFullPrice(undiscountedOrder)).thenReturn(undiscountedSum);
		when(orderPriceCalculatorService.getFullPrice(discountedOrder)).thenReturn(discountedSum);
		when(undiscountedOrder.getPizzas()).thenReturn(getUndiscountablePizzas());
		when(discountedOrder.getPizzas()).thenReturn(getDiscountablePizzas());
		when(mockedSimpleOrderService.createOrder()).thenReturn(new Order());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlaceNewOrderWithTooMuchPizzasThrowsIllegalArgumentException() {
		System.out.println("test placeNewOrder with too much pizzas throws IllegalArgumentException");
		int length = 11;
		Integer[] arr = new Integer[length];
		orderService.placeNewOrder(customer, address, arr);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlaceNewOrderWithZeroPizzasThrowsIllegalArgumentException() {
		System.out.println("test placeNewOrder with zero pizzas throws IllegalArgumentException");
		int length = 0;
		Integer[] arr = new Integer[length];
		orderService.placeNewOrder(customer, address, arr);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPlaceNewOrderWithNullCustomerThrowsIllegalArgumentException() {
		System.out.println("test placeNewOrder with null customer throws IllegalArgumentException");
		int pizzaCount = 5;
		Integer[] arr = new Integer[pizzaCount];
		Customer nullCustomer = null;
		orderService.placeNewOrder(nullCustomer, address, arr);
	}

	@Test
	public void testCanChangeOrderWithNewStateReturnsTrue() {
		System.out.println("test canChange order with NEW state returns true");
		OrderState state = OrderState.NEW;
		Order order = new Order();
		order.setState(state);
		assertTrue(orderService.canChange(order));
	}

	@Test
	public void testCanChangeOrderWithInProgressStateReturnsFalse() {
		System.out.println("test canChange order with IN_PROGRESS state returns false");
		OrderState state = OrderState.IN_PROGRESS;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.canChange(order));
	}

	@Test
	public void testCanChangeOrderWithDoneStateReturnsFalse() {
		System.out.println("test canChange order with DONE state returns false");
		OrderState state = OrderState.DONE;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.canChange(order));
	}

	@Test
	public void testCanChangeOrderWithCancelledStateReturnsFalse() {
		System.out.println("test canChange order with CANCELLED state returns false");
		OrderState state = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.canChange(order));
	}

	@Test
	public void testChangeOrderWithNewStateAndAppropriatePizzaCountReturnsTrue() {
		System.out.println("test changeOrder with NEW state and appropriate pizza count " + "returns true");
		Integer[] arr = new Integer[]{1, 1, 2, 3, 3};
		OrderState state = OrderState.NEW;
		Order order = new Order();
		order.setState(state);
		assertTrue(orderService.changeOrder(order, arr));
	}

	@Test
	public void testChangeOrderWithInProgressStateAndAppropriatePizzaCountReturnsFalse() {
		System.out.println("test changeOrder with IN_PROGRESS state and appropriate " + "pizza count returns false");
		int pizzaCount = 5;
		Integer[] arr = new Integer[pizzaCount];
		OrderState state = OrderState.IN_PROGRESS;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.changeOrder(order, arr));
	}

	@Test
	public void testChangeOrderWithDoneStateAndAppropriatePizzaCountReturnsFalse() {
		System.out.println("test changeOrder with DONE state and appropriate " + "pizza count returns false");
		int pizzaCount = 5;
		Integer[] arr = new Integer[pizzaCount];
		OrderState state = OrderState.DONE;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.changeOrder(order, arr));
	}

	@Test
	public void testChangeOrderWithCancelledStateAndAppropriatePizzaCountReturnsFalse() {
		System.out.println("test changeOrder with CANCELLED state and appropriate " + "pizza count returns false");
		int pizzaCount = 5;
		Integer[] arr = new Integer[pizzaCount];
		OrderState state = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.changeOrder(order, arr));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChangeOrderWithNewStateAndNotAppropriatePizzaCountThrowsIllegalArgumentException() {
		System.out.println(
				"test changeOrder with NEW state and not appropriate pizza count " + "throws IllegalArgumentException");
		int pizzaCount = 11;
		Integer[] arr = new Integer[pizzaCount];
		OrderState state = OrderState.NEW;
		Order order = new Order();
		order.setState(state);
		orderService.changeOrder(order, arr);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChangeOrderWithInProgressStateAndNotAppropriatePizzaCountThrowsIllegalArgumentException() {
		System.out.println("test changeOrder with IN_PROGRESS state and not appropriate "
				+ "pizza count throws IllegalArgumentException");
		int pizzaCount = 11;
		Integer[] arr = new Integer[pizzaCount];
		OrderState state = OrderState.IN_PROGRESS;
		Order order = new Order();
		order.setState(state);
		orderService.changeOrder(order, arr);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChangeOrderWithDoneStateNotAppropriatePizzaCountThrowsIllegalArgumentException() {
		System.out.println("test changeOrder with DONE state and not appropriate "
				+ "pizza count throws IllegalArgumentException");
		int pizzaCount = 11;
		Integer[] arr = new Integer[pizzaCount];
		OrderState state = OrderState.DONE;
		Order order = new Order();
		order.setState(state);
		orderService.changeOrder(order, arr);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChangeOrderWithCancelledStateAndNotAppropriatePizzaCountThrowsIllegalArgumentException() {
		System.out.println("test changeOrder with CANCELLED state and not appropriate "
				+ "pizza count throws IllegalArgumentException");
		int pizzaCount = 11;
		Integer[] arr = new Integer[pizzaCount];
		OrderState state = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(state);
		orderService.changeOrder(order, arr);
	}

	@Test
	public void testProcessOrderWithNewStateReturnsTrue() {
		System.out.println("test processOrder with NEW state returns true");
		OrderState state = OrderState.NEW;
		Order order = new Order();
		order.setState(state);
		assertTrue(orderService.processOrder(order));
	}

	@Test
	public void testProcessOrderWithNewStateChangesStateToInProgress() {
		System.out.println("test processOrder with NEW state changes state to IN_PROGRESS");
		OrderState stateFrom = OrderState.NEW;
		OrderState stateTo = OrderState.IN_PROGRESS;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.processOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testProcessOrderWithInProgressStateReturnsFalse() {
		System.out.println("test processOrder with IN_PROGRESS state returns false");
		OrderState state = OrderState.IN_PROGRESS;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.processOrder(order));
	}

	@Test
	public void testProcessOrderWithInProgressStateDontChangesState() {
		System.out.println("test processOrder with IN_PROGRESS state dont changes state");
		OrderState stateFrom = OrderState.IN_PROGRESS;
		OrderState stateTo = OrderState.IN_PROGRESS;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.processOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testProcessOrderWithDoneStateReturnsFalse() {
		System.out.println("test processOrder with DONE state returns false");
		OrderState state = OrderState.DONE;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.processOrder(order));
	}

	@Test
	public void testProcessOrderWithDoneStateDontChangesState() {
		System.out.println("test processOrder with DONE state dont changes state");
		OrderState stateFrom = OrderState.DONE;
		OrderState stateTo = OrderState.DONE;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.processOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testProcessOrderWithCancelledStateReturnsFalse() {
		System.out.println("test processOrder with CANCELLED state returns false");
		OrderState state = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.processOrder(order));
	}

	@Test
	public void testProcessOrderWithCancelledStateDontChangesState() {
		System.out.println("test processOrder with CANCELLED state dont changes state");
		OrderState stateFrom = OrderState.CANCELLED;
		OrderState stateTo = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.processOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testCancelOrderWithNewStateReturnsTrue() {
		System.out.println("test cancelOrder with NEW state returns true");
		OrderState state = OrderState.NEW;
		Order order = new Order();
		order.setState(state);
		assertTrue(orderService.cancelOrder(order));
	}

	@Test
	public void testCancelOrderWithNewStateChangesStateToCancelled() {
		System.out.println("test cancelOrder with NEW state changes state to cancelled");
		OrderState stateFrom = OrderState.NEW;
		OrderState stateTo = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.cancelOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testCancelOrderWithInProgressStateReturnsTrue() {
		System.out.println("test cancelOrder with IN_PROGRESS state returns true");
		OrderState state = OrderState.IN_PROGRESS;
		Order order = new Order();
		order.setState(state);
		assertTrue(orderService.cancelOrder(order));
	}

	@Test
	public void testCancelOrderWithInProgressStateChangesStateToCancelled() {
		System.out.println("test cancelOrder with IN_PROGRESS state changes state to cancelled");
		OrderState stateFrom = OrderState.IN_PROGRESS;
		OrderState stateTo = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.cancelOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testCancelOrderWithDoneStateReturnsFalse() {
		System.out.println("test cancelOrder with DONE state returns false");
		OrderState state = OrderState.DONE;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.cancelOrder(order));
	}

	@Test
	public void testCancelOrderWithDoneStateDontChangesState() {
		System.out.println("test cancelOrder with DONE state dont changes state");
		OrderState stateFrom = OrderState.DONE;
		OrderState stateTo = OrderState.DONE;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.cancelOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testCancelOrderWithCancelledStateReturnsFalse() {
		System.out.println("test cancelOrder with CANCELLED state returns false");
		OrderState state = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.cancelOrder(order));
	}

	@Test
	public void testCancelOrderWithCancelledStateDontChangesState() {
		System.out.println("test cancelOrder with CANCELLED state dont changes state");
		OrderState stateFrom = OrderState.CANCELLED;
		OrderState stateTo = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.cancelOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testDoneOrderWithNewStateReturnsFalse() {
		System.out.println("test doneOrder with NEW state returns false");
		OrderState state = OrderState.NEW;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.doneOrder(order));
	}

	@Test
	public void testDoneOrderWithNewStateDontChangesState() {
		System.out.println("test doneOrder with NEW state dont changes state");
		OrderState stateFrom = OrderState.NEW;
		OrderState stateTo = OrderState.NEW;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.doneOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testDoneOrderWithInProgressStateReturnsTrue() {
		System.out.println("test doneOrder with IN_PROGRESS state returns true");
		OrderState state = OrderState.IN_PROGRESS;
		Order order = new Order();
		order.setCustomer(customer);
		order.setState(state);
		assertTrue(orderService.doneOrder(order));
	}

	@Test
	public void testDoneOrderWithInProgressStateChangesStateToDone() {
		System.out.println("test doneOrder with IN_PROGRESS state changes state to DONE");
		OrderState stateFrom = OrderState.IN_PROGRESS;
		OrderState stateTo = OrderState.DONE;
		Order order = new Order();
		order.setState(stateFrom);
		order.setCustomer(customer);
		orderService.doneOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testDoneOrderWithDoneStateReturnsFalse() {
		System.out.println("test doneOrder with DONE state returns false");
		OrderState state = OrderState.DONE;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.doneOrder(order));
	}

	@Test
	public void testDoneOrderWithDoneStateDontChangesState() {
		System.out.println("test doneOrder with DONE state dont changes state");
		OrderState stateFrom = OrderState.DONE;
		OrderState stateTo = OrderState.DONE;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.doneOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testDoneOrderWithCancelledStateReturnsFalse() {
		System.out.println("test doneOrder with CANCELLED state returns false");
		OrderState state = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(state);
		assertFalse(orderService.doneOrder(order));
	}

	@Test
	public void testDoneOrderWithCancelledStateDontChangesState() {
		System.out.println("test doneOrder with CANCELLED state dont changes state");
		OrderState stateFrom = OrderState.CANCELLED;
		OrderState stateTo = OrderState.CANCELLED;
		Order order = new Order();
		order.setState(stateFrom);
		orderService.doneOrder(order);
		assertEquals(stateTo, order.getState());
	}

	@Test
	public void testGetFinalPriceOfOrderWithoutDiscountsAndWithoutAccumulationCard() {
		System.out.println("test getFinalPrice of order without discounts and " + "without accumulation card");
		when(undiscountedOrder.getCustomer()).thenReturn(customerWithoutCard);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice();
		Double finalPrice = orderService.getFinalPrice(undiscountedOrder);
		double eps = 1E-5;
		assertEquals(sum, finalPrice, eps);
	}

	@Test
	public void testGetFinalPriceOfOrderWithoutDiscountsAndWithAccumulationCard() {
		System.out.println("test getFinalPrice of order without discounts and with accumulation card");
		when(undiscountedOrder.getCustomer()).thenReturn(customerWithCard);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice();
		double cardDiscount = calculateAccumulationCardServiceDiscount(activatedCard, sum);
		when(accCardService.calculateDiscount(activatedCard, sum)).thenReturn(cardDiscount);
		double finalPrice = orderService.getFinalPrice(undiscountedOrder);
		double expectedFinalPrice = sum - 10d;
		double eps = 1E-5;
		assertEquals(expectedFinalPrice, finalPrice, eps);
	}

	@Test
	public void testGetFinalPriceOfOrderWithDiscountsAndWithoutAccumulationCard() {
		System.out.println("test getFinalPrice of order with discounts and without accumulation card");
		when(discountedOrder.getCustomer()).thenReturn(customerWithoutCard);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice() + pizzaFour.getPrice();
		double finalPrice = orderService.getFinalPrice(discountedOrder);
		double expectedFinalPrice = sum - 30d;
		double eps = 1E-5;
		assertEquals(expectedFinalPrice, finalPrice, eps);
	}

	@Test
	public void testGetFinalPriceOfOrderWithDiscountsAndWithAccumulationCard() {
		System.out.println("test getFinalPrice of order with discounts and with accumulation card");
		when(discountedOrder.getCustomer()).thenReturn(customerWithCard);
		double sum = pizzaOne.getPrice() + pizzaTwo.getPrice() + pizzaThree.getPrice() + pizzaFour.getPrice();
		double discountedOrderPrice = sum - pizzaFour.getPrice() * 0.3d;
		double cardDiscount = calculateAccumulationCardServiceDiscount(activatedCard, discountedOrderPrice);
		when(accCardService.calculateDiscount(activatedCard, discountedOrderPrice)).thenReturn(cardDiscount);
		double finalPrice = orderService.getFinalPrice(discountedOrder);
		double expectedFinalPrice = discountedOrderPrice - cardDiscount;
		double eps = 1E-5;
		assertEquals(expectedFinalPrice, finalPrice, eps);
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
