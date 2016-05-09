package ua.rd.pizzaservice.repository.accumulationcard;

import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAAccumulationCardRepository extends GenericDaoJPAImpl<AccumulationCard, Integer>
		implements AccumulationCardRepository {

	@Override
	public List<AccumulationCard> getAllAccumulationCards() {
		return findAll("findAllAccumulationCards");
	}

	@Override
	public Optional<Customer> getOwner(AccumulationCard card) {
		return Optional.ofNullable(card.getOwner());
	}

	@Override
	public AccumulationCard read(Integer id) {
		System.out.println("Got id: " + id);
		TypedQuery<AccumulationCard> query = em.createNamedQuery("findAccumulationCard", AccumulationCard.class);
		query.setParameter("id", id);
		AccumulationCard accumulationCard = query.getSingleResult();
		accumulationCard.getOwner().getAddresses();
		return accumulationCard;
	}

}
