package ua.rd.pizzaservice.repository.genericdao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import ua.rd.pizzaservice.repository.genericdao.GenericDao;

@Transactional
public abstract class GenericDaoJPAImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	protected Class<T> entityClass;

	@PersistenceContext
	protected EntityManager em;
	
	@SuppressWarnings("unchecked")
	public GenericDaoJPAImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

	@Override
	public T create(T t) {
		em.persist(t);
		return t;
	}

	@Override
	@Transactional(readOnly = true)
	public T read(PK id) {
		T foundObject = em.find(entityClass, id);
		return foundObject;
	}

	@Override
	public T update(T t) {
		T mergedEntity = em.merge(t);
		return mergedEntity;
	}

	@Override
	public void delete(T t) {
		t = em.merge(t);
		em.remove(t);
	}

	@Override
	public List<T> findAll(String queryName) {
		return em.createNamedQuery(queryName, entityClass).getResultList();
	}

}
