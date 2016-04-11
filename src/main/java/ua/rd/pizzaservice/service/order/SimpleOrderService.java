package ua.rd.pizzaservice.service.order;

import java.util.ArrayList;
import java.util.List;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.order.OrderRepository;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;

public class SimpleOrderService implements OrderService {

    private PizzaRepository pizzaRepository;
    private OrderRepository orderRepository;
    private Order order;

    public SimpleOrderService(OrderRepository orderRepository,
            PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order placeNewOrder(Customer customer, Integer... pizzasID) {

        List<Pizza> pizzas = pizzasByArrOfId(pizzasID);
        Order newOrder = createOrder();
        newOrder.setCustomer(customer);
        newOrder.setPizzas(pizzas);
        orderRepository.saveOrder(newOrder);
        return newOrder;
    }

    private Order createOrder() {
        return order;
    }

    private List<Pizza> pizzasByArrOfId(Integer... pizzasID) {
        List<Pizza> pizzas = new ArrayList<>();

        for (Integer id : pizzasID) {
            pizzas.add(pizzaRepository.getPizzaByID(id));
        }
        return pizzas;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
