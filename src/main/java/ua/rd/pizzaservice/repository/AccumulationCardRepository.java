package ua.rd.pizzaservice.repository;

import java.util.List;
import java.util.Optional;

import ua.rd.pizzaservice.domain.AccumulationCard;
import ua.rd.pizzaservice.domain.Customer;

public interface AccumulationCardRepository {

	AccumulationCard create(AccumulationCard card);

	AccumulationCard read(Integer id);

	List<AccumulationCard> getAllAccumulationCards();

	AccumulationCard update(AccumulationCard card);

	void delete(AccumulationCard card);
	
	Optional<Customer> getOwner(AccumulationCard card);
	
	Boolean hasAccumulationCard(Customer owner);
	
	AccumulationCard getCardByOwner(Customer owner);
}
