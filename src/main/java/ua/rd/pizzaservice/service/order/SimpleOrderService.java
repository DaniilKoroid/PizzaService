package ua.rd.pizzaservice.service.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import infrastructure.Benchmark;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.order.OrderRepository;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;

@Service
public class SimpleOrderService implements OrderService {

    private PizzaRepository pizzaRepository;
    private OrderRepository orderRepository;

    @Autowired
    public SimpleOrderService(OrderRepository orderRepository,
            PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Benchmark
    public Order placeNewOrder(Customer customer, Integer... pizzasID) {

        List<Pizza> pizzas = pizzasByArrOfId(pizzasID);
        Order newOrder = createOrder();
        newOrder.setCustomer(customer);
        newOrder.setPizzas(pizzas);
        orderRepository.saveOrder(newOrder);
        return newOrder;
    }

    @Lookup
    protected Order createOrder() {
        return null;
    }

    private List<Pizza> pizzasByArrOfId(Integer... pizzasID) {
        List<Pizza> pizzas = new ArrayList<>();

        for (Integer id : pizzasID) {
            pizzas.add(pizzaRepository.getPizzaByID(id));
        }
        return pizzas;
    }

}
