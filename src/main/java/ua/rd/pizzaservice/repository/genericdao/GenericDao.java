package ua.rd.pizzaservice.repository.genericdao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, PK extends Serializable> {
	T create(T t);
	T read(PK id);	
	T update(T t);
	List<T> findAll(String queryName);
	void delete(T t);
}
