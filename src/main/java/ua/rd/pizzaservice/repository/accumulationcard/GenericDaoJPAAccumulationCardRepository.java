package ua.rd.pizzaservice.repository.accumulationcard;

import java.util.List;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAAccumulationCardRepository extends GenericDaoJPAImpl<AccumulationCard, Integer>
		implements AccumulationCardRepository {

	@Override
	public List<AccumulationCard> getAllAccumulationCards() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
