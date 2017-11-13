package de.tub.ise.anwsys.repos;

import de.tub.ise.anwsys.models.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
