package ua.rd.pizzaservice.repository.customer;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPACustomerRepository extends GenericDaoJPAImpl<Customer, Integer> implements CustomerRepository {

	@Override
	public List<Customer> getAllCustomers() {
		return findAll("findAllCustomers");
	}

	@Override
	public Customer read(Integer id) {
		Query namedQuery = em.createNamedQuery("findCustomer");
		namedQuery.setParameter("id", id);
		Object result = namedQuery.getSingleResult();
		Customer customer = (Customer) result;
		return customer;
	}

}
