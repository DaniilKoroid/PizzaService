package ua.rd.pizzaservice.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericSessionDao<T, PK extends Serializable> implements GenericDao<T, PK>{

	private Class<T> entityClass;
	
	@Autowired
	protected SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public GenericSessionDao() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}
	
	@Override
	public T create(T t) {
		getCurrentSession().persist(t);
		return t;
	}

	@Override
	public T read(PK id) {
		return getCurrentSession().get(entityClass, id);
	}

	@Override
	public T update(T t) {
		getCurrentSession().merge(t);
		return t;
	}

	@Override
	public void delete(T t) {
		getCurrentSession().delete(t);
	}
	
	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

}
