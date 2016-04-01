package infrastructure;

import java.util.HashMap;
import java.util.Map;

import ua.rd.pizzaservice.repository.order.InMemOrderRepository;
import ua.rd.pizzaservice.repository.pizza.InMemPizzaRepository;
import ua.rd.pizzaservice.service.order.SimpleOrderService;

public class JavaConfig implements Config {

	private static Map<String, Class<?>> beans = new HashMap<>();
	
	{
		beans.put("orderRepository", InMemOrderRepository.class);
		beans.put("pizzaRepository", InMemPizzaRepository.class);
		beans.put("orderService", SimpleOrderService.class);
	}
	
	@Override
	public Class<?> getImpl(String bean) {
		return beans.get(bean);
	}

}
