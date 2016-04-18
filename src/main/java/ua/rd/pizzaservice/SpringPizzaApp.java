package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;
import ua.rd.pizzaservice.service.order.OrderService;

public class SpringPizzaApp {

    public static void main(String[] args) {

        ConfigurableApplicationContext repositoryContext = new ClassPathXmlApplicationContext(
                "file:src/main/resources/repositoryContext.xml");

        ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[]{"appContext.xml"}, repositoryContext);

        PizzaRepository pizzaRepository = appContext
                .getBean(PizzaRepository.class);
        OrderService orderService = appContext.getBean(OrderService.class);

        System.out.println(pizzaRepository.getPizzaByID(1));
        Order order = orderService.placeNewOrder(null, 1, 2, 3);
        System.out.println(order);

        appContext.close();
        repositoryContext.close();
    }

}
