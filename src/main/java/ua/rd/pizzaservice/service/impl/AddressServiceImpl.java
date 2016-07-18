package ua.rd.pizzaservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.infrastructure.exceptions.NoSuchEntityException;
import ua.rd.pizzaservice.repository.AddressRepository;
import ua.rd.pizzaservice.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addrRep;
	
	@Override
	public Address create(Address address) {
		checkAddressExistance(address);
		return addrRep.create(address);
	}

	@Override
	public Address read(Integer id) {
		Address address = addrRep.read(id);
		checkAddressExistance(id, address);
		return address;
	}

	@Override
	public List<Address> getAllAddresses() {
		return addrRep.getAllAddresses();
	}

	@Override
	public Address update(Address address) {
		checkAddressExistance(address);
		return addrRep.update(address);
	}

	@Override
	public void delete(Address address) {
		checkAddressExistance(address);
		addrRep.delete(address);
	}
	
	private void checkAddressExistance(Integer id, Address address) {
		if (address == null) {
			throw new NoSuchEntityException("Address with id: " + id + " does not exist.", id.longValue());
		}
	}
	
	private void checkAddressExistance(Address address) {
		if (address == null) {
			throw new IllegalArgumentException("Can't do operations with inexistant address."); 
		}
	}
}
