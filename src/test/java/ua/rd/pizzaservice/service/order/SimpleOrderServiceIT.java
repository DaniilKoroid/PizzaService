package ua.rd.pizzaservice.service.order;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.domain.order.Order;

@ContextConfiguration(locations = {
"classpath:/appTestContext.xml"}, inheritInitializers = true)
@ActiveProfiles("test")
public class SimpleOrderServiceIT extends RepositoryTestConfig {

    @Autowired
    OrderService orderService;

    @Test
    public void testPlaceNewOrderWithAppropriatePizzasCount() {
        System.out.println("test placeNewOrder with appropriate pizzas count");
        Integer[] pizzasID = new Integer[] { 1, 2, 3, 3, 2, 1 };
        int expectedOrderSize = pizzasID.length;
        Customer customer = new Customer();
        Order newOrder = orderService.placeNewOrder(customer, pizzasID);
        int orderSize = newOrder.getPizzas().size();
        assertEquals(expectedOrderSize, orderSize);
    }

}
