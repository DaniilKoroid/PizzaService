package ua.rd.pizzaservice.domain.customer;

public class Customer {

	private static int idCounter = 0;
	
	private Integer id;
	private String name;
	
	public Customer() {
	}

	public Customer(String name) {
		id = ++idCounter;
		this.name = name;
	}

	public Customer(Integer id, String name) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + "]";
	}	
	
}
