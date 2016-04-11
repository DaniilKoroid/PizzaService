package ua.rd.pizzaservice.repository.pizza;

import java.util.ArrayList;
import java.util.List;

import infrastructure.Benchmark;
import infrastructure.PostConstruction;
import ua.rd.pizzaservice.domain.pizza.Pizza;

public class InMemPizzaRepository implements PizzaRepository {

    List<Pizza> pizzas = new ArrayList<>();

    @PostConstruction
    public void cookPizzas() {
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
