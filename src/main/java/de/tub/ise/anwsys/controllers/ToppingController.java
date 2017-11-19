package de.tub.ise.anwsys.controllers;

import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.CustomExceptions.ItemNotFoundException;
import de.tub.ise.anwsys.CustomStatus.CustomHttpResponse;
import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.models.Topping;
import de.tub.ise.anwsys.repos.CustomRepository.CustomRepository;
import de.tub.ise.anwsys.repos.PizzaRepository;
import de.tub.ise.anwsys.repos.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class ToppingController {

	@Autowired
	private ToppingRepository toppingRepository;

	@Autowired
	private PizzaRepository pizzaRepository;

	@Autowired
	private CustomRepository<Topping, Long> toppingCustomRepository;
	@Autowired
	private CustomRepository<Pizza, Long> pizzaCustomRepository;

	@RequestMapping(path = "/pizza/{pizzaId}/topping", method = RequestMethod.GET)
	public ResponseEntity<List<Long>> getAllToppings(@PathVariable Long pizzaId) {
		if (!pizzaCustomRepository.checkIfIdExists(Pizza.class.getName(), pizzaId)) {
			throw new ItemNotFoundException("Invalid Pizza Id");
		}
		List<Long> toppings = null;
		try {
			toppings = toppingCustomRepository.getAllIdsByColumnValue(Topping.class.getName(), "", "pizza_id", pizzaId);
//			toppingRepository.findByPizzaId(pizzaId)
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new InvalidInputException("Invalid Pizza Id");
		}
		if (toppings.size() > 0)
			return CustomHttpResponse.createResponse(HttpStatus.OK, toppings);
		throw new InvalidInputException("No Toppings found");

	}

	@RequestMapping(value = "/pizza/{pizzaId}/topping/{toppingId}", method = RequestMethod.GET)
	public ResponseEntity<Topping> getTopping(@PathVariable Long pizzaId, @PathVariable Long toppingId) throws
			ItemNotFoundException {
		Topping topping = toppingRepository.findOneByPizzaIdAndId(pizzaId, toppingId);
		if (topping == null) {
			throw new ItemNotFoundException("Topping or Pizza could not be found");
		}
		topping.setPizza(null);
		return CustomHttpResponse.createResponse(HttpStatus.OK, topping);
	}


	@RequestMapping(value = "/pizza/{pizzaId}/topping", method = RequestMethod.POST)
	public ResponseEntity<String> addTopping(@RequestBody Topping topping, @PathVariable Long pizzaId) throws
			InvalidInputException, ItemNotFoundException {
		if (topping == null) {
			throw new InvalidInputException("Invalid input");
		}
		try {
//			topping.setPizza(new Pizza(pizzaId, "", "", 0));
			topping.setPizza_id(pizzaId);
			topping = toppingRepository.save(topping);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("Invalid Pizza Id");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Another Topping with the same name exists in the pizza");
		}
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{toppingId}")
				.buildAndExpand(topping.getId()).toUri();
		return CustomHttpResponse.createResponse(HttpStatus.CREATED, "location", location.toString(), "Created new Topping for pizza");
	}


	@RequestMapping(value = "/pizza/{pizzaId}/topping/{toppingId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateTopping(@RequestBody Topping topping, @PathVariable Long
			pizzaId, @PathVariable Long toppingId) throws ItemNotFoundException, InvalidInputException {
		if (topping == null) {
			throw new InvalidInputException("Invalid input");
		}
		topping.setId(toppingId);
		Topping originalTopping = toppingRepository.findOne(topping.getId());
		if (originalTopping == null) {
			throw new ItemNotFoundException("Topping not found");
		}
		if (originalTopping.getPizza().getId() != pizzaId) {
			throw new ItemNotFoundException("Invalid PizzaId");
		}
		try {
			toppingCustomRepository.detachEntity(Topping.class.getName(), originalTopping);
			topping.setPizza_id(pizzaId);
			toppingRepository.save(topping);

		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("Invalid Pizza Id");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Another Topping with the same name exists in the pizza");
		}
		return CustomHttpResponse.createResponse(HttpStatus.NO_CONTENT, "Update okay");
	}

	@RequestMapping(value = "/pizza/{pizzaId}/topping/{toppingId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTopping(@PathVariable Long pizzaId, @PathVariable Long toppingId) throws
			ItemNotFoundException, InvalidInputException {
		if (toppingRepository.findOneByPizzaIdAndId(pizzaId, toppingId) == null) {
			throw new ItemNotFoundException("Pizza or Topping not found");
		}
		try {
			toppingRepository.delete(toppingId);
			return CustomHttpResponse.createResponse(HttpStatus.NO_CONTENT, "deleted");
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("Pizza or Topping not found");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Cannot delete Toppings as the it contains orders");
		}
	}
}
