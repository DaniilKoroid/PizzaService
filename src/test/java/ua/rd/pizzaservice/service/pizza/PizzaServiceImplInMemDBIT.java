package ua.rd.pizzaservice.service.pizza;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/repositoryH2TestContext.xml", "classpath:/appTestContext.xml"})
@Transactional
public class PizzaServiceImplInMemDBIT {

	@Autowired
	private PizzaService pizzaService;

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Test
	public void testCreatePizza() {
		String sqlForPizza = "SELECT id, name, price, type FROM pizza WHERE id = ?";
		
		Pizza expectedPizza = new Pizza();
		expectedPizza.setName("Marga");
		expectedPizza.setPrice(120d);
		expectedPizza.setType(PizzaType.MEAT);
		expectedPizza = pizzaService.create(expectedPizza);
		pizzaService.getPizzaByID(expectedPizza.getId());
		Pizza actualPizza = jdbcTemplate.query(sqlForPizza, new Object[] { expectedPizza.getId()}, new ResultSetExtractor<Pizza>() {

			@Override
			public Pizza extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				Pizza pizza = new Pizza();
				pizza.setId(rs.getInt(1));
				pizza.setName(rs.getString(2));
				pizza.setPrice(rs.getDouble(3));
				pizza.setType(PizzaType.values()[rs.getInt(4)]);
				return pizza;
			}
		});
		assertEquals(expectedPizza, actualPizza);
	}
	
	@Test
	public void testReadReturnsActualCustomer() {
		String name = "Marg";
		Double price = 144.1d;
		PizzaType type = PizzaType.SEA;
		Integer pizzaId = insertPizza(name, price, type);
		Pizza actualPizza = pizzaService.getPizzaByID(pizzaId);
		pizzaService.getPizzaByID(actualPizza.getId());
		assertNotNull(actualPizza.getId());
		assertEquals(name, actualPizza.getName());
		assertEquals(price, actualPizza.getPrice(), 1e-8);
		assertEquals(type, actualPizza.getType());
	}
	
	@Test
	public void testUpdatePizza() {
		String sqlForPizza = "SELECT id, name, price, type FROM pizza WHERE id = ?";
		Double price = 155.5d;
		PizzaType type = PizzaType.MEAT;
		String oldName = "Marga";
		String newName = "Rita";
		Integer pizzaId = insertPizza(oldName, price, type);
		Pizza pizza = pizzaService.getPizzaByID(pizzaId);
		assertEquals(oldName, pizza.getName());
		assertEquals(price, pizza.getPrice());
		assertEquals(type, pizza.getType());
		pizza.setName(newName);
		Pizza updatedPizza = pizzaService.update(pizza);
		Customer actualUpdatedCustomer = jdbcTemplate.queryForObject(sqlForPizza, new Object[] { pizzaId },
				new BeanPropertyRowMapper<Customer>(Customer.class));
		assertEquals(newName, updatedPizza.getName());
		assertEquals(newName, actualUpdatedCustomer.getName());
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void testDeletePizza() {
		Double price = 155.5d;
		PizzaType type = PizzaType.MEAT;
		String name = "Marga";
		Integer pizzaId = insertPizza(name, price, type);
		Pizza pizza = pizzaService.getPizzaByID(pizzaId);
		pizzaService.delete(pizza);
		
		final String selectSQL = "SELECT * FROM pizza WHERE id = ?";

		jdbcTemplate.queryForObject(selectSQL, new Object[] { pizzaId },
				new BeanPropertyRowMapper<Pizza>(Pizza.class));
	}
	
	private Integer insertPizza(String name, Double price, PizzaType type) {
		String sql = "INSERT INTO pizza (name, price, type, id) values (?, ?, ?, pizza_sequence.NEXTVAL)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
				ps.setString(1, name);
				ps.setDouble(2, price);
				ps.setInt(3, type.ordinal());
				return ps;
			}
		}, keyHolder);

		Integer pizzaId = keyHolder.getKey().intValue();
		return pizzaId;
	}

}
