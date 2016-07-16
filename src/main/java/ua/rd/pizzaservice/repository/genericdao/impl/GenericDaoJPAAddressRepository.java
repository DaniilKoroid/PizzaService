package ua.rd.pizzaservice.repository.genericdao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.repository.AddressRepository;

@Repository
public class GenericDaoJPAAddressRepository extends GenericDaoJPAImpl<Address, Integer> implements AddressRepository {

	@Override
	public List<Address> getAllAddresses() {
		return findAll("findAllAddresses");
	}

}
