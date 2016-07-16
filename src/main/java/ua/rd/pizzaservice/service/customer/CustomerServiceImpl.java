package ua.rd.pizzaservice.service.customer;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.domain.Customer;
import ua.rd.pizzaservice.repository.customer.CustomerRepository;
import ua.rd.pizzaservice.service.address.AddressService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository custRep;
	
	@Autowired
	private AddressService addressService;
	
	@Override
	public Customer create(Customer customer) {
		return custRep.create(customer);
	}

	@Override
	public Customer read(Integer id) {
		return custRep.read(id);
	}

	@Override
	public List<Customer> getAllCustomers() {
		return custRep.getAllCustomers();
	}

	@Override
	public Customer update(Customer customer) {
		return custRep.update(customer);
	}

	@Override
	public void delete(Customer customer) {
		custRep.delete(customer);
	}

	@Override
	public Customer proposeAddress(Address newAddress, Customer customer) {
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
		customer.addAddress(address);
		Customer resultedCustomer = custRep.update(customer);
		return resultedCustomer;
	}

}
