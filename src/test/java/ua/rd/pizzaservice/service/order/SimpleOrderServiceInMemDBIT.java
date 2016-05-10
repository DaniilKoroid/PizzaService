package ua.rd.pizzaservice.service.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.order.OrderState;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/repositoryH2TestContext.xml", "classpath:/appTestContext.xml"})
public class SimpleOrderServiceInMemDBIT {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private OrderService service;
	
	private Customer customer;
	
	@Before
	public void setCustomer() {
		customer = new Customer("Ivan");
	}
	
	@Autowired
	public void setDataSource(DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	@Test
	public void testPlaceNewOrderReturnsNotNullOrder() {
		Integer[] pizzasID = getInsertedPizzasToOrderIds();
		Order order = service.placeNewOrder(customer, pizzasID);
		assertNotNull(order);
	}
	
	@Test
	public void testPlaceNewOrderReturnsOrderWithAppropriatePizzaCount() {
		Integer[] pizzasID = getInsertedPizzasToOrderIds();
		Order order = service.placeNewOrder(customer, pizzasID);
		Map<Pizza, Integer> pizzas = order.getPizzas();
		for (Entry<Pizza, Integer> entry : pizzas.entrySet()) {
			Integer pizzaId = entry.getKey().getId();
			Integer numberOfOccurancesInArray = numberOfOccurancesInArray(pizzaId, pizzasID);
			Integer pizzaCount = entry.getValue();
			assertEquals(numberOfOccurancesInArray, pizzaCount);
		}
	}
	
	@Test
	public void testPlaceNewOrderReturnsOrderWithNewState() {
		Integer[] pizzasID = getInsertedPizzasToOrderIds();
		Order order = service.placeNewOrder(customer, pizzasID);
		assertOrderStateEqualsToGivenState(order, OrderState.NEW);
	}
	
	@Test(expected = Exception.class)
	public void testPlaceNewOrderThrowsExceptionWhenPizzasWithGivenIdsDontExist() {
		Integer[] pizzasID = getDefectedInsertedPizzasToOrderIds();
		service.placeNewOrder(customer, pizzasID);
	}
	
	@Test
	public void testCancelOrderReturnsTrueOnOrderWithNewState() {
		Order order = getOrderFromServiceWithNewState();
		Boolean isCancelled = service.cancelOrder(order);
		assertTrue(isCancelled);
	}

	@Test
	public void testCancelOrderChangedOrderStateToCancelledFromNewState() {
		Order order = getOrderFromServiceWithNewState();
		service.cancelOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.CANCELLED);
	}
	
	@Test
	public void testCancelOrderReturnsTrueOnOrderWithInProgressState() {
		Order order = getOrderFromServiceWithInProgressState();
		Boolean isCancelled = service.cancelOrder(order);
		assertTrue(isCancelled);
	}

	@Test
	public void testCancelOrderChangedOrderStateToCancelledFromInProgressState() {
		Order order = getOrderFromServiceWithInProgressState();
		service.cancelOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.CANCELLED);
	}
	
	@Test
	public void testCancelOrderReturnsFalseOnOrderWithCancelledState() {
		Order order = getOrderFromServiceWithCancelledState();
		Boolean isCancelled = service.cancelOrder(order);
		assertFalse(isCancelled);
	}

	@Test
	public void testCancelOrderChangedOrderStateToCancelledFromCancelledState() {
		Order order = getOrderFromServiceWithCancelledState();
		service.cancelOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.CANCELLED);
	}
	
	@Test
	public void testCancelOrderReturnsFalseOnOrderWithDoneState() {
		Order order = getOrderFromServiceWithDoneState();
		Boolean isCancelled = service.cancelOrder(order);
		assertFalse(isCancelled);
	}

	@Test
	public void testCancelOrderChangedOrderStateToCancelledFromDoneState() {
		Order order = getOrderFromServiceWithDoneState();
		service.cancelOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.DONE);
	}

	@Test
	public void testProcessOrderReturnsTrueWithOrderNewState() {
		Order order = getOrderFromServiceWithNewState();
		Boolean orderProcessed = service.processOrder(order);
		assertTrue(orderProcessed);
	}
	
	@Test
	public void testProcessOrderChangedStateToProcessedFromNewState() {
		Order order = getOrderFromServiceWithNewState();
		service.processOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.IN_PROGRESS);
	}
	
	@Test
	public void testProcessOrderReturnsFalseWithOrderInProgressState() {
		Order order = getOrderFromServiceWithInProgressState();
		Boolean orderProcessed = service.processOrder(order);
		assertFalse(orderProcessed);
	}
	
	@Test
	public void testProcessOrderDontChangedStateFromInProgressState() {
		Order order = getOrderFromServiceWithInProgressState();
		service.processOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.IN_PROGRESS);
	}
	
	@Test
	public void testProcessOrderReturnsFalseWithOrderInDoneState() {
		Order order = getOrderFromServiceWithDoneState();
		Boolean orderProcessed = service.processOrder(order);
		assertFalse(orderProcessed);
	}
	
	@Test
	public void testProcessOrderDontChangedStateFromDoneState() {
		Order order = getOrderFromServiceWithDoneState();
		service.processOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.DONE);
	}
	
	@Test
	public void testProcessOrderReturnsFalseWithOrderInCancelledState() {
		Order order = getOrderFromServiceWithCancelledState();
		Boolean orderProcessed = service.processOrder(order);
		assertFalse(orderProcessed);
	}
	
	@Test
	public void testProcessOrderDontChangedStateFromCancelledState() {
		Order order = getOrderFromServiceWithCancelledState();
		service.processOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.CANCELLED);
	}
	
	@Test
	public void testDoneOrderReturnsFalseWithOrderInNewState() {
		Order order = getOrderFromServiceWithNewState();
		Boolean orderDone = service.doneOrder(order);
		assertFalse(orderDone);
	}
	
	@Test
	public void testDoneOrderDontChangedStateFromNewState() {
		Order order = getOrderFromServiceWithNewState();
		service.doneOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.NEW);
	}
	
	@Test
	public void testDoneOrderReturnsTrueWithOrderInProgressState() {
		Order order = getOrderFromServiceWithInProgressState();
		Boolean orderDone = service.doneOrder(order);
		assertTrue(orderDone);
	}
	
	@Test
	public void testDoneOrderChangedStateFromInProgressStateToDone() {
		Order order = getOrderFromServiceWithInProgressState();
		service.doneOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.DONE);
	}
	
	@Test
	public void testDoneOrderReturnsFalseWithOrderDoneState() {
		Order order = getOrderFromServiceWithDoneState();
		Boolean orderDone = service.doneOrder(order);
		assertFalse(orderDone);
	}
	
	@Test
	public void testDoneOrderDontChangedStateFromDoneState() {
		Order order = getOrderFromServiceWithDoneState();
		service.doneOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.DONE);
	}
	
	@Test
	public void testDoneOrderReturnsFalseWithOrderCancelledState() {
		Order order = getOrderFromServiceWithCancelledState();
		Boolean orderDone = service.doneOrder(order);
		assertFalse(orderDone);
	}
	
	@Test
	public void testDoneOrderDontChangedStateFromCancelledState() {
		Order order = getOrderFromServiceWithCancelledState();
		service.doneOrder(order);
		assertOrderStateEqualsToGivenState(order, OrderState.CANCELLED);
	}
	
	@Test
	public void testCanChangeReturnsTrueWithOrderInNewState() {
		Order order = getOrderFromServiceWithNewState();
		Boolean canChange = service.canChange(order);
		assertTrue(canChange);
	}
	
	@Test
	public void testCanChangeReturnsFalseWithOrderInProgressState() {
		Order order = getOrderFromServiceWithInProgressState();
		Boolean canChange = service.canChange(order);
		assertFalse(canChange);
	}
	
	@Test
	public void testCanChangeReturnsFalseWithOrderInDoneState() {
		Order order = getOrderFromServiceWithDoneState();
		Boolean canChange = service.canChange(order);
		assertFalse(canChange);
	}
	
	@Test
	public void testCanChangeReturnsFalseWithOrderInCancelledState() {
		Order order = getOrderFromServiceWithCancelledState();
		Boolean canChange = service.canChange(order);
		assertFalse(canChange);
	}
	
	@Test
	public void testChangeOrderReturnsTrueWithOrderInNewState() {
		Order order = getOrderFromServiceWithNewState();
		Integer[] newPizzasId = getNewInsertedPizzasToOrderIds();
		Boolean orderChanged = service.changeOrder(order, newPizzasId);
		assertTrue(orderChanged);
	}
	
	public Integer[] insertThreePizzas() {
		Integer id1 = insertPizza("N1", 111.1d, PizzaType.MEAT);
		Integer id2 = insertPizza("N2", 222.2d, PizzaType.SEA);
		Integer id3 = insertPizza("N3", 333.3d, PizzaType.VEGETERIAN);
		Integer[] idArr = new Integer[3];
		idArr[0] = id1;
		idArr[1] = id2;
		idArr[2] = id3;
		return idArr;
	}

	private Integer[] getInsertedPizzasToOrderIds() {
		Integer[] threePizzas = insertThreePizzas();
		Integer pizza1Id = threePizzas[0];
		Integer pizza2Id = threePizzas[1];
		Integer pizza3Id = threePizzas[2];
		Integer[] pizzasID = new Integer[] {pizza1Id, pizza1Id, pizza2Id, pizza3Id, pizza3Id};
		return pizzasID;
	}
	
	private Integer[] getDefectedInsertedPizzasToOrderIds() {
		Integer[] threePizzas = insertThreePizzas();
		Integer pizza1Id = threePizzas[0] + 1;
		Integer pizza2Id = threePizzas[1] + 1;
		Integer pizza3Id = threePizzas[2] + 1;
		Integer[] pizzasID = new Integer[] {pizza1Id, pizza1Id, pizza2Id, pizza3Id, pizza3Id};
		return pizzasID;
	}
	
	private Integer[] getNewInsertedPizzasToOrderIds() {
		Integer[] threePizzas = insertThreePizzas();
		Integer pizza1Id = threePizzas[0];
		Integer pizza2Id = threePizzas[1];
		Integer pizza3Id = threePizzas[2];
		Integer[] pizzasID = new Integer[] {pizza1Id, pizza1Id, pizza1Id, pizza2Id, pizza3Id};
		return pizzasID;
	}
	
	private int numberOfOccurancesInArray(int value, Integer[] arr) {
		int total = 0;
		for (int arrValue : arr) {
			if (arrValue == value) {
				total++;
			}
		}
		return total;
	}

	private Order getOrderFromServiceWithNewState() {
		Integer[] pizzasID = getInsertedPizzasToOrderIds();
		Order order = service.placeNewOrder(customer, pizzasID);
		return order;
	}
	
	private Order getOrderFromServiceWithInProgressState() {
		Order order = getOrderFromServiceWithNewState();
		Boolean isProcessed = service.processOrder(order);
		assertTrue(isProcessed);
		assertOrderStateEqualsToGivenState(order, OrderState.IN_PROGRESS);
		return order;
	}
	
	private Order getOrderFromServiceWithDoneState() {
		Order order = getOrderFromServiceWithNewState();
		Boolean isProcessed = service.processOrder(order);
		assertTrue(isProcessed);
		OrderState state = order.getState();
		OrderState expectedState = OrderState.IN_PROGRESS;
		assertEquals(expectedState, state);
		Boolean doneOrder = service.doneOrder(order);
		assertTrue(doneOrder);
		assertOrderStateEqualsToGivenState(order, OrderState.DONE);
		return order;
	}
	
	private Order getOrderFromServiceWithCancelledState() {
		Order order = getOrderFromServiceWithNewState();
		Boolean isCancelled = service.cancelOrder(order);
		assertTrue(isCancelled);
		assertOrderStateEqualsToGivenState(order, OrderState.CANCELLED);
		return order;
	}
	
	private void assertOrderStateEqualsToGivenState(Order order, OrderState state) {
		OrderState orderState = order.getState();
		assertEquals(state, orderState);
	}
	
	private Integer insertPizza(String name, Double price, PizzaType type) {
		String sqlInsert = "INSERT INTO pizza (name, price, type, id) VALUES (?, ?, ?, pizza_sequence.NEXTVAL)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				(Connection con) -> {
			PreparedStatement ps = con.prepareStatement(sqlInsert, new String[]{"id"});
			ps.setString(1, name);
			ps.setDouble(2, price);
			ps.setInt(3, type.ordinal());
			return ps;
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

}
