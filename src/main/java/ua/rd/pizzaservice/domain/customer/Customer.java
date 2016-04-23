package ua.rd.pizzaservice.domain.customer;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ua.rd.pizzaservice.domain.address.Address;

@Component
@Scope("prototype")
public class Customer {

	private Integer id;

	private String name;

	private Set<Address> addresses;

	public Customer() {
		addresses = new HashSet<>();
	}

	public Customer(String name) {
		this();
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Boolean addAddress(Address address) {
		return addresses.add(address);
	}
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", addresses=" + addresses + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addresses == null) ? 0 : addresses.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Customer other = (Customer) obj;
		if (addresses == null) {
			if (other.addresses != null) {
				return false;
			}
		} else if (!addresses.equals(other.addresses)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
