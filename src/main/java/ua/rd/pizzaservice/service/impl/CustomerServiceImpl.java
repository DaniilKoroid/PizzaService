package ua.rd.pizzaservice.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.infrastructure.exceptions.NoSuchEntityException;
import ua.rd.pizzaservice.repository.CustomerRepository;
import ua.rd.pizzaservice.service.AddressService;
import ua.rd.pizzaservice.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository custRep;
	
	@Autowired
	private AddressService addressService;
	
	@Override
	public Customer create(Customer customer) {
		checkCustomerExistance(customer);
		return custRep.create(customer);
	}

	@Override
	public Customer read(Integer id) {
		Customer customer = custRep.read(id);
		checkCustomerExistance(id, customer);
		return customer;
	}

	@Override
	public List<Customer> getAllCustomers() {
		return custRep.getAllCustomers();
	}

	@Override
	public Customer update(Customer customer) {
		checkCustomerExistance(customer);
		return custRep.update(customer);
	}

	@Override
	public void delete(Customer customer) {
		checkCustomerExistance(customer);
		custRep.delete(customer);
	}

	@Override
	public Customer proposeAddress(Address newAddress, Customer customer) {
		if(newAddress == null) {
			return customer;
		}
		checkCustomerExistance(customer);
		Customer resultedCustomer = customer;
		if (resultedCustomer.getId() == null) {
			resultedCustomer = custRep.create(customer);
		}
		if (newAddress.getId() == null) {
			newAddress = addressService.create(newAddress);
			resultedCustomer = addAddressToCustomer(resultedCustomer, newAddress);
		} else if (!hasAddress(resultedCustomer, newAddress)) {
			resultedCustomer = addAddressToCustomer(resultedCustomer, newAddress);
		}
		return resultedCustomer;
	}
	
	private boolean hasAddress(Customer customer, Address address) {
		checkCustomerExistance(customer);
		if (address == null) {
			return false;
		}
		boolean result = false;
		Set<Address> addresses = customer.getAddresses();
		for (Address customerAddr : addresses) {
			if (customerAddr.equals(address)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private Customer addAddressToCustomer(Customer customer, Address address) {
		checkCustomerExistance(customer);
		if(address == null) {
			return customer;
		}
		customer.addAddress(address);
		Customer resultedCustomer = custRep.update(customer);
		return resultedCustomer;
	}
	
	private void checkCustomerExistance(Integer id, Customer customer) {
		if (customer == null) {
			throw new NoSuchEntityException("Customer with id: " + id + " does not exist.", id.longValue());
		}
	}
	
	private void checkCustomerExistance(Customer customer) {
		if (customer == null) {
			throw new IllegalArgumentException("Can't do operations with inexistant customer."); 
		}
	}
}
