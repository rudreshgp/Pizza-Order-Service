package de.tub.ise.anwsys.repos.CustomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class CustomRepository<T, K> implements ICustomRepository<T, K> {
	private final EntityManager entityManager;

	@Autowired
	public CustomRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Transactional
	public List<K> getAllIds(String className) {
		Query query = this.entityManager.createQuery(
				"SELECT e.id FROM  " +
						className +
						" e");
		List<K> pizzas = query.getResultList();
		this.entityManager.close();
		return pizzas;
	}

	@Override
	@Transactional
	public List<K> getAllIdsByColumnValue(String className, String whereCondition , String paramName,K paramValue) {
		Query query = this.entityManager.createQuery(
				"SELECT e.id FROM  " +
						className +
						" e where e.pizza_id = :pizza_id" );
		query.setParameter(paramName,paramValue);
		List<K> pizzas = query.getResultList();
		this.entityManager.close();
		return pizzas;
	}

	@Override
	@Transactional
	public boolean checkIfIdExists(String className, K id) {
		Query query = this.entityManager.createQuery(
				"SELECT e.id FROM  " +
						className +
						" e where e.id=" + id.toString());
		return query.getSingleResult() != null;
	}

	public <T> T create(T object) {
		this.entityManager.persist(object);
		return object;
	}
}
