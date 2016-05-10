package ua.rd.pizzaservice.service.customer;

import java.util.List;

import ua.rd.pizzaservice.domain.customer.Customer;

public interface CustomerService {

	Customer create(Customer customer);

	Customer read(Integer id);

	List<Customer> getAllCustomers();

	Customer update(Customer customer);

	void delete(Customer customer);
}
