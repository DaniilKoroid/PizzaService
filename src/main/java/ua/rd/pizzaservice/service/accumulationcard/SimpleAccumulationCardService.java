package ua.rd.pizzaservice.service.accumulationcard;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.accumulationcard.AccumulationCard;
import ua.rd.pizzaservice.domain.customer.Customer;
import ua.rd.pizzaservice.repository.accumulationcard.AccumulationCardRepository;

@Service
public class SimpleAccumulationCardService implements AccumulationCardService {

	@Autowired
	AccumulationCardRepository cardRep;
	
    public SimpleAccumulationCardService() {
    }

    @Override
    public AccumulationCard getAccumulationCardByCustomer(Customer customer) {
        if (!hasAccumulationCard(customer)) {
            throw new NoSuchElementException("Given customer with id "
                    + customer.getId() + " has " + "no accumulation card.");
        }
        AccumulationCard card = cardRep.getCardByOwner(customer);
        return card;
    }

    @Override
    public Boolean hasAccumulationCard(Customer customer) {
    	return cardRep.hasAccumulationCard(customer);
    }

    @Override
    public Boolean assignNewAccumulationCardToCustomer(Customer customer) {
        if (hasAccumulationCard(customer)) {
            return false;
        }

        AccumulationCard card = new AccumulationCard();
        card.setOwner(customer);
        cardRep.create(card);
        return true;
    }

    @Override
    public Boolean activateAccumulationCardForCustomer(Customer customer) {
        if (!hasAccumulationCard(customer)) {
        	System.out.println("has no card");
            return false;
        }
        AccumulationCard card = getAccumulationCardByCustomer(customer);
        if (card.getIsActivated()) {
            return false;
        }
        boolean setActivated = true;
        card.setIsActivated(setActivated);
        cardRep.update(card);
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
        cardRep.update(card);
        return true;
    }

	@Override
	public Customer getOwner(AccumulationCard card) {
		return card.getOwner();
	}

}
