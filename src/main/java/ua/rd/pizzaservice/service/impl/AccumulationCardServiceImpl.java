package ua.rd.pizzaservice.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.AccumulationCard;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.repository.AccumulationCardRepository;
import ua.rd.pizzaservice.service.AccumulationCardService;

@Service
public class AccumulationCardServiceImpl implements AccumulationCardService {

	@Autowired
	AccumulationCardRepository cardRep;
	
    public AccumulationCardServiceImpl() {
    }
    
    public AccumulationCardServiceImpl(AccumulationCardRepository cardRepository) {
    	cardRep = cardRepository;
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

	@Override
	public AccumulationCard read(Integer id) {
		AccumulationCard card = cardRep.read(id);
		if (card == null) {
			throw new NoSuchElementException("Given accumulation card with id "
                    + id + " does not exist.");
		}
		return card;
	}

}