package ua.rd.pizzaservice.repository.pizza;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

@Repository
public class SpringJDBCPizzaRepository implements PizzaRepository {

    private JdbcTemplate jdbcTemplateObject;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplateObject = new JdbcTemplate(dataSource);
    }

    public void createPizza(String name, Double price, PizzaType pizzaType) {
        String SQL = "INSERT INTO pizza (name, price, type) values (?, ?, ?)";
        jdbcTemplateObject.update(SQL, name, price, pizzaType.toString());
    }

    public Optional<Pizza> getPizza(Integer id) {
        String SQL = "SELECT * FROM pizza WHERE id = ?";
        Pizza pizza = jdbcTemplateObject.query(SQL, new Object[]{id}, new PizzaSetExtractor());
        Optional<Pizza> pizzaOp = Optional.ofNullable(pizza);
        return pizzaOp;
    }

    class PizzaSetExtractor implements ResultSetExtractor<Pizza>{

        @Override
        public Pizza extractData(ResultSet rs)
                throws SQLException, DataAccessException {
            Pizza pizza = new Pizza();
            if (!rs.next()) {
                return null;
            }
            pizza.setId(rs.getInt("id"));
            pizza.setName(rs.getString("name"));
            pizza.setPrice(rs.getDouble("price"));
            pizza.setType(PizzaType.valueOf(rs.getString("type")));
            return pizza;
        }
    }

    public void deletePizza(Integer id) {
        String SQL = "DELETE FROM pizza WHERE id = ?";
        jdbcTemplateObject.update(SQL, id);
    }

    public void updatePizza(Integer id, String newName, Double newPrice, PizzaType newPizzaType) {
        String SQL = "UPDATE pizza SET (name, price, type) = (?, ?, ?) WHERE id = ?";
        jdbcTemplateObject.update(SQL, newName, newPrice, newPizzaType.toString(), id);
    }

    @Override
    public Pizza getPizzaByID(Integer id) {
        return getPizza(id).get();
    }

}
