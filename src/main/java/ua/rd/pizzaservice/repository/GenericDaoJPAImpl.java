package ua.rd.pizzaservice.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class GenericDaoJPAImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

	private Class<T> entityClass;

	//TODO: make this field @Autowired
	protected EntityManagerFactory emf;
	
	@SuppressWarnings("unchecked")
	public GenericDaoJPAImpl() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

	@Override
	public T create(T t) {
		EntityManager em = getEntityManager();
		em.persist(t);
		em = null;
		return t;
	}

	@Override
	public T read(PK id) {
		EntityManager em = getEntityManager();
		T find = em.find(entityClass, id);
		em = null;
		return find;
	}

	@Override
	public T update(T t) {
		EntityManager em = getEntityManager();
		T merge = em.merge(t);
		em = null;
		return merge;
	}

	@Override
	public void delete(T t) {
		EntityManager em = getEntityManager();
		t = em.merge(t);
		em.remove(t);
		em = null;
	}
	
	protected final EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

}
