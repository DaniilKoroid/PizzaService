package ua.rd.pizzaservice.repository.discount;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ua.rd.pizzaservice.domain.discount.Discount;
import ua.rd.pizzaservice.domain.order.Order;
import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.service.discount.DiscountProvider;
import ua.rd.pizzaservice.service.discount.InMemDiscountProvider;

@RunWith(MockitoJUnitRunner.class)
public class InMemDiscountRepositoryTest {

    @Mock
    Order            order;

    @Mock
    List<Pizza>      pizzas;

    DiscountProvider discountRepository;

    @Before
    public void setUpDiscountRepository() {
        discountRepository = new InMemDiscountProvider();
        ((InMemDiscountProvider) discountRepository).determineDiscounts();
    }

    @Test
    public void testGetAppliableDiscountsReturnsForOrderWithAppliableDiscountsListWithDiscounts() {
        System.out.println(
                "test getAppliableDiscounts returns for order with appliable discounts "
                        + "list with discounts");
        Integer appliableSize = 4;
        when(order.getPizzas()).thenReturn(pizzas);
        when(pizzas.size()).thenReturn(appliableSize);
        List<Discount> appliableDiscounts = discountRepository
                .getAppliableDiscounts(order);
        int expectedSize = 1;
        int actualSize = appliableDiscounts.size();
        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void testGetAppliableDiscountsReturnsEmptyListWhenThereAreNoAppliableDiscounts() {
        System.out.println(
                "test getAppliableDiscounts returns empty list when there are no "
                        + "appliable discounts");
        Integer notAppliableSize = 3;
        when(order.getPizzas()).thenReturn(pizzas);
        when(pizzas.size()).thenReturn(notAppliableSize);
        List<Discount> appliableDiscounts = discountRepository
                .getAppliableDiscounts(order);
        int expectedSize = 0;
        int actualSize = appliableDiscounts.size();
        assertEquals(expectedSize, actualSize);
        assertEquals(Collections.emptyList(), appliableDiscounts);
    }
}
