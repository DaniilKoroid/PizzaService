package ua.rd.pizzaservice.repository.customer;

import java.util.List;

import javax.persistence.Persistence;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPACustomerRepository extends GenericDaoJPAImpl<Customer, Integer> implements CustomerRepository {

	public GenericDaoJPACustomerRepository() {
		emf = Persistence.createEntityManagerFactory("jpa_mysql");
	}
	
	public GenericDaoJPACustomerRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
	}
	
	@Override
	public List<Customer> getAllCustomers() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
