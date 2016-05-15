package ua.rd.pizzaservice.service.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.domain.customer.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/repositoryH2TestContext.xml", "classpath:/appTestContext.xml"})
@Transactional
public class CustomerServiceImplInMemDBIT {

	@Autowired
	private CustomerService customerService;

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	public void testCreateCustomer() {
		String sqlForCustomer = "SELECT * FROM customer WHERE id = ?";
		String sqlForAddress = "SELECT address_id FROM customer_address WHERE customer_id = ?";
		
		Customer expectedCustomer = new Customer("SayMyName");
		Address address = new Address(null, "Ukraine", "Kyiv", null, null, null, null);
		expectedCustomer.addAddress(address);
		expectedCustomer = customerService.create(expectedCustomer);
		customerService.getAllCustomers();
		
		Customer actualCustomer = jdbcTemplate.queryForObject(sqlForCustomer, new Object[] { expectedCustomer.getId() },
				new BeanPropertyRowMapper<Customer>(Customer.class));
		
		Integer addrId = jdbcTemplate.queryForObject(sqlForAddress, new Object[] { expectedCustomer.getId() },
				Integer.class);
		
		assertEquals(expectedCustomer.getName(), actualCustomer.getName());
		assertEquals(expectedCustomer.getAddresses().iterator().next().getId(), addrId);
	}
	
	@Test
	public void testReadReturnsActualCustomer() {
		String name = "Vova";
		Integer customerId = insertCustomer(name);
		Customer actualCustomer = customerService.read(customerId);
		customerService.getAllCustomers();
		assertNotNull(actualCustomer.getId());
		assertEquals(name, actualCustomer.getName());
	}
	
	@Test
	public void testUpdateCustomer() {
		String sqlForCustomer = "SELECT * FROM customer WHERE id = ?";
		String oldName = "Arya";
		String newName = "Little girl has no name";
		Integer customerId = insertCustomer(oldName);
		Customer customer = customerService.read(customerId);
		customerService.getAllCustomers();
		assertEquals(oldName, customer.getName());
		customer.setName(newName);
		Customer updatedCustomer = customerService.update(customer);
		customerService.getAllCustomers();
		Customer actualUpdatedCustomer = jdbcTemplate.queryForObject(sqlForCustomer, new Object[] { customerId },
				new BeanPropertyRowMapper<Customer>(Customer.class));
		assertEquals(newName, updatedCustomer.getName());
		assertEquals(newName, actualUpdatedCustomer.getName());
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void deleteCustomerTest() {
		String name = "Vova";
		Integer customerId = insertCustomer(name);
		Customer customer = customerService.read(customerId);
		customerService.delete(customer);
		customerService.getAllCustomers();
		final String selectSQL = "SELECT * FROM customer WHERE id = ?";

		jdbcTemplate.queryForObject(selectSQL, new Object[] { customerId },
				new BeanPropertyRowMapper<Customer>(Customer.class));
	}
	
	@Test
	public void testProposeAddressCustomerAfterProposeNotHavedAddressNowHasIt() {
		String country = "Ukraine";
		String city = "Kyiv";
		String street = "Gonchara";
		String building = "18";
		String flatNumber = "12";
		String zipCode = "1000";
		Integer addressId = insertAddress(country, city, street, building, flatNumber, zipCode);
		
		String name = "Vova";
		Integer customerId = insertCustomer(name);

		String selectAddressById = "SELECT * FROM address WHERE id = ?";
		Address jdbcAddress = jdbcTemplate.queryForObject(selectAddressById, new Object[] { addressId },
				new BeanPropertyRowMapper<Address>(Address.class));
		
		String selectCustomerById = "SELECT * FROM customer WHERE id = ?";
		Customer jdbcCustomer = jdbcTemplate.queryForObject(selectCustomerById, new Object[] { customerId },
				new BeanPropertyRowMapper<Customer>(Customer.class));
		
		customerService.proposeAddress(jdbcAddress, jdbcCustomer);
		
		assertTrue(hasAddress(jdbcCustomer, jdbcAddress));
	}
		
	private boolean hasAddress(Customer customer, Address address) {
		boolean result = false;
		Set<Address> addresses = customer.getAddresses();
		for (Address customerAddr : addresses) {
			if (customerAddr.equals(address)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private Integer insertCustomer(String name) {
		String sql = "INSERT INTO customer(name, id) values (?, customer_sequence.NEXTVAL)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
				ps.setString(1, name);
				return ps;
			}
		}, keyHolder);

		Integer customerId = keyHolder.getKey().intValue();
		return customerId;
	}
	
	private Integer insertAddress(String country, String city, String street, String building, String flatNumber,
			String zipCode) {
		String sql = "INSERT INTO address (country, city, street, building, flatNumber, zipCode, id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, address_sequence.NEXTVAL)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
				ps.setString(1, country);
				ps.setString(2, city);
				ps.setString(3, street);
				ps.setString(4, building);
				ps.setString(5, flatNumber);
				ps.setString(6, zipCode);
				return ps;
			}
		}, keyHolder);

		Integer addressId = keyHolder.getKey().intValue();
		return addressId;
	}

}
