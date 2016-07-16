package ua.rd.pizzaservice.service.accumulationcard;

import ua.rd.pizzaservice.domain.AccumulationCard;
import ua.rd.pizzaservice.domain.Customer;

public interface AccumulationCardService {

	AccumulationCard getAccumulationCardByCustomer(Customer customer);

	Customer getOwner(AccumulationCard card);
	
	Boolean hasAccumulationCard(Customer customer);

	Boolean assignNewAccumulationCardToCustomer(Customer customer);

	Boolean activateAccumulationCardForCustomer(Customer customer);

	Boolean deactivateAccumulationCardForCustomer(Customer customer);
	
	AccumulationCard read(Integer id);
}
