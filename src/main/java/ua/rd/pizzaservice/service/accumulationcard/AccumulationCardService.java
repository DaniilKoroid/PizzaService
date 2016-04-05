package ua.rd.pizzaservice.service.accumulationcard;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;

public interface AccumulationCardService {

	AccumulationCard getAccumulationCardByCustomer(Customer customer);

	Boolean hasAccumulationCard(Customer customer);

	Boolean assignNewAccumulationCardToCustomer(Customer customer);

	Boolean activateAccumulationCardForCustomer(Customer customer);

	Boolean deactivateAccumulationCardForCustomer(Customer customer);
}
