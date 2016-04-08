package ua.rd.pizzaservice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.repository.pizza.PizzaRepository;
import ua.rd.pizzaservice.service.order.OrderService;

public class SpringPizzaApp {

    public static void main(String[] args) {

        ConfigurableApplicationContext repositoryContext =
                new ClassPathXmlApplicationContext("repositoryContext.xml");

        ConfigurableApplicationContext appContext =
                new ClassPathXmlApplicationContext(new String[]{"appContext.xml"}, false);

        appContext.setParent(repositoryContext);
        appContext.refresh();

        PizzaRepository pizzaRepository = (PizzaRepository) appContext
                .getBean("pizzaRepository");
        OrderService orderService = (OrderService) appContext
                .getBean("orderService");
        System.out.println(pizzaRepository.getPizzaByID(1));
        Order order = orderService.placeNewOrder(null, 1, 2, 3);
        System.out.println(order);

        Customer customer = appContext.getBean(Customer.class);
        System.out.println("appContext customer: " + customer);

        Customer parentCustomer = appContext.getParent().getBean(Customer.class);
        System.out.println("repositoryContext customer: " + parentCustomer);

        Pizza pizza = appContext.getBean(Pizza.class);
        System.out.println(pizza);

        ApplicationContext parent = appContext.getParent();
        System.out.println("Parent: " + parent);

        appContext.close();
        repositoryContext.close();
    }

}
