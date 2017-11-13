package de.tub.ise.anwsys.repos;

import de.tub.ise.anwsys.models.Pizza;
import org.springframework.data.repository.CrudRepository;

public interface PizzaRepository extends CrudRepository<Pizza, Long> {

	Pizza findByName(String name);
}
