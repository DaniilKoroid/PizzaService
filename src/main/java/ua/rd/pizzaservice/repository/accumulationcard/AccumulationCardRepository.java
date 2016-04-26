package ua.rd.pizzaservice.repository.accumulationcard;

import java.util.List;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;

public interface AccumulationCardRepository {

	void create(AccumulationCard card);

	AccumulationCard read(Integer id);

	List<AccumulationCard> getAllAccumulationCards();

	void update(AccumulationCard card);

	void delete(AccumulationCard card);
}
