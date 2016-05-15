package ua.rd.pizzaservice.service.accumulationcard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/repositoryH2TestContext.xml", "classpath:/appTestContext.xml"})
@Transactional
public class SimpleAccumulationCardServiceInMemDBTest {

	@Autowired
	private AccumulationCardService service;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerReturnsTrueOnAssigningCardToCustomerWithoutCard() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerReturnsFalseForCustomerWithCard() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		Boolean assignedToCustomerWithCard = service.assignNewAccumulationCardToCustomer(customer);
		invokeJPQL();
		assertFalse(assignedToCustomerWithCard);
	}

	@Test
	public void testAssignNewAccumulationCardToCustomerCreatedCardForCustomerWithoutCard() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		getCardByCustomerViaServiceMethod(customer);
		
	}

	@Test
	public void testActivateAccumulationCardForCustomerReturnsTrueWhenActivatingDeactivatedCard() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		AccumulationCard card = service.getAccumulationCardByCustomer(customer);
		service.read(card.getId());
		assertFalse(card.getIsActivated());
		Boolean isActivated = service.activateAccumulationCardForCustomer(customer);
		assertTrue(isActivated);
	}
	
	@Test
	public void testActivateAccumulationCardForCustomerReturnsFalseWhenCardIsActivated() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		AccumulationCard card = getCardByCustomerViaServiceMethod(customer);
		assertFalse(card.getIsActivated());		
		Boolean isActivated = service.activateAccumulationCardForCustomer(customer);
		card = service.getAccumulationCardByCustomer(customer);
		assertTrue(isActivated);
		assertTrue(card.getIsActivated());
		Boolean isActivatedAgain = service.activateAccumulationCardForCustomer(customer);
		assertFalse(isActivatedAgain);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testGetAccumulationCardThrowsNoSuchElementExceptionWhenCustomerHasNoCard() {
		Customer customer = createCustomer();
		service.getAccumulationCardByCustomer(customer);
	}
	
	@Test
	public void testActivateAccumulationCardForCustomerWithourCardReturnsFalse() {
		Customer customer = new Customer("Sveta");
		insertCustomerToDB(customer);
		Boolean isActivated = service.activateAccumulationCardForCustomer(customer);
		invokeJPQL();
		assertFalse(isActivated);
	}
	
	@Test
	public void testDeactivateAccumulationCardForCustomerReturnsTrueOnDeactivating() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		AccumulationCard card = getCardByCustomerViaServiceMethod(customer);
		assertFalse(card.getIsActivated());		
		Boolean isActivated = service.activateAccumulationCardForCustomer(customer);
		card = service.getAccumulationCardByCustomer(customer);
		assertTrue(isActivated);
		assertTrue(card.getIsActivated());
		Boolean isDeactivated = service.deactivateAccumulationCardForCustomer(customer);
		assertTrue(isDeactivated);
	}
	
	@Test
	public void testDeactivateAccumulationCardForCustomerDeactivatesCard() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		AccumulationCard card = getCardByCustomerViaServiceMethod(customer);
		assertFalse(card.getIsActivated());		
		Boolean isActivated = service.activateAccumulationCardForCustomer(customer);
		card = service.getAccumulationCardByCustomer(customer);
		assertTrue(isActivated);
		assertTrue(card.getIsActivated());
		Boolean isDeactivated = service.deactivateAccumulationCardForCustomer(customer);
		assertTrue(isDeactivated);
		card = service.getAccumulationCardByCustomer(customer);
		assertFalse(card.getIsActivated());
	}

	@Test
	public void testGetAccumulationCardByCustomer() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		AccumulationCard assignedCard = getCardByCustomerViaServiceMethod(customer);
		AccumulationCard cardByCustomer = service.getAccumulationCardByCustomer(customer);
		assertEquals(assignedCard, cardByCustomer);
	}

	@Test
	public void testGetOwnerReturnsAppropriateOwner() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		AccumulationCard card = getCardByCustomerViaServiceMethod(customer);
		Customer ownerByService = service.getOwner(card);
		assertEquals(customer, ownerByService);
	}

	@Test
	public void testGetOwnerReturnsNullWhenCardHasNoOwner() {
		AccumulationCard card = new AccumulationCard();
		Customer owner = service.getOwner(card);
		assertNull(owner);
	}

	@Test
	public void testHasAccumulationCardWithOwnerThatHasCardReturnsTrue() {
		Customer customer = createCustomer();
		assignCardToCustomer(customer);
		getCardByCustomerViaServiceMethod(customer);
		Boolean hasAccumulationCard = service.hasAccumulationCard(customer);
		assertTrue(hasAccumulationCard);
	}

	private AccumulationCard getCardByCustomerViaServiceMethod(Customer customer) {
		AccumulationCard card = service.getAccumulationCardByCustomer(customer);
		invokeJPQL();
		assertNotNull(card);
		return card;
	}

	private void assignCardToCustomer(Customer customer) {
		Boolean isAssigned = service.assignNewAccumulationCardToCustomer(customer);
		invokeJPQL();
		assertTrue(isAssigned);
	}

	private Customer createCustomer() {
		Customer customer = new Customer("Ivanna");
		insertCustomerToDB(customer);
		return customer;
	}
	
	@Test
	public void testHasAccumulationCardWithOwnerThatHasNoAccumulationCardReturnsFalse() {
		Customer customer = createCustomer();
		Boolean hasAccumulationCard = service.hasAccumulationCard(customer);
		invokeJPQL();
		assertFalse(hasAccumulationCard);
	}
	
	private Integer insertCustomerToDB(Customer customer) {
		String sqlInsert = "INSERT INTO customer (name, id) VALUES (?, customer_sequence.NEXTVAL)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update((Connection con) -> {
			PreparedStatement ps = con.prepareStatement(sqlInsert, new String[]{"id"});
			ps.setString(1, customer.getName());
			return ps;
		}, keyHolder);
		Integer id = keyHolder.getKey().intValue();
		return id;
	}

	private void invokeJPQL() {
		try {
			service.read(17);
		} catch (Exception ex) {}
	}
}
