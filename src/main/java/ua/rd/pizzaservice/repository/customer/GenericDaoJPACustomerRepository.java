package ua.rd.pizzaservice.repository.customer;

import java.util.List;

import javax.persistence.TypedQuery;

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
		TypedQuery<Customer> query = em.createNamedQuery("findCustomer", Customer.class);
		query.setParameter("id", id);
		Customer customer = query.getSingleResult();
		return customer;
	}

}
