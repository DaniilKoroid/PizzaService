package ua.rd.pizzaservice.repository.address;

import java.util.List;

import javax.persistence.Persistence;

import org.springframework.stereotype.Repository;

import ua.rd.pizzaservice.domain.address.Address;
import ua.rd.pizzaservice.repository.GenericDaoJPAImpl;

@Repository
public class GenericDaoJPAAddressRepository extends GenericDaoJPAImpl<Address, Integer> implements AddressRepository {

	public GenericDaoJPAAddressRepository() {
		emf = Persistence.createEntityManagerFactory("jpa_mysql");
	}
	
	public GenericDaoJPAAddressRepository(String persistenceUnitName) {
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
	}
	
	@Override
	public List<Address> getAllAddresses() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

}
