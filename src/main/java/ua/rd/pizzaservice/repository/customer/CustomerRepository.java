package ua.rd.pizzaservice.repository.customer;

import java.util.List;

import ua.rd.pizzaservice.domain.customer.Customer;

public interface CustomerRepository {

	void create(Customer customer);
	
	Customer read(Integer id);
	
	List<Customer> getAllCustomers();
	
	void update(Customer customer);
	
	void delete(Customer customer);
}
