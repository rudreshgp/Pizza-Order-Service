package de.tub.ise.anwsys.repos;

import de.tub.ise.anwsys.models.PizzaOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<PizzaOrder, Long> {
}
