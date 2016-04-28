package ua.rd.pizzaservice.repository.accumulationcard;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAAccumulationCardRepository extends GenericDaoJPAImpl<AccumulationCard, Integer>
		implements AccumulationCardRepository {

	public GenericDaoJPAAccumulationCardRepository() {
		emf = Persistence.createEntityManagerFactory("jpa_mysql");
	}
	
	public GenericDaoJPAAccumulationCardRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
	}
	
	@Override
	public List<AccumulationCard> getAllAccumulationCards() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Optional<Customer> getOwner(AccumulationCard card) {
		EntityManager em = getEntityManager();
		String sql = "SELECT c from Customer c WHERE c.id = :id";
		TypedQuery<Customer> query = em.createQuery(sql, Customer.class).setParameter("id", card.getOwner().getId());
		Customer customer = query.getSingleResult();
		Optional<Customer> optional = Optional.ofNullable(customer);
		return optional;
	}

}
