package de.tub.ise.anwsys.repos.CustomRepository;

import de.tub.ise.anwsys.models.Pizza;

import java.util.HashSet;
import java.util.List;

public interface  ICustomRepository<T,K> {
	List<K> getAllIds(String className);
	List<K> getAllIdsByColumnValue(String className, String whereCondition , String paramName,K paramValue);
	boolean checkIfIdExists(String className, K id);
	<T> T create(T obj);
	void detachEntity(String className, T object);
}
