package ua.rd.pizzaservice.repository.accumulationcard;

import java.util.List;
import java.util.Optional;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;

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
