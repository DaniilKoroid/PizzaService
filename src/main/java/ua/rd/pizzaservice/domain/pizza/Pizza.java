package ua.rd.pizzaservice.domain.pizza;

public class Pizza {

	public enum PizzaType {
		MEAT,
		VEGETERIAN,
		SEA, 
		;
	}

	private Integer id;
	private String name;
	private Double price;
	private PizzaType type;

	public Pizza() {
	}

	public Pizza(Integer id, String name, Double price, PizzaType type) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.type = type;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public PizzaType getType() {
		return type;
	}

	public void setType(PizzaType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Pizza [id=" + id + ", name=" + name + ", price=" 
				+ price + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Pizza other = (Pizza) obj;
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
		if (price == null) {
			if (other.price != null) {
				return false;				
			}
		} else if (!price.equals(other.price)) {
			return false;			
		}
		if (type != other.type) {
			return false;			
		}
		return true;
	}

}
