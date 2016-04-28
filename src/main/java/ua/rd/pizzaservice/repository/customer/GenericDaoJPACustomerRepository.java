package ua.rd.pizzaservice.repository.customer;

import java.util.List;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPACustomerRepository extends GenericDaoJPAImpl<Customer, Integer> implements CustomerRepository {

	@Override
	public List<Customer> getAllCustomers() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
