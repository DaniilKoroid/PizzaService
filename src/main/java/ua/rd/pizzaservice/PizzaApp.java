package ua.rd.pizzaservice;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.service.order.OrderService;
import ua.rd.pizzaservice.service.order.SimpleOrderService;

public class PizzaApp {
    public static void main(String[] args) {

        ConfigurableApplicationContext repContext = new ClassPathXmlApplicationContext(
                "repositoryContext.xml");
        ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[]{"appContext.xml"}, repContext);

        Order order;
        Customer customer = appContext.getBean(Customer.class);
        OrderService orderService = appContext.getBean(SimpleOrderService.class);
        Integer[] pizzasId = new Integer[]{1, 2, 3};
        order = orderService.placeNewOrder(customer, pizzasId);

        System.out.println(order);

        appContext.close();
    }
}
