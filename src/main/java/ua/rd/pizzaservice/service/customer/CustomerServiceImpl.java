package ua.rd.pizzaservice.service.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.repository.customer.CustomerRepository;

public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository custRep;
	
	@Override
	public Customer create(Customer customer) {
		return custRep.create(customer);
	}

	@Override
	public Customer read(Integer id) {
		return custRep.read(id);
	}

	@Override
	public List<Customer> getAllCustomers() {
		return custRep.getAllCustomers();
	}

	@Override
	public Customer update(Customer customer) {
		return custRep.update(customer);
	}

	@Override
	public void delete(Customer customer) {
		custRep.delete(customer);
	}

}
