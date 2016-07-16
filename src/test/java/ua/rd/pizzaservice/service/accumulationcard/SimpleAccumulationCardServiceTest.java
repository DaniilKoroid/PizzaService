package ua.rd.pizzaservice.service.accumulationcard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ua.rd.pizzaservice.domain.AccumulationCard;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.repository.AccumulationCardRepository;
import ua.rd.pizzaservice.service.AccumulationCardService;
import ua.rd.pizzaservice.service.DiscountService;
import ua.rd.pizzaservice.service.impl.AccumulationCardServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SimpleAccumulationCardServiceTest {

	private AccumulationCardService accCardService;

	@Mock
	private AccumulationCardRepository cardRep;
	
	@Mock
	private DiscountService discountService;
	
	@Mock
	private Order orderForDiscounts;
	
	private Customer customerWithActivatedCard;
	private Customer customerWithNotActivatedCard;
	private Customer customerWithoutCard;

	private AccumulationCard activatedCard;
	private AccumulationCard notActivatedCard;
	
	private AccumulationCard cardForDiscounts;
	private Customer customerForDiscounts;

	@Before
	public void setUpDiscounts() {
		Double baseAmount = 100d;
		cardForDiscounts = new AccumulationCard();
		cardForDiscounts.setAmount(baseAmount);
		customerForDiscounts = new Customer();
		when(orderForDiscounts.getCustomer()).thenReturn(customerForDiscounts);
	}
	
	@Before
	public void setUp() throws Exception {
		customerWithActivatedCard = new Customer("name1");
		customerWithNotActivatedCard = new Customer("name2");
		customerWithoutCard = new Customer("name3");
		activatedCard = new AccumulationCard();
		activatedCard.setAmount(100d);
		activatedCard.setIsActivated(true);
		activatedCard.setOwner(customerWithActivatedCard);
		notActivatedCard = new AccumulationCard();
		notActivatedCard.setAmount(100d);
		notActivatedCard.setIsActivated(false);
		notActivatedCard.setOwner(customerWithNotActivatedCard);
		when(cardRep.hasAccumulationCard(customerWithActivatedCard)).thenReturn(true);
		when(cardRep.hasAccumulationCard(customerWithNotActivatedCard)).thenReturn(true);
		when(cardRep.hasAccumulationCard(customerWithoutCard)).thenReturn(false);
		when(cardRep.getCardByOwner(customerWithActivatedCard)).thenReturn(activatedCard);
		when(cardRep.getCardByOwner(customerWithNotActivatedCard)).thenReturn(notActivatedCard);
		when(cardRep.update(notActivatedCard)).thenReturn(notActivatedCard);
		when(cardRep.update(activatedCard)).thenReturn(activatedCard);
		when(cardRep.create(any(AccumulationCard.class))).thenReturn(notActivatedCard);
		accCardService = new AccumulationCardServiceImpl(cardRep, discountService);
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
		when(cardRep.hasAccumulationCard(customerWithoutCard)).thenReturn(true);
		when(cardRep.getCardByOwner(customerWithoutCard)).thenReturn(new AccumulationCard());
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
		when(cardRep.update(notActivatedCard)).thenReturn(notActivatedCard);
		notActivatedCard.setIsActivated(true);
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
//		when(cardRep.update(any(AccumulationCard.class))).thenReturn(activatedCard);
		assertTrue(accCardService.deactivateAccumulationCardForCustomer(customerWithActivatedCard));
	}

	@Test
	public void testDeactivateAccumulationCardForCustomerWithActivatedCardChangesCardState() {
		System.out.println("test deactivateAccumulationCardForCustomer with activated card "
				+ "changes card state");
		AccumulationCard cardBefore = accCardService.getAccumulationCardByCustomer(customerWithActivatedCard);
		boolean before = cardBefore.getIsActivated();
		when(cardRep.update(activatedCard)).thenReturn(activatedCard);
		activatedCard.setIsActivated(false);
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
		accCardService.deactivateAccumulationCardForCustomer(customerWithNotActivatedCard);
		when(cardRep.update(notActivatedCard)).thenReturn(notActivatedCard);
		AccumulationCard cardAfter = accCardService.getAccumulationCardByCustomer(customerWithNotActivatedCard);
		boolean after = cardAfter.getIsActivated();
		assertEquals(before, after);
	}

	@Test
	public void testDeactivateAccumulationCardForCustomerWithoutCardReturnsFalse() {
		System.out.println("test deactivateAccumulationCardForCustomer without card returns false");
		assertFalse(accCardService.deactivateAccumulationCardForCustomer(customerWithoutCard));
	}
	
	@Test
	public void testCalculateDiscountWithCardPercentage() {
		System.out.println("test calculate discount with card percentage");
		double orderPriceWithDiscounts = 100d;
		Double discount = accCardService.calculateDiscount(cardForDiscounts, orderPriceWithDiscounts);
		double expectedDiscount = 10d;
		double eps = 1E-5;
		assertEquals(expectedDiscount, discount, eps);
	}
	
	@Test
	public void testCalculateDiscountWithTotalPricePercentage() {
		System.out.println("test calculate discount with total price percentage");
		double orderPriceWithDiscounts = 30d;
		Double discount = accCardService.calculateDiscount(cardForDiscounts, orderPriceWithDiscounts);
		double expectedDiscount = 9d;
		double eps = 1E-5;
		assertEquals(expectedDiscount, discount, eps);
	}

	@Test
	public void testCalculateDiscountWhenTotalPriceAndCardPercentagesAreEqual() {
		System.out.println("test calculate discount when total price and " + "card percentages are equal");
		double cardAmount = 300d;
		cardForDiscounts.setAmount(cardAmount);
		double orderPriceWithDiscounts = 100d;
		Double discount = accCardService.calculateDiscount(cardForDiscounts, orderPriceWithDiscounts);
		double expectedDiscount = 30d;
		double eps = 1E-5;
		assertEquals(expectedDiscount, discount, eps);
	}

	@Test
	public void testUseDiscountWithCardPercentage() {
		System.out.println("test use discount with card percentage");
		double orderPrice = 100d;
		when(discountService.calculatePriceWithDiscounts(orderForDiscounts)).thenReturn(orderPrice);
		double eps = 1E-5;
		double newCardAmount = cardForDiscounts.getAmount() + orderPrice;
		accCardService.use(cardForDiscounts, orderForDiscounts);
		assertEquals(newCardAmount, cardForDiscounts.getAmount(), eps);
	}

	@Test
	public void testUseDiscountWithTotalPricePercentage() {
		System.out.println("test use discount with total price percentage");
		double totalPrice = 30d;
		when(discountService.calculatePriceWithDiscounts(orderForDiscounts)).thenReturn(totalPrice);
		double eps = 1E-5;
		double newCardAmount = cardForDiscounts.getAmount() + totalPrice;
		accCardService.use(cardForDiscounts, orderForDiscounts);
		assertEquals(newCardAmount, cardForDiscounts.getAmount(), eps);
	}

	@Test
	public void testUseDiscountWhenTotalPriceAndCardPercentagesAreEqual() {
		System.out.println("test use discount when total price and " + "card percentages are equal");
		double cardAmount = 300d;
		cardForDiscounts.setAmount(cardAmount);
		double totalPrice = 100d;
		when(discountService.calculatePriceWithDiscounts(orderForDiscounts)).thenReturn(totalPrice);
		double eps = 1E-5;
		double newCardAmount = cardForDiscounts.getAmount() + totalPrice;
		accCardService.use(cardForDiscounts, orderForDiscounts);
		assertEquals(newCardAmount, cardForDiscounts.getAmount(), eps);
	}
}
