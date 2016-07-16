package ua.rd.pizzaservice.web.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import ua.rd.pizzaservice.domain.Address;
import ua.rd.pizzaservice.service.AddressService;

public class AddressConverter implements Converter<String, Address> {

	@Autowired
	private AddressService addressService;
	
	@Override
	public Address convert(String source) {
		Integer addressId = Integer.valueOf(source);
		Address address = addressService.read(addressId);
		return address;
	}

}
