package ua.rd.pizzaservice.domain.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.discount.FourPizzaDiscount;
import ua.rd.pizzaservice.domain.order.Order.OrderStatus;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

public class OrderTest {

	Order order;
	
	@Before
	public void setUpOrder() {
		order = new Order();
	}
	
	@Test
	public void testChangeOrderFromNewStatusReturnsTrue() {
		System.out.println("test change order from NEW status returns true");
		order.setStatus(OrderStatus.NEW);
		List<Pizza> atStartPizzas = order.getPizzas();
		@SuppressWarnings("serial")
		List<Pizza> newPizzas = new ArrayList<Pizza>() {
			{
				add(new Pizza());
			}
		};
		assertNotEquals(atStartPizzas, newPizzas);
		boolean orderChanged = order.changeOrder(newPizzas);
		List<Pizza> afterChangePizzas = order.getPizzas();
		assertTrue(orderChanged);
		assertEquals(newPizzas, afterChangePizzas);
	}
	
	@Test
	public void testChangeOrderFromInProgressStatusReturnsFalse() {
		System.out.println("test change order from IN_PROGRESS status returns false");
		order.setStatus(OrderStatus.IN_PROGRESS);
		List<Pizza> atStartPizzas = order.getPizzas();
		@SuppressWarnings("serial")
		List<Pizza> newPizzas = new ArrayList<Pizza>() {
			{
				add(new Pizza());
			}
		};
		assertNotEquals(atStartPizzas, newPizzas);
		boolean orderChanged = order.changeOrder(newPizzas);
		List<Pizza> afterChangePizzas = order.getPizzas();
		assertFalse(orderChanged);
		assertNotEquals(newPizzas, afterChangePizzas);
	}
	
	@Test
	public void testChangeOrderFromCancelledStatusReturnsFalse() {
		System.out.println("test change order from CANCELLED status returns false");
		order.setStatus(OrderStatus.CANCELLED);
		List<Pizza> atStartPizzas = order.getPizzas();
		@SuppressWarnings("serial")
		List<Pizza> newPizzas = new ArrayList<Pizza>() {
			{
				add(new Pizza());
			}
		};
		assertNotEquals(atStartPizzas, newPizzas);
		boolean orderChanged = order.changeOrder(newPizzas);
		List<Pizza> afterChangePizzas = order.getPizzas();
		assertFalse(orderChanged);
		assertNotEquals(newPizzas, afterChangePizzas);
	}
	
	@Test
	public void testChangeOrderFromDoneStatusReturnsFalse() {
		System.out.println("test change order from DONE status returns false");
		order.setStatus(OrderStatus.DONE);
		List<Pizza> atStartPizzas = order.getPizzas();
		@SuppressWarnings("serial")
		List<Pizza> newPizzas = new ArrayList<Pizza>() {
			{
				add(new Pizza());
			}
		};
		assertNotEquals(atStartPizzas, newPizzas);
		boolean orderChanged = order.changeOrder(newPizzas);
		List<Pizza> afterChangePizzas = order.getPizzas();
		assertFalse(orderChanged);
		assertNotEquals(newPizzas, afterChangePizzas);
	}

	@Test
	public void testCalculateTotalPriceDontDependsOnOrderStatus() {
		System.out.println("test calculate total price dont depends on order status");
		List<Pizza> pizzaList = getDefaultPizzaList();
		order.setPizzas(pizzaList);
		double totalPrice = calculateTotalPrice(pizzaList);
		double eps = 1E-5;
		OrderStatus[] orderStatusValues = OrderStatus.values();
		for (OrderStatus orderStatus : orderStatusValues) {
			order.setStatus(orderStatus);
			assertEquals(totalPrice, order.calculateTotalPrice(), eps);
		}
	}
	
	@Test
	public void testCalculateTotalPrice() {
		System.out.println("test calculate total price");
		List<Pizza> pizzaList = getDefaultPizzaList();
		order.setPizzas(pizzaList);
		double expectedTotalPrice = calculateTotalPrice(pizzaList);
		double actualTotalPrice = order.calculateTotalPrice();
		double eps = 1E-5;
		assertEquals(expectedTotalPrice, actualTotalPrice, eps);
	}

	@Test
	public void testCalculateTotalPriceDontDependsOnDiscount() {
		System.out.println("test calculate total price dont depends on discount");
		List<Pizza> discountablePizzaList = getFourPizzaDiscountablePizzaList();
		order.setPizzas(discountablePizzaList);
		double expectedTotalPrice = order.calculateTotalPrice();
		Discount discount = new FourPizzaDiscount();
		order.setDiscount(discount);
		double actualTotalPrice = order.calculateTotalPrice();
		double eps = 1E-5;
		assertEquals(expectedTotalPrice, actualTotalPrice, eps);
		
	}

	@Test
	public void testCalculateTotalPriceDontDependsOnAccumulationCard() {
		System.out.println("test calculate total price dont depends on accumulation card");
		List<Pizza> pizzaList = getDefaultPizzaList();
		order.setPizzas(pizzaList);
		double expectedTotalPrice = calculateTotalPrice(pizzaList);
		double eps = 1E-5;
		Customer customer = new Customer();
		AccumulationCard card = new AccumulationCard();
		customer.setAccumulationCard(card);
		order.setCustomer(customer);
		double actualTotalPrice = order.calculateTotalPrice();
		assertEquals(expectedTotalPrice, actualTotalPrice, eps);
	}

	@Test
	public void testCalculateDiscountIsZeroWhenDiscountIsNull() {
		System.out.println("test calculate discount is zero when discount is null");
		List<Pizza> pizzaList = getDefaultPizzaList();
		order.setPizzas(pizzaList);
		order.setDiscount(null);
		double expectedDiscount = order.calculateDiscount();
		double eps = 1E-5;
		assertEquals(0d, expectedDiscount, eps);
	}
	
	@Test
	public void testCalculateDiscountWithFourPizzaDiscount() {
		System.out.println("test calculate discount with four pizza discount");
		List<Pizza> discountablePizzaList = getFourPizzaDiscountablePizzaList();
		Discount discount = new FourPizzaDiscount();
		order.setPizzas(discountablePizzaList);
		order.setDiscount(discount);
		double discountPrecentage = 0.3d;
		double expectedDiscount = getMaxPrice(discountablePizzaList) * discountPrecentage;
		double actualDiscount = order.calculateDiscount();
		double eps = 1E-5;
		assertEquals(expectedDiscount, actualDiscount, eps);
	}

	@Test
	public void testCalculateAccumulationCardDiscountIsZeroWithoutAccumulationCard() {
		System.out.println("test calculate accumulation card discount is zero without accumulation card");
		Customer customer = new Customer();
		customer.setAccumulationCard(null);
		order.setCustomer(customer);
		List<Pizza> pizzaList = getDefaultPizzaList();
		order.setPizzas(pizzaList);
		double accumulativeCardDiscount = order.calculateAccumulativeCardDiscount();
		double eps = 1E-5;
		assertEquals(0, accumulativeCardDiscount, eps);
	}
	
	@Test
	public void testCalculateAccumulativeCardDiscountWithAccumulationCard() {
		System.out.println("test calculate accumulative card discount with accumulative card");
		AccumulationCard card = new AccumulationCard();
		double amount = 10d;
		double cardPercentage = 0.1d;
		card.setAmount(amount);
		Customer customer = new Customer();
		customer.setAccumulationCard(card);
		order.setCustomer(customer);
		List<Pizza> pizzaList = getDefaultPizzaList();
		order.setPizzas(pizzaList);
		double totalPrice = order.calculateTotalPrice();
		double pricePercentage = 0.3d;
		double cardDiscount = order.calculateAccumulativeCardDiscount();
		double eps = 1E-5;
		double expectedCardDiscount = Math.min(amount * cardPercentage, totalPrice * pricePercentage);
		assertEquals(expectedCardDiscount, cardDiscount, eps);
	}
	
	@Test
	public void testCalculateTotalPriceWithDiscountsWithoutDiscountAndAccumulativeCard() {
		System.out.println("test calculateTotalPriceWithDiscounts without discount and accumulative card");
		Customer customer = new Customer();
		customer.setAccumulationCard(null);
		Discount discount = null;
		List<Pizza> pizzas = getDefaultPizzaList();
		order.setCustomer(customer);
		order.setDiscount(discount);
		order.setPizzas(pizzas);
		double totalPrice = order.calculateTotalPrice();
		double totalPriceWithDiscounts = order.calculateTotalPriceWithDiscounts();
		double eps = 1E-5;
		assertEquals(totalPrice, totalPriceWithDiscounts, eps);
	}
	
	@Test
	public void testCalculateTotalPriceWithDiscountsWithFourPizzaDiscountOnly() {
		System.out.println("test calculateTotalPriceWithDiscounts with FourPizzaDiscount only");
		Customer customer = new Customer();
		customer.setAccumulationCard(null);
		Discount discount = new FourPizzaDiscount();
		List<Pizza> pizzas = getFourPizzaDiscountablePizzaList();
		order.setCustomer(customer);
		order.setDiscount(discount);
		order.setPizzas(pizzas);
		double expectedPrice = order.calculateTotalPrice() - order.calculateDiscount();
		double totalPriceWithDiscounts = order.calculateTotalPriceWithDiscounts();
		double eps = 1E-5;
		assertEquals(expectedPrice, totalPriceWithDiscounts, eps);
	}
	
	@Test
	public void testCalculateTotalPriceWithDiscountsWithAccumulativeCardOnly() {
		System.out.println("test calculate totalPriceWithDiscounts with accumulative card only");
		AccumulationCard card = new AccumulationCard();
		double amount = 10d;
		card.setAmount(amount);
		Customer customer = new Customer();
		customer.setAccumulationCard(card);
		Discount discount = null;
		List<Pizza> pizzas = getFourPizzaDiscountablePizzaList();
		order.setCustomer(customer);
		order.setDiscount(discount);
		order.setPizzas(pizzas);
		order.calculateTotalPrice();
		double expectedPrice = order.calculateTotalPrice() - order.calculateAccumulativeCardDiscount();
		double totalPriceWithDiscounts = order.calculateTotalPriceWithDiscounts();
		double eps = 1E-5;
		assertEquals(expectedPrice, totalPriceWithDiscounts, eps);
	}
	
	@Test
	public void testCalculatedTotalPriceWithDiscountsWithFourPizzaDiscountAndAccumulativeCard() {
		System.out.println("test calculated totalPriceWithDiscounts with FourPizzaDiscount and AccumulationCard");
		AccumulationCard card = new AccumulationCard();
		double amount = 10d;
		card.setAmount(amount);
		Customer customer = new Customer();
		customer.setAccumulationCard(card);
		Discount discount = new FourPizzaDiscount();;
		List<Pizza> pizzas = getFourPizzaDiscountablePizzaList();
		order.setCustomer(customer);
		order.setDiscount(discount);
		order.setPizzas(pizzas);
		order.calculateTotalPrice();
		double expectedPrice = order.calculateTotalPrice() 
				- order.calculateAccumulativeCardDiscount()
				- order.calculateDiscount();
		double totalPriceWithDiscounts = order.calculateTotalPriceWithDiscounts();
		double eps = 1E-5;
		assertEquals(expectedPrice, totalPriceWithDiscounts, eps);
	}

	@Test
	public void testCancelFromNewStateReturnsTrue() {
		System.out.println("test cancel from new state returns true");
		OrderStatus status = OrderStatus.NEW;
		order.setStatus(status);
		boolean orderCanceled = order.cancel();
		assertTrue(orderCanceled);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.CANCELLED;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testCancelFromInProgressStateReturnsTrue() {
		System.out.println("test cancel from IN_PROGRESS state returns true");
		OrderStatus status = OrderStatus.IN_PROGRESS;
		order.setStatus(status);
		boolean orderCanceled = order.cancel();
		assertTrue(orderCanceled);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.CANCELLED;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testCancelFromDoneStateReturnsFalse() {
		System.out.println("test cancel from DONE state returns true");
		OrderStatus status = OrderStatus.DONE;
		order.setStatus(status);
		boolean orderCanceled = order.cancel();
		assertFalse(orderCanceled);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.DONE;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testCancelFromCancelledStateReturnsFalse() {
		System.out.println("test cancel from CANCELLED state returns true");
		OrderStatus status = OrderStatus.CANCELLED;
		order.setStatus(status);
		boolean orderCanceled = order.cancel();
		assertFalse(orderCanceled);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.CANCELLED;
		assertEquals(expectedOrderStatus, orderStatus);
	}

	@Test
	public void testSetInProgressFromNewStateReturnsTrue() {
		System.out.println("test setInProgress from New state returns true");
		OrderStatus status = OrderStatus.NEW;
		order.setStatus(status);
		boolean orderSetedInProgress = order.setInProgress();
		assertTrue(orderSetedInProgress);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.IN_PROGRESS;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testSetInProgressFromInProgressStateReturnsFalse() {
		System.out.println("test setInProgress from IN_PROGRESS state returns true");
		OrderStatus status = OrderStatus.IN_PROGRESS;
		order.setStatus(status);
		boolean orderSetedInProgress = order.setInProgress();
		assertFalse(orderSetedInProgress);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.IN_PROGRESS;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testSetInProgressFromCancelledStateReturnsFalse() {
		System.out.println("test setInProgress from CANCELLED state returns true");
		OrderStatus status = OrderStatus.CANCELLED;
		order.setStatus(status);
		boolean orderSetedInProgress = order.setInProgress();
		assertFalse(orderSetedInProgress);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.CANCELLED;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testSetInProgressFromDoneStateReturnsFalse() {
		System.out.println("test setInProgress from DONE state returns true");
		OrderStatus status = OrderStatus.DONE;
		order.setStatus(status);
		boolean orderSetedInProgress = order.setInProgress();
		assertFalse(orderSetedInProgress);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.DONE;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testSetDoneFromNewStateReturnsFalse() {
		System.out.println("test setDone from NEW state returns false");
		OrderStatus status = OrderStatus.NEW;
		order.setStatus(status);
		boolean orderSetedDone = order.setDone();
		assertFalse(orderSetedDone);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.NEW;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testSetDoneFromInProgressStateReturnsTrue() {
		System.out.println("test setDone from IN_PROGRESS state returns true");
		OrderStatus status = OrderStatus.IN_PROGRESS;
		Customer customer = new Customer();
		order.setCustomer(customer);
		order.setStatus(status);
		boolean orderSetedDone = order.setDone();
		assertTrue(orderSetedDone);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.DONE;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testSetDoneFromCancelledStateReturnsFalse() {
		System.out.println("test setDone from CANCELLED state returns false");
		OrderStatus status = OrderStatus.CANCELLED;
		order.setStatus(status);
		boolean orderSetedDone = order.setDone();
		assertFalse(orderSetedDone);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.CANCELLED;
		assertEquals(expectedOrderStatus, orderStatus);
	}
	
	@Test
	public void testSetDoneFromDoneStateReturnsFalse() {
		System.out.println("test setDone from DONE state returns false");
		OrderStatus status = OrderStatus.DONE;
		order.setStatus(status);
		boolean orderSetedDone = order.setDone();
		assertFalse(orderSetedDone);
		OrderStatus orderStatus = order.getStatus();
		OrderStatus expectedOrderStatus = OrderStatus.DONE;
		assertEquals(expectedOrderStatus, orderStatus);
	}

	private List<Pizza> getDefaultPizzaList() {
		List<Pizza> pizzaList = new ArrayList<>();
		Pizza pizzaOne = new Pizza(1, "Margarita", 60d, PizzaType.MEAT);
		Pizza pizzaTwo = new Pizza(2, "SeaPizza", 90d, PizzaType.SEA);
		Pizza pizzaThree = new Pizza(3, "Ayurveda", 80d, PizzaType.VEGETERIAN);
		
		
		pizzaList.add(pizzaOne);
		pizzaList.add(pizzaTwo);
		pizzaList.add(pizzaThree);
		
		return pizzaList;
	}
	
	private List<Pizza> getFourPizzaDiscountablePizzaList() {
		List<Pizza> pizzaList = new ArrayList<>();
		Pizza pizzaOne = new Pizza(1, "Margarita", 60d, PizzaType.MEAT);
		Pizza pizzaTwo = new Pizza(2, "SeaPizza", 90d, PizzaType.SEA);
		Pizza pizzaThree = new Pizza(3, "Ayurveda", 80d, PizzaType.VEGETERIAN);
		Pizza pizzaFour = new Pizza(4, "Americana", 75d, PizzaType.MEAT);
		
		pizzaList.add(pizzaOne);
		pizzaList.add(pizzaTwo);
		pizzaList.add(pizzaThree);
		pizzaList.add(pizzaFour);
		
		return pizzaList;
	}
	
	private Double calculateTotalPrice(List<Pizza> pizzas) {
		double result = 0d;
		for (Pizza pizza : pizzas) {
			result += pizza.getPrice();
		}
		return result;
	}
	
	private Double getMaxPrice(List<Pizza> pizzas) {
		double maxPrice = 0d;
		for (Pizza pizza : pizzas) {
			if(pizza.getPrice() > maxPrice) {
				maxPrice = pizza.getPrice();
			}
		}
		return maxPrice;
	}
	
}
