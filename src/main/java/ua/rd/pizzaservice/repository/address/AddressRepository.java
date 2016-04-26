package ua.rd.pizzaservice.repository.address;

import java.util.List;

import ua.rd.pizzaservice.domain.address.Address;

public interface AddressRepository {

	void create(Address address);

	Address read(Integer id);

	List<Address> getAllAddresses();

	void update(Address address);

	void delete(Address address);

}
