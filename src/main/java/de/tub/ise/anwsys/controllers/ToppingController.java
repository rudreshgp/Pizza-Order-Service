package de.tub.ise.anwsys.controllers;

import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.CustomExceptions.ItemNotFoundException;
import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.models.Topping;
import de.tub.ise.anwsys.repos.PizzaRepository;
import de.tub.ise.anwsys.repos.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ToppingController {

	@Autowired
	private ToppingRepository toppingRepository;

	@Autowired
	private PizzaRepository pizzaRepository;

	@RequestMapping("/pizza/{pizzaId}/topping")
	public List<Topping> getAllToppings(@PathVariable Long pizzaId) {
		ArrayList<Topping> pizzas = new ArrayList<>();
		try {
			toppingRepository.findByPizzaId(pizzaId).forEach(pizzas::add);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new InvalidInputException("Invalid Pizza Id");
		}
		if (pizzas.size() > 0)
			return pizzas;
		throw new ItemNotFoundException("No Toppings found");
	}

	@RequestMapping("/pizza/{pizzaId}/topping/{toppingId}")
	public Topping getTopping(@PathVariable Long pizzaId, @PathVariable Long toppingId) throws ItemNotFoundException {
		Topping topping = toppingRepository.findOne(toppingId);
		if (topping == null) {
			throw new ItemNotFoundException("Pizza could not be found");
		}
		return topping;
	}


	@RequestMapping(value = "/pizza/{pizzaId}/topping", method = RequestMethod.POST)
	public String addTopping(@RequestBody Topping topping, @PathVariable Long pizzaId) throws InvalidInputException {
		if (topping == null) {
			throw new InvalidInputException("Invalid input");
		}
		if (pizzaRepository.findByName(topping.getName()) != null) {
			throw new DuplicateKeyException("Pizza with the same name exists");
		}
		try {
			topping.setPizza(new Pizza(pizzaId, "", "", 0));
			toppingRepository.save(topping);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new InvalidInputException("Invalid Pizza Id");
		}
//		URI location = ServletUriComponentsBuilder
//				.fromCurrentRequest().path("/{pizzaId}")
//				.buildAndExpand(topping.getId()).toUri();
//		return location.getRawPath();
		return "";
	}


	@RequestMapping(value = "/pizza/{pizzaId}/topping", method = RequestMethod.PUT)
	public String updateTopping(@RequestBody Topping topping, @PathVariable Long pizzaId) throws ItemNotFoundException {
		if (topping == null) {
			throw new InvalidInputException("Invalid input");
		}
		Topping originalTopping = toppingRepository.findOne(topping.getId());
		if (originalTopping == null) {
			throw new ItemNotFoundException("Topping not found");
		}
		if (originalTopping.getPizza().getId() != pizzaId) {
			throw new ItemNotFoundException("Invalid PizzaId");
		}
		toppingRepository.save(topping);
//		topping.setPizza(pizza);
//		toppingRepository.save(topping);
////		URI location = ServletUriComponentsBuilder
////				.fromCurrentRequest().path("/{id}")
////				.buildAndExpand(pizza.getId()).toUri();
		return "Update okay";
	}

	@RequestMapping(value = "/pizza/{pizzaId}/topping/{toppingId}", method = RequestMethod.DELETE)
	public String deleteTopping(@PathVariable Long pizzaId, @PathVariable Long toppingId) throws ItemNotFoundException {
		Topping topping = toppingRepository.findOne(toppingId);
		if (topping == null) {
			throw new ItemNotFoundException("Topping not found");
		}
		if (topping.getPizza().getId() != pizzaId) {
			throw new ItemNotFoundException("Pizza not found");
		}
		try {
			toppingRepository.delete(toppingId);
			return "Deleted";
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Cannot delete Toppings as the it contains orders");
		}
	}
}
