package ua.rd.pizzaservice.service.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.repository.address.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addrRep;
	
	@Override
	public Address create(Address address) {
		return addrRep.create(address);
	}

	@Override
	public Address read(Integer id) {
		return addrRep.read(id);
	}

	@Override
	public List<Address> getAllAddresses() {
		return addrRep.getAllAddresses();
	}

	@Override
	public Address update(Address address) {
		return addrRep.update(address);
	}

	@Override
	public void delete(Address address) {
		addrRep.delete(address);
	}

}
