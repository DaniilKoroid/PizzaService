package ua.rd.pizzaservice;

import infrastructure.ApplicationContext;
import infrastructure.JavaConfigApplicationContext;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;
import ua.rd.pizzaservice.service.order.OrderService;

public class PizzaApp {
    public static void main(String[] args) throws Exception {
        Customer customer = null;
        Order order;

        ApplicationContext ac = new JavaConfigApplicationContext();
        PizzaRepository pizzaRepository = (PizzaRepository) ac
                .getBean("pizzaRepository");
        System.out.println(pizzaRepository.getPizzaByID(1));

        OrderService orderService = (OrderService) ac.getBean("orderService");
        order = orderService.placeNewOrder(customer, 1, 2, 3);

        System.out.println(order);
    }
}
