package ua.rd.pizzaservice.repository.accumulationcard;

import java.util.List;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;

public interface AccumulationCardRepository {

	AccumulationCard create(AccumulationCard card);

	AccumulationCard read(Integer id);

	List<AccumulationCard> getAllAccumulationCards();

	AccumulationCard update(AccumulationCard card);

	void delete(AccumulationCard card);
}
