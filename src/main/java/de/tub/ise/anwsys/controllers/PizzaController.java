package de.tub.ise.anwsys.controllers;

import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.CustomExceptions.ItemNotFoundException;
import de.tub.ise.anwsys.CustomStatus.CustomHttpResponse;
import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.repos.CustomRepository.ICustomRepository;
import de.tub.ise.anwsys.repos.PizzaRepository;
import org.h2.util.StringUtils;
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
public class PizzaController {

	@Autowired
	private PizzaRepository pizzaRepository;

	@Autowired
	private ICustomRepository<Pizza,Long> customPizzaRepository;

	@RequestMapping("/pizza")
	public ResponseEntity<List<Long>> getAllPizzas() throws ItemNotFoundException {
		List<Long> pizzas = customPizzaRepository.getAllIds(Pizza.class.getName());
		if (pizzas != null && pizzas.size() == 0) {
			throw new ItemNotFoundException("No pizzas exist");
		}
		return CustomHttpResponse.createResponse(HttpStatus.OK, pizzas);
	}

	@RequestMapping(value = "/pizza/{pizzaId}", method = RequestMethod.GET)
	public Pizza getPizza(@PathVariable Long pizzaId) throws ItemNotFoundException {
		Pizza pizza = pizzaRepository.findOne(pizzaId);
		if (pizza == null) {
			throw new ItemNotFoundException("Pizza could not be found");
		}
		return pizza;
	}


	@RequestMapping(value = "/pizza", method = RequestMethod.POST)
	public ResponseEntity<String> addPizza(@RequestBody Pizza pizza) throws InvalidInputException {

		if (pizza == null) {
			throw new InvalidInputException("Invalid input");
		}
		if (StringUtils.isNullOrEmpty(pizza.getSize())) {
			pizza.setSize(Pizza.Size.Standard.name());
		} else {
			Pizza.Size size = Pizza.Size.parse(pizza.getSize());
			if (size == null) {
				throw new InvalidInputException("The size is invalid. either enter Large or Standard");
			}
		}
		try {
			pizza.setId(0);
			pizza = pizzaRepository.save(pizza);
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Another Pizza with the same name exists");
		}
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{pizzaId}")
				.buildAndExpand(pizza.getId()).toUri();
		return CustomHttpResponse.createResponse(HttpStatus.CREATED, "location", location.toString(), "Created new pizza.");
	}

	@RequestMapping(value = "/pizza/{pizzaId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updatePizza(@RequestBody Pizza pizza, @PathVariable Long pizzaId) throws ItemNotFoundException {

		pizza.setId(pizzaId);
		if (pizza == null) {
			throw new InvalidInputException("Invalid pizza supplied");
		}
		if (StringUtils.isNullOrEmpty(pizza.getSize())) {
			pizza.setSize(Pizza.Size.Standard.name());
		} else {
			Pizza.Size size = Pizza.Size.parse(pizza.getSize());
			if (size == null) {
				throw new InvalidInputException("The size is invalid. either enter Large or Standard");
			}
		}
		if (pizzaRepository.findOne(pizzaId) == null) {
			throw new ItemNotFoundException("Pizza not found");
		}
		try {
			pizza = pizzaRepository.save(pizza);
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Another Pizza with the same name exists in the szstem");
		}
		return CustomHttpResponse.createResponse(HttpStatus.NO_CONTENT, "Update okay");
	}


	@RequestMapping(value = "/pizza/{pizzaId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deletePizza(@PathVariable Long pizzaId) throws ItemNotFoundException, InvalidInputException {
		if (pizzaRepository.findOne(pizzaId) == null) {
			throw new ItemNotFoundException("Pizza not found exception");
		}
		try {
			pizzaRepository.delete(pizzaId);
			return CustomHttpResponse.createResponse(HttpStatus.NO_CONTENT, "Deleted");
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("The order with the specified id might not be available");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Cannot delete Pizza as it contains toppings");
		}
	}


}
