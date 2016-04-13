package ua.rd.pizzaservice.service.accumulationcard;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;

@Service
public class SimpleAccumulationCardService implements AccumulationCardService {

    private Map<Customer, AccumulationCard> ownedCards = new HashMap<>();

    public SimpleAccumulationCardService() {
    }

    @Override
    public AccumulationCard getAccumulationCardByCustomer(Customer customer) {
        if (!hasAccumulationCard(customer)) {
            throw new NoSuchElementException("Given customer with id "
                    + customer.getId() + " has " + "no accumulation card.");
        }
        AccumulationCard card = ownedCards.get(customer);
        return card;
    }

    @Override
    public Boolean hasAccumulationCard(Customer customer) {
        return ownedCards.containsKey(customer);
    }

    @Override
    public Boolean assignNewAccumulationCardToCustomer(Customer customer) {
        if (ownedCards.containsKey(customer)) {
            return false;
        }

        AccumulationCard card = new AccumulationCard();
        ownedCards.put(customer, card);
        return true;
    }

    @Override
    public Boolean activateAccumulationCardForCustomer(Customer customer) {
        if (!hasAccumulationCard(customer)) {
            return false;
        }
        AccumulationCard card = getAccumulationCardByCustomer(customer);
        if (card.getIsActivated()) {
            return false;
        }
        boolean setActivated = true;
        card.setIsActivated(setActivated);
        return true;
    }

    @Override
    public Boolean deactivateAccumulationCardForCustomer(Customer customer) {
        if (!hasAccumulationCard(customer)) {
            return false;
        }
        AccumulationCard card = getAccumulationCardByCustomer(customer);
        if (!card.getIsActivated()) {
            return false;
        }
        boolean setActivated = false;
        card.setIsActivated(setActivated);
        return true;
    }

}
