package de.tub.ise.anwsys.controllers;

import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.CustomExceptions.ItemNotFoundException;
import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.models.Topping;
import de.tub.ise.anwsys.repos.PizzaRepository;
import de.tub.ise.anwsys.repos.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

	@RequestMapping(path = "/pizza/{pizzaId}/topping", method = RequestMethod.GET)
	public List<Topping> getAllToppings(@PathVariable Long pizzaId) {
		ArrayList<Topping> pizzas = new ArrayList<>();
		if (pizzaRepository.findOne(pizzaId) == null) {
			throw new InvalidInputException("Invalid Pizza Id");
		}
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
		Topping topping = toppingRepository.findOneByPizzaIdAndId(pizzaId, toppingId);
		if (topping == null) {
			throw new ItemNotFoundException("Topping or Pizza could not be found");
		}
		return topping;
	}


	@RequestMapping(value = "/pizza/{pizzaId}/topping", method = RequestMethod.POST)
	public String addTopping(@RequestBody Topping topping, @PathVariable Long pizzaId) throws InvalidInputException, ItemNotFoundException {
		if (topping == null) {
			throw new InvalidInputException("Invalid input");
		}
		try {
			topping.setPizza(new Pizza(pizzaId, "", "", 0));
			toppingRepository.save(topping);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("Invalid Pizza Id");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Another Topping with the same name exists in the pizza");
		}
//		URI location = ServletUriComponentsBuilder
//				.fromCurrentRequest().path("/{pizzaId}")
//				.buildAndExpand(topping.getId()).toUri();
//		return location.getRawPath();
		return "";
	}


	@RequestMapping(value = "/pizza/{pizzaId}/topping/{toppingId}", method = RequestMethod.PUT)
	public String updateTopping(@RequestBody Topping topping, @PathVariable Long pizzaId, @PathVariable Long toppingId) throws ItemNotFoundException, InvalidInputException {
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
			toppingRepository.save(topping);

		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("Invalid Pizza Id");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Another Topping with the same name exists in the pizza");
		}
		return "Update okay";
	}

	@RequestMapping(value = "/pizza/{pizzaId}/topping/{toppingId}", method = RequestMethod.DELETE)
	public String deleteTopping(@PathVariable Long pizzaId, @PathVariable Long toppingId) throws ItemNotFoundException, InvalidInputException {
		try {
			if (toppingRepository.findOneByPizzaIdAndId(pizzaId, toppingId) == null) {
				throw new ItemNotFoundException("Pizza or Topping not found");
			}
			toppingRepository.delete(toppingId);
			return "Deleted";
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("Pizza or Topping not found");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Cannot delete Toppings as the it contains orders");
		}
	}
}
