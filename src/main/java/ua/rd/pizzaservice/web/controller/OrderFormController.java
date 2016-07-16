package ua.rd.pizzaservice.web.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.domain.Order;
import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.service.address.AddressService;
import ua.rd.pizzaservice.service.customer.CustomerService;
import ua.rd.pizzaservice.service.order.OrderService;
import ua.rd.pizzaservice.service.pizza.PizzaService;

@Controller
public class OrderFormController {

	@Autowired
	private PizzaService pizzaService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(path = "/orders", method = RequestMethod.GET)
	public String listInfoForOrder(Model model) {
		List<Pizza> pizzas = pizzaService.getAllPizzas();
		Collections.sort(pizzas, (p1, p2) ->{
			return p1.getId() - p2.getId();
		});
		model.addAttribute("pizzas", pizzas);
		OrderForm orderForm = new OrderForm();
		model.addAttribute("orderForm", orderForm);
		addAllAddressesToModel(model);
		addAllCustomersToModel(model);
		return "/orders/orderForm";
	}
	
	@RequestMapping(path = "/orders/create", method = RequestMethod.POST)
	public String makeOrder(@ModelAttribute("orderForm") OrderForm orderForm,
							BindingResult bindingResult, Model model) {
		Map<Pizza, Integer> pizzaMap = convertOrderFormToPizzaMap(orderForm.getPizzas());
		Customer customer = orderForm.getCustomer();
		Address deliveryAddress = orderForm.getDeliveryAddress();
		Order order = orderService.placeNewOrder(customer, deliveryAddress, pizzaMap);
		model.addAttribute("order", order);
		return "/orders/orderInfo";
	}
	
	private Map<Pizza, Integer> convertOrderFormToPizzaMap(Map<Integer, String> pizzaMap) {
		Map<Pizza, Integer> pizzas = new HashMap<>();
		for (Entry<Integer, String> entry : pizzaMap.entrySet()) {
			String value = entry.getValue();
			if ((value == null) || (value.isEmpty()) || ("0".equals(value))) {
				continue;
			}
			Integer pizzaAmount = Integer.valueOf(value);
			Pizza pizza = pizzaService.getPizzaByID(entry.getKey());
			pizzas.put(pizza, pizzaAmount);
		}
		return pizzas.isEmpty() ? Collections.emptyMap() : pizzas;
	}
	
	private void addAllAddressesToModel(Model model) {
		List<Address> addresses = addressService.getAllAddresses();
		Collections.sort(addresses, (a1, a2) ->{
			return a1.getId() - a2.getId();
		});
		model.addAttribute("addresses", addresses);
	}
	
	private void addAllCustomersToModel(Model model) {
		List<Customer> customers = customerService.getAllCustomers();
		Collections.sort(customers, (c1, c2) ->{
			return c1.getId() - c2.getId();
		});
		model.addAttribute("customers", customers);
	}
}
