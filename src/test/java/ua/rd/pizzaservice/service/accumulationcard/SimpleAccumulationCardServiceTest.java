package ua.rd.pizzaservice.service.accumulationcard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;

public class SimpleAccumulationCardServiceTest {

	private AccumulationCardService accCardService;

	private Customer customerWithActivatedCard;
	private Customer customerWithNotActivatedCard;
	private Customer customerWithoutCard;

	@Before
	public void setUp() throws Exception {
		accCardService = new SimpleAccumulationCardService();
		customerWithActivatedCard = new Customer("name1");
		customerWithNotActivatedCard = new Customer("name2");
		customerWithoutCard = new Customer("name3");
		accCardService.assignNewAccumulationCardToCustomer(customerWithActivatedCard);
		accCardService.assignNewAccumulationCardToCustomer(customerWithNotActivatedCard);
		accCardService.activateAccumulationCardForCustomer(customerWithActivatedCard);
	}

	@After
	public void tearDown() throws Exception {
		accCardService = null;
		customerWithActivatedCard = null;
		customerWithNotActivatedCard = null;
		customerWithoutCard = null;
	}

	@Test
	public void testGetAccumulationCardByCustomerWithActivatedCardReturnsCard() {
		System.out.println("test getAccumulationCardByCustomer with activated card returns card");
		AccumulationCard card = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		assertNotNull(card);
	}

	@Test
	public void testGetAccumulationCardByCustomerWithActivatedCardReturnsActivatedCard() {
		System.out.println("test getAccumulationCardByCustomer with activated card returns activated card");
		AccumulationCard card = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		assertTrue(card.getIsActivated());
	}

	@Test
	public void testGetAccumulationCardByCustomerWithNotActivatedCardReturnsCard() {
		System.out.println("test getAccumulationCardByCustomer with not activated card returns card");
		AccumulationCard card = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		assertNotNull(card);
	}

	@Test
	public void testGetAccumulationCardByCustomerWithNotActivatedCardReturnsNotActivatedCard() {
		System.out.println("test getAccumulationCardByCustomer with activated card returns card");
		AccumulationCard card = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		assertFalse(card.getIsActivated());
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetAccumulationCardByCustomerWithoutCardThrowsNoSuchElementException() {
		System.out.println("test getAccumulationCardByCustomer with activated card returns card");
		accCardService.getAccumulationCardByCustomer(customerWithoutCard);
	}

	@Test
	public void testHasAccumulationCardWithCustomerWithActivatedCardReturnsTrue() {
		System.out.println("test hasAccumulationCard with customer with activated card returns true");
		assertTrue(accCardService.hasAccumulationCard(customerWithActivatedCard));
	}

	@Test
	public void testHasAccumulationCardWithCustomerWithNotActivatedCardReturnsTrue() {
		System.out.println("test hasAccumulationCard with customer with not activated card returns true");
		assertTrue(accCardService.hasAccumulationCard(customerWithNotActivatedCard));
	}

	@Test
	public void testHasAccumulationCardWithCustomerWithoutCardReturnsFalse() {
		System.out.println("test hasAccumulationCard with customer without card returns false");
		assertFalse(accCardService.hasAccumulationCard(customerWithoutCard));
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerWithActivatedCardReturnsFalse() {
		System.out.println("test assignNewAccumulationCardToCustomer with activated card returns false");
		assertFalse(accCardService.assignNewAccumulationCardToCustomer(customerWithActivatedCard));
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerWithActivatedCardDontCreatesNewCard() {
		System.out.println("test assignNewAccumulationCardToCustomer with activated card dont creates new card");
		AccumulationCard cardBefore = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		accCardService.assignNewAccumulationCardToCustomer(customerWithActivatedCard);
		AccumulationCard cardAfter = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		assertTrue(cardBefore == cardAfter);
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerWithNotActivatedCardReturnsFalse() {
		System.out.println("test assignNewAccumulationCardToCustomer with not activated card returns false");
		assertFalse(accCardService.assignNewAccumulationCardToCustomer(customerWithNotActivatedCard));
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerWithNotActivatedCardDontCreatesNewCard() {
		System.out.println("test assignNewAccumulationCardToCustomer with not activated card dont creates new card");
		AccumulationCard cardBefore = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		accCardService.assignNewAccumulationCardToCustomer(customerWithNotActivatedCard);
		AccumulationCard cardAfter = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		assertTrue(cardBefore == cardAfter);
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerWithoutCardReturnsTrue() {
		System.out.println("test assignNewAccumulationCardToCustomer without card returns true");
		assertTrue(accCardService.assignNewAccumulationCardToCustomer(customerWithoutCard));
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerWithoutCardCreatesCard() {
		System.out.println("test assignNewAccumulationCardToCustomer without card created card");
		assertFalse(accCardService.hasAccumulationCard(customerWithoutCard));
		accCardService.assignNewAccumulationCardToCustomer(customerWithoutCard);
		assertTrue(accCardService.hasAccumulationCard(customerWithoutCard));
		assertNotNull(accCardService.getAccumulationCardByCustomer(customerWithoutCard));
	}

	@Test
	public void testActivateAccumulationCardForCustomerWithActivatedCardReturnsFalse() {
		System.out.println("test activateAccumulationCardForCustomer with activated card returns false");
		assertFalse(accCardService.activateAccumulationCardForCustomer(customerWithActivatedCard));
	}

	@Test
	public void testActivateAccumulationCardForCustomerWithActivatedCardDontChangesCardState() {
		System.out.println("test activateAccumulationCardForCustomer with activated card dont " 
				+ "changes card state");
		AccumulationCard cardBefore = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		boolean before = cardBefore.getIsActivated();
		accCardService.activateAccumulationCardForCustomer(customerWithActivatedCard);
		AccumulationCard cardAfter = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		boolean after = cardAfter.getIsActivated();
		assertEquals(before, after);
	}

	@Test
	public void testActivateAccumulationCardForCustomerWithNotActivatedCardReturnsTrue() {
		System.out.println("test activateAccumulationCardForCustomer with not activated card returns true");
		assertTrue(accCardService.activateAccumulationCardForCustomer(customerWithNotActivatedCard));
	}
	
	@Test
	public void testActivateAccumulationCardForCustomerWithNotActivatedCardChangesCardState() {
		System.out.println("test activateAccumulationCardForCustomer with not activated card "
				+ "changes card state");
		AccumulationCard cardBefore = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		boolean before = cardBefore.getIsActivated(); 
		accCardService.activateAccumulationCardForCustomer(customerWithNotActivatedCard);
		AccumulationCard cardAfter = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		boolean after = cardAfter.getIsActivated();
		assertNotEquals(before, after);
	}

	@Test
	public void testActivateAccumulationCardForCustomerWithoutCardReturnsFalse() {
		System.out.println("test activateAccumulationCardForCustomer without card returns false");
		assertFalse(accCardService.activateAccumulationCardForCustomer(customerWithoutCard));
	}
	
	@Test
	public void testDeactivateAccumulationCardForCustomerWithActivatedCardReturnsTrue() {
		System.out.println("test deactivateAccumulationCardForCustomer with activated card returns true");
		assertTrue(accCardService.deactivateAccumulationCardForCustomer(customerWithActivatedCard));
	}
	
	@Test
	public void testDeactivateAccumulationCardForCustomerWithActivatedCardChangesCardState() {
		System.out.println("test deactivateAccumulationCardForCustomer with activated card " 
				+ "changes card state");
		AccumulationCard cardBefore = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		boolean before = cardBefore.getIsActivated();
		accCardService.deactivateAccumulationCardForCustomer(customerWithActivatedCard);
		AccumulationCard cardAfter = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		boolean after = cardAfter.getIsActivated();
		assertNotEquals(before, after);
	}

	@Test
	public void testDeactivateAccumulationCardForCustomerWithNotActivatedCardReturnsFalse() {
		System.out.println("test deactivateAccumulationCardForCustomer with not activated card returns false");
		assertFalse(accCardService.deactivateAccumulationCardForCustomer(customerWithNotActivatedCard));
	}

	@Test
	public void testDeactivateAccumulationCardForCustomerWithNotActivatedCardDontChangesCardState() {
		System.out.println("test deactivateAccumulationCardForCustomer with not activated card " 
				+ "dont changes card state");
		AccumulationCard cardBefore = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		boolean before = cardBefore.getIsActivated();
		accCardService.activateAccumulationCardForCustomer(customerWithNotActivatedCard);
		AccumulationCard cardAfter = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		boolean after = cardAfter.getIsActivated();
		assertNotEquals(before, after);
	}
	
	@Test
	public void testDeactivateAccumulationCardForCustomerWithoutCardReturnsFalse() {
		System.out.println("test deactivateAccumulationCardForCustomer without card returns false");
		assertFalse(accCardService.deactivateAccumulationCardForCustomer(customerWithoutCard));
	}
}
