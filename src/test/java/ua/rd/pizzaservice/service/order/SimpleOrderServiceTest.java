package ua.rd.pizzaservice.service.order;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;

@ContextConfiguration(locations = {
        "classpath:/appContext.xml"}, inheritInitializers = true)
public class SimpleOrderServiceTest extends RepositoryTestConfig {

    @Autowired
    OrderService orderService;

    @Test
    public final void testPlaceNewOrder() {
        System.out.println("placeNewOrder");
        Customer customer = null;
        Integer[] pizzasId = new Integer[]{1};
        Order result = orderService.placeNewOrder(customer, pizzasId);
        System.out.println(result);
    }

}
