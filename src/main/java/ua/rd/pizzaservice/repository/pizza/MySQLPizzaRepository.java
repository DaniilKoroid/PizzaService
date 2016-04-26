package ua.rd.pizzaservice.repository.pizza;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

@Repository
public class MySQLPizzaRepository implements PizzaRepository {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void create(Pizza pizza) {
		String sqlAdd = "INSERT INTO pizza (name, price, type) VALUES (?, ?, ?);";
		jdbcTemplate.update(sqlAdd, pizza.getName(), pizza.getPrice(), pizza.getType().ordinal());
	}

	@Override
	public Pizza getPizzaByID(Integer id) {
		String sqlGet = "SELECT name, price, type FROM pizza WHERE id = ?;";
		Pizza pizza = jdbcTemplate.queryForObject(sqlGet, 
				new Object[]{id},
				new RowMapper<Pizza>() {

					@Override
					public Pizza mapRow(ResultSet rs, int rowNum) throws SQLException {
						Pizza pizza = new Pizza();
						pizza.setId(id);
						pizza.setName(rs.getString("name"));
						pizza.setPrice(rs.getDouble("price"));
						pizza.setType(PizzaType.values()[rs.getInt("type")]);
						return pizza;
					}
		});
		return pizza;
	}

	@Override
	public List<Pizza> getAllPizzas() {
		String sql = "SELECT id, name, price, type FROM pizza";
		List<Pizza> pizzas = new ArrayList<>();
		
		List<Map<String,Object>> queryForList = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> map : queryForList) {
			Pizza pizza = new Pizza();
			pizza.setId((Integer) map.get("id"));
			pizza.setName((String) map.get("name"));
			pizza.setPrice((Double) map.get("price"));
			Integer ordinalType = (Integer) map.get("type");
			pizza.setType(PizzaType.values()[ordinalType]);
			pizzas.add(pizza);
		}
		return pizzas;
	}

	@Override
	public void update(Pizza pizza) {
		String sqlUpdate = "UPDATE pizza SET name = ?, price = ?, type = ?";
		jdbcTemplate.update(sqlUpdate, pizza.getName(), pizza.getPrice(), pizza.getType().ordinal());
	}

	@Override
	public void delete(Pizza pizza) {
		String sqlDelete = "DELETE FROM pizza where id = ?";
		jdbcTemplate.update(sqlDelete, pizza.getId());
	}

}
