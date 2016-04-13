package ua.rd.pizzaservice.repository.pizza;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import infrastructure.Benchmark;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.domain.pizza.Pizza.PizzaType;

@Repository
public class InMemPizzaRepository implements PizzaRepository {

    List<Pizza> pizzas = new ArrayList<>();

    @PostConstruct
    public void cookPizzas() {
        pizzas.add(new Pizza(1, "Margarita", 60d, PizzaType.MEAT));
        pizzas.add(new Pizza(2, "SeaPizza", 90d, PizzaType.SEA));
        pizzas.add(new Pizza(3, "Ayurveda", 80d, PizzaType.VEGETERIAN));
    }

    public void init() {
        System.out.println("init method");
    }

    @Benchmark
    @Override
    public Pizza getPizzaByID(Integer id) {
        Pizza result = null;
        for (Pizza pizza : pizzas) {
            if (pizza.getId() == id) {
                result = pizza;
                break;
            }
        }
        return result;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

}
