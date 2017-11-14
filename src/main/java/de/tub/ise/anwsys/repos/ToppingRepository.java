package de.tub.ise.anwsys.repos;

import de.tub.ise.anwsys.models.Topping;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ToppingRepository extends CrudRepository<Topping, Long> {
	List<Topping> findByPizzaId(Long pizzaId);
	Topping findOneByPizzaIdAndId(Long pizzaId,Long id);
}
