package ua.rd.pizzaservice.repository;

import java.util.List;

import ua.rd.pizzaservice.domain.Customer;

public interface CustomerRepository {

	Customer create(Customer customer);
	
	Customer read(Integer id);
	
	List<Customer> getAllCustomers();
	
	Customer update(Customer customer);
	
	void delete(Customer customer);
}
