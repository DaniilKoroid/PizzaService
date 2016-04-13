package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.pizza.InMemPizzaRepository;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;
import ua.rd.pizzaservice.service.order.OrderService;
import ua.rd.pizzaservice.service.order.SimpleOrderService;

public class SpringPizzaApp {

    public static void main(String[] args) {

        ConfigurableApplicationContext repositoryContext =
                new ClassPathXmlApplicationContext("repositoryContext.xml");

        ConfigurableApplicationContext appContext =
                new ClassPathXmlApplicationContext(new String[]{"appContext.xml"}, repositoryContext);

        PizzaRepository pizzaRepository = (PizzaRepository) appContext
                .getBean(InMemPizzaRepository.class);
        OrderService orderService = (OrderService) appContext
                .getBean(SimpleOrderService.class);

        System.out.println(pizzaRepository.getPizzaByID(1));
        Order order = orderService.placeNewOrder(null, 1, 2, 3);
        System.out.println(order);

        Customer customer = appContext.getBean(Customer.class);
        System.out.println("appContext customer: " + customer);

        appContext.close();
        repositoryContext.close();
    }

}
