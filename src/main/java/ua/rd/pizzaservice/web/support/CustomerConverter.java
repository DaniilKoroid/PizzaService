package ua.rd.pizzaservice.web.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.service.customer.CustomerService;

public class CustomerConverter implements Converter<String, Customer> {

	@Autowired
	private CustomerService customerService;
	
	@Override
	public Customer convert(String source) {
		Integer customerId = Integer.valueOf(source);
		Customer customer = customerService.read(customerId);
		return customer;
	}

}
