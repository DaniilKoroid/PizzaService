package ua.rd.pizzaservice.domain.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.OrderState;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;

public class OrderTest {

	Order order;

	@Before
	public void setUpOrder() {
		order = new Order();
	}

	@Test
	public void testOrderCanChangeReturnsTrueFromNewState() {
		System.out.println("test order canChange returns true from NEW state");
		order.setState(OrderState.NEW);
		assertTrue(order.canChange());
	}

	@Test
	public void testOrderCanChangeReturnsFalseFromInProgressState() {
		System.out.println("test order canChange returns false from IN_PROGRESS state");
		order.setState(OrderState.IN_PROGRESS);
		assertFalse(order.canChange());
	}

	@Test
	public void testOrderCanChangeReturnsFalseFromDoneState() {
		System.out.println("test order canChange returns false from DONE state");
		order.setState(OrderState.DONE);
		assertFalse(order.canChange());
	}

	@Test
	public void testOrderCanChangeReturnsFalseFromCancelledState() {
		System.out.println("test order canChange returns false from CANCELLED state");
		order.setState(OrderState.CANCELLED);
		assertFalse(order.canChange());
	}

	@Test
	public void testChangeOrderFromNewStateReturnsTrue() {
		System.out.println("test change order from NEW state returns true");
		order.setState(OrderState.NEW);
		Map<Pizza, Integer> atStartPizzas = order.getPizzas();
		@SuppressWarnings("serial")
		Map<Pizza, Integer> newPizzas = new HashMap<Pizza, Integer>() {
			{
				put(new Pizza(), 1);
			}
		};
		assertNotEquals(atStartPizzas, newPizzas);
		boolean orderChanged = order.changeOrder(newPizzas);
		Map<Pizza, Integer> afterChangePizzas = order.getPizzas();
		assertTrue(orderChanged);
		assertEquals(newPizzas, afterChangePizzas);
	}

	@Test
	public void testChangeOrderFromInProgressStateReturnsFalse() {
		System.out.println("test change order from IN_PROGRESS state returns false");
		order.setState(OrderState.IN_PROGRESS);
		Map<Pizza, Integer> atStartPizzas = order.getPizzas();
		@SuppressWarnings("serial")
		Map<Pizza, Integer> newPizzas = new HashMap<Pizza, Integer>() {
			{
				put(new Pizza(), 1);
			}
		};
		assertNotEquals(atStartPizzas, newPizzas);
		boolean orderChanged = order.changeOrder(newPizzas);
		Map<Pizza, Integer> afterChangePizzas = order.getPizzas();
		assertFalse(orderChanged);
		assertNotEquals(newPizzas, afterChangePizzas);
	}

	@Test
	public void testChangeOrderFromCancelledStateReturnsFalse() {
		System.out.println("test change order from CANCELLED state returns false");
		order.setState(OrderState.CANCELLED);
		Map<Pizza, Integer> atStartPizzas = order.getPizzas();
		@SuppressWarnings("serial")
		Map<Pizza, Integer> newPizzas = new HashMap<Pizza, Integer>() {
			{
				put(new Pizza(), 1);
			}
		};
		assertNotEquals(atStartPizzas, newPizzas);
		boolean orderChanged = order.changeOrder(newPizzas);
		Map<Pizza, Integer> afterChangePizzas = order.getPizzas();
		assertFalse(orderChanged);
		assertNotEquals(newPizzas, afterChangePizzas);
	}

	@Test
	public void testChangeOrderFromDoneStateReturnsFalse() {
		System.out.println("test change order from DONE state returns false");
		order.setState(OrderState.DONE);
		Map<Pizza, Integer> atStartPizzas = order.getPizzas();
		@SuppressWarnings("serial")
		Map<Pizza, Integer> newPizzas = new HashMap<Pizza, Integer>() {
			{
				put(new Pizza(), 1);
			}
		};
		assertNotEquals(atStartPizzas, newPizzas);
		boolean orderChanged = order.changeOrder(newPizzas);
		Map<Pizza, Integer> afterChangePizzas = order.getPizzas();
		assertFalse(orderChanged);
		assertNotEquals(newPizzas, afterChangePizzas);
	}

	@Test
	public void testCalculateFullPriceDontDependsOnOrderStatus() {
		System.out.println("test calculate full price dont depends on order status");
		Map<Pizza, Integer> pizzaMap = getDefaultPizzaMap();
		order.setPizzas(pizzaMap);
		double totalPrice = calculateTotalPrice(pizzaMap);
		double eps = 1E-5;
		OrderState[] orderStateValues = OrderState.values();
		for (OrderState orderState : orderStateValues) {
			order.setState(orderState);
			assertEquals(totalPrice, order.calculateFullPrice(), eps);
		}
	}

	@Test
	public void testCalculateFullPrice() {
		System.out.println("test calculate full price");
		Map<Pizza, Integer> pizzaMap = getDefaultPizzaMap();
		order.setPizzas(pizzaMap);
		double expectedTotalPrice = calculateTotalPrice(pizzaMap);
		double actualTotalPrice = order.calculateFullPrice();
		double eps = 1E-5;
		assertEquals(expectedTotalPrice, actualTotalPrice, eps);
	}

	@Test
	public void testCancelFromNewStateReturnsTrue() {
		System.out.println("test cancel from new state returns true");
		OrderState state = OrderState.NEW;
		order.setState(state);
		boolean orderCanceled = order.cancel();
		assertTrue(orderCanceled);
		OrderState orderState = order.getState();
		OrderState expectedOrderState = OrderState.CANCELLED;
		assertEquals(expectedOrderState, orderState);
	}

	@Test
	public void testCancelFromInProgressStateReturnsTrue() {
		System.out.println("test cancel from IN_PROGRESS state returns true");
		OrderState state = OrderState.IN_PROGRESS;
		order.setState(state);
		boolean orderCanceled = order.cancel();
		assertTrue(orderCanceled);
		OrderState orderState = order.getState();
		OrderState expectedOrderState = OrderState.CANCELLED;
		assertEquals(expectedOrderState, orderState);
	}

	@Test
	public void testCancelFromDoneStateReturnsFalse() {
		System.out.println("test cancel from DONE state returns true");
		OrderState state = OrderState.DONE;
		order.setState(state);
		boolean orderCanceled = order.cancel();
		assertFalse(orderCanceled);
		OrderState orderState = order.getState();
		OrderState expectedOrderState = OrderState.DONE;
		assertEquals(expectedOrderState, orderState);
	}

	@Test
	public void testCancelFromCancelledStateReturnsFalse() {
		System.out.println("test cancel from CANCELLED state returns true");
		OrderState state = OrderState.CANCELLED;
		order.setState(state);
		boolean orderCanceled = order.cancel();
		assertFalse(orderCanceled);
		OrderState orderState = order.getState();
		OrderState expectedOrderState = OrderState.CANCELLED;
		assertEquals(expectedOrderState, orderState);
	}

	@Test
	public void testCanProceedToStateNewFromNewReturnsFalse() {
		System.out.println("test canProceedToState NEW from NEW returns false");
		OrderState stateFrom = OrderState.NEW;
		OrderState stateTo = OrderState.NEW;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateInProgressFromNewReturnsTrue() {
		System.out.println("test canProceedToState IN_PROGRESS from NEW returns true");
		OrderState stateFrom = OrderState.NEW;
		OrderState stateTo = OrderState.IN_PROGRESS;
		order.setState(stateFrom);
		assertTrue(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateDoneFromNewReturnsFalse() {
		System.out.println("test canProceedToState DONE from NEW returns false");
		OrderState stateFrom = OrderState.NEW;
		OrderState stateTo = OrderState.DONE;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateCancelledFromNewReturnsTrue() {
		System.out.println("test canProceedToState CANCELLED from NEW returns true");
		OrderState stateFrom = OrderState.NEW;
		OrderState stateTo = OrderState.CANCELLED;
		order.setState(stateFrom);
		assertTrue(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateNewFromInProgressReturnsFalse() {
		System.out.println("test canProceedToState NEW from IN_PROGRESS returns false");
		OrderState stateFrom = OrderState.IN_PROGRESS;
		OrderState stateTo = OrderState.NEW;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateInProgressFromInProgressReturnsFalse() {
		System.out.println("test canProceedToState IN_PROGRESS from IN_PROGRESS returns false");
		OrderState stateFrom = OrderState.IN_PROGRESS;
		OrderState stateTo = OrderState.IN_PROGRESS;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateDoneFromInProgressReturnsTrue() {
		System.out.println("test canProceedToState DONE from IN_PROGRESS returns true");
		OrderState stateFrom = OrderState.IN_PROGRESS;
		OrderState stateTo = OrderState.DONE;
		order.setState(stateFrom);
		assertTrue(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateCancelledFromInProgressReturnsTrue() {
		System.out.println("test canProceedToState CANCELLED from IN_PROGRESS returns true");
		OrderState stateFrom = OrderState.IN_PROGRESS;
		OrderState stateTo = OrderState.CANCELLED;
		order.setState(stateFrom);
		assertTrue(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateNewFromCancelledReturnsFalse() {
		System.out.println("test canProceedToState NEW from CANCELLED returns false");
		OrderState stateFrom = OrderState.CANCELLED;
		OrderState stateTo = OrderState.NEW;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateInProgressFromCancelledReturnsFalse() {
		System.out.println("test canProceedToState IN_PROGRESS from CANCELLED returns false");
		OrderState stateFrom = OrderState.CANCELLED;
		OrderState stateTo = OrderState.IN_PROGRESS;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateDoneFromCancelledReturnsFalse() {
		System.out.println("test canProceedToState DONE from CANCELLED returns true");
		OrderState stateFrom = OrderState.CANCELLED;
		OrderState stateTo = OrderState.DONE;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateCancelledFromCancelledReturnsFalse() {
		System.out.println("test canProceedToState CANCELLED from CANCELLED returns true");
		OrderState stateFrom = OrderState.CANCELLED;
		OrderState stateTo = OrderState.CANCELLED;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateNewFromDoneReturnsFalse() {
		System.out.println("test canProceedToState NEW from DONE returns false");
		OrderState stateFrom = OrderState.DONE;
		OrderState stateTo = OrderState.NEW;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateInProgressFromDoneReturnsFalse() {
		System.out.println("test canProceedToState IN_PROGRESS from DONE returns false");
		OrderState stateFrom = OrderState.DONE;
		OrderState stateTo = OrderState.IN_PROGRESS;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateDoneFromDoneReturnsFalse() {
		System.out.println("test canProceedToState DONE from DONE returns true");
		OrderState stateFrom = OrderState.DONE;
		OrderState stateTo = OrderState.DONE;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testCanProceedToStateCancelledFromDoneReturnsFalse() {
		System.out.println("test canProceedToState CANCELLED from DONE returns true");
		OrderState stateFrom = OrderState.DONE;
		OrderState stateTo = OrderState.CANCELLED;
		order.setState(stateFrom);
		assertFalse(order.canProceedToState(stateTo));
	}

	@Test
	public void testNextStateFromNewStateReturnsTrue() {
		System.out.println("test nextState from NEW state returns true");
		OrderState state = OrderState.NEW;
		order.setState(state);
		assertTrue(order.nextState());
	}

	@Test
	public void testNextStateFromNewStateChangesStateToInProgress() {
		System.out.println("test nextState from NEW state changes state to IN_PROGRESS");
		OrderState startState = OrderState.NEW;
		OrderState toState = OrderState.IN_PROGRESS;
		order.setState(startState);
		order.nextState();
		OrderState actualState = order.getState();
		assertEquals(toState, actualState);
	}

	@Test
	public void testNextStateFromInProgressStateReturnsTrue() {
		System.out.println("test nextState from IN_PROGRESS state returns true");
		OrderState state = OrderState.IN_PROGRESS;
		order.setState(state);
		assertTrue(order.nextState());
	}

	@Test
	public void testNextStateFromInProgressStateChangesStateToDone() {
		System.out.println("test nextState from IN_PROGRESS state changes state to DONE");
		OrderState startState = OrderState.IN_PROGRESS;
		OrderState toState = OrderState.DONE;
		order.setState(startState);
		order.nextState();
		OrderState actualState = order.getState();
		assertEquals(toState, actualState);
	}

	@Test
	public void testNextStateFromDoneStateReturnsFalse() {
		System.out.println("test nextState from DONE state returns false");
		OrderState state = OrderState.DONE;
		order.setState(state);
		assertFalse(order.nextState());
	}

	@Test
	public void testNextStateFromDoneStateDontChangesState() {
		System.out.println("test nextState from DONE state dont changes state");
		OrderState startState = OrderState.DONE;
		OrderState toState = OrderState.DONE;
		order.setState(startState);
		order.nextState();
		OrderState actualState = order.getState();
		assertEquals(toState, actualState);
	}

	@Test
	public void testNextStateFromCancelledStateReturnsFalse() {
		System.out.println("test nextState from CANCELLED state returns false");
		OrderState state = OrderState.CANCELLED;
		order.setState(state);
		assertFalse(order.nextState());
	}

	@Test
	public void testNextStateFromCancelledStateDontChangesState() {
		System.out.println("test nextState from CANCELLED state dont changes state");
		OrderState startState = OrderState.CANCELLED;
		OrderState toState = OrderState.CANCELLED;
		order.setState(startState);
		order.nextState();
		OrderState actualState = order.getState();
		assertEquals(toState, actualState);
	}

	private Map<Pizza, Integer> getDefaultPizzaMap() {
		Map<Pizza, Integer> pizzaMap = new HashMap<Pizza, Integer>() {
			static final long serialVersionUID = 920564268758649188L;

			{
				put(new Pizza(1, "Margarita", 60d, PizzaType.MEAT), 1);
				put(new Pizza(2, "SeaPizza", 90d, PizzaType.SEA), 1);
				put(new Pizza(3, "Ayurveda", 80d, PizzaType.VEGETERIAN), 1);
			}
		};
		return pizzaMap;
	}

	private Double calculateTotalPrice(Map<Pizza, Integer> pizzas) {
		double result = 0d;
		for (Entry<Pizza, Integer> entry : pizzas.entrySet()) {
			double price = entry.getKey().getPrice();
			Integer count = entry.getValue();
			result += price * count;
		}
		return result;
	}
}
