package de.tub.ise.anwsys.controllers;

import de.tub.ise.anwsys.CustomExceptions.ItemNotFoundException;
import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.repos.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@RestController
public class PizzaController {

	@Autowired
	private PizzaRepository pizzaRepository;

	@RequestMapping("/pizza")
	public List<Pizza> getAllPizzas() {
		ArrayList<Pizza> pizzas = new ArrayList<>();
		pizzaRepository.findAll().forEach(pizzas::add);
		return pizzas;
	}

	@RequestMapping("/pizza/{pizzaId}")
	public Pizza getPizza(@PathVariable Integer pizzaId) throws ItemNotFoundException {
		Pizza pizza = pizzaRepository.findOne(pizzaId);
		if (pizza == null) {
			throw new ItemNotFoundException("Pizza could not be found");
		}
		return pizza;
	}


	@RequestMapping(value = "/pizza", method = RequestMethod.POST)
	public String addPizza(@RequestBody Pizza pizza) throws InvalidInputException {
		if (pizza == null) {
			throw new InvalidInputException("Invalid input");
		}
		pizzaRepository.save(pizza);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{pizzaId}")
				.buildAndExpand(pizza.getId()).toUri();
		return location.getRawPath();
	}

	@RequestMapping(value = "/pizza/{pizzaId}", method = RequestMethod.PUT)
	public String updatePizza(@RequestBody Pizza pizza, @PathVariable Integer pizzaId) throws ItemNotFoundException, ChangeSetPersister.NotFoundException {
		if (pizza == null || pizzaId == 0 || pizzaId!=pizza.getId()) {
			throw new InvalidInputException("Invalid pizza supplied");
		}
		if (pizzaRepository.findOne(pizzaId) == null) {
			throw new ItemNotFoundException("Pizza not found");
		}
		pizzaRepository.save(pizza);
//		URI location = ServletUriComponentsBuilder
//				.fromCurrentRequest().path("/{id}")
//				.buildAndExpand(pizza.getId()).toUri();
		return "Update okay";
	}


	@RequestMapping(value = "/pizza/{pizzaId}", method = RequestMethod.DELETE)
	public String getPizza(@PathVariable int pizzaId) throws ItemNotFoundException {
		if (pizzaRepository.findOne(pizzaId) == null) {
			throw new ItemNotFoundException("Pizza not found");
		}
		pizzaRepository.delete(pizzaId);
		return "Deleted";
	}


}
