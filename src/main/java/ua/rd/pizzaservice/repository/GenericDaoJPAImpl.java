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
		try {
			em.getTransaction().begin();
			em.persist(t);
			em.getTransaction().commit();
		} catch (Throwable th) {
			System.out.println(th.getLocalizedMessage());
		} finally {
			closeEntityManager(em);
		}
		return t;
	}

	@Override
	public T read(PK id) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			T find = em.find(entityClass, id);
			em.getTransaction().commit();
			return find;
		} catch (Throwable th) {
			System.out.println(th.getLocalizedMessage());
		} finally {
			closeEntityManager(em);
		}
		throw new RuntimeException("Failed to read.");
	}

	@Override
	public T update(T t) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			T merge = em.merge(t);
			em.getTransaction().commit();
			return merge;
		} catch (Throwable th) {
			System.out.println(th.getLocalizedMessage());
		} finally {
			closeEntityManager(em);
		}
		throw new RuntimeException("Failed to update.");
	}

	@Override
	public void delete(T t) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			t = em.merge(t);
			em.remove(t);
			em.getTransaction().commit();
		} catch (Throwable th) {
			System.out.println(th.getLocalizedMessage());
		} finally {
			closeEntityManager(em);
		}
	}
	
	protected final EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
	
	protected final void closeEntityManager(EntityManager em) {
		if (em != null) {
			em.close();
		}
	}

}
