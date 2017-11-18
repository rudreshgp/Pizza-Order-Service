package de.tub.ise.anwsys.repos;

import de.tub.ise.anwsys.models.Pizza;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface PizzaRepository extends CrudRepository<Pizza, Long> {

	Pizza findByName(String name);

	List<Pizza> findAllByIdOrName(Long id, String name);

	List<Pizza> findByIdIn(Collection<Long> ids);

//	List<Long> get();

}
