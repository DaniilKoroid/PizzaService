package ua.rd.pizzaservice.repository.address;

import java.util.List;

import ua.rd.pizzaservice.domain.Address;

public interface AddressRepository {

	Address create(Address address);

	Address read(Integer id);

	List<Address> getAllAddresses();

	Address update(Address address);

	void delete(Address address);

}
