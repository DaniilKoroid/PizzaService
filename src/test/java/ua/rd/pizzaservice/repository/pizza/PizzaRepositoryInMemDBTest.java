package ua.rd.pizzaservice.repository.pizza;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;
import ua.rd.pizzaservice.repository.PizzaRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/repositoryH2TestContext.xml"})
public class PizzaRepositoryInMemDBTest {

	@Autowired
	private PizzaRepository pizzaRep;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Test
	public void testGetPizzaByID() {
		String name = "PizzaName";
		Double price = 120d;
		PizzaType type = PizzaType.SEA;
		Integer id = insertPizza(name, price, type);
		
		Pizza expectedPizza = new Pizza(id, name, price, type);
		Pizza actualPizza = pizzaRep.getPizzaByID(id);
		assertEquals(expectedPizza, actualPizza);
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
