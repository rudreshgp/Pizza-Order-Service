package de.tub.ise.anwsys.controllers;


import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.CustomExceptions.ItemNotFoundException;
import de.tub.ise.anwsys.CustomStatus.CustomHttpResponse;
import de.tub.ise.anwsys.models.OrderItem;
import de.tub.ise.anwsys.models.Pizza;
import de.tub.ise.anwsys.models.PizzaOrder;
import de.tub.ise.anwsys.repos.CustomRepository.ICustomRepository;
import de.tub.ise.anwsys.repos.OrderRepository;
import de.tub.ise.anwsys.repos.PizzaRepository;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PizzaRepository pizzaRepository;

	@Autowired
	ICustomRepository<PizzaOrder, Long> customOrderRepository;

	@Autowired
	ICustomRepository<OrderItem, Long> customOrderItemRepository;

	@RequestMapping(path = "/order", method = RequestMethod.GET)
	public ResponseEntity<List<Long>> getAllOrders() throws ItemNotFoundException {
		List<Long> orders = customOrderRepository.getAllIds(PizzaOrder.class.getName());
		if (orders.size() == 0)
			throw new ItemNotFoundException("No orders found");
		return CustomHttpResponse.createResponse(HttpStatus.OK, orders);
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<PizzaOrder> getOrder(@PathVariable long orderId) throws ItemNotFoundException {
		PizzaOrder order = orderRepository.findOne(orderId);
		if (order == null) {
			throw new ItemNotFoundException("Order not found");
		}
		return CustomHttpResponse.createResponse(HttpStatus.OK, order);
	}

	@RequestMapping(path = "/order", method = RequestMethod.POST)
	public ResponseEntity<String> createOrder(@RequestBody PizzaOrder order) throws InvalidInputException {
		if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
			throw new InvalidInputException("At-least one order item is required");
		}
		if (StringUtils.isNullOrEmpty(order.getRecipient())) {
			throw new InvalidInputException("Recipient is mandatory");
		}
		try {
			HashSet<Long> pizzaIds = new HashSet<>();
			for (OrderItem item : order.getOrderItems()) {
				pizzaIds.add(item.getPizzaId());
			}
			List<Pizza> pizzas = pizzaRepository.findByIdIn(pizzaIds);
			double totlaPrice = 0.0;
			if (pizzas != null && pizzas.size() > 0) {

				for (OrderItem item : order.getOrderItems()) {
					Optional<Pizza> pizza = pizzas.stream().filter(x -> x.getId() == item.getPizzaId()).findFirst();
					if (pizza.isPresent()) {
						totlaPrice += (pizza.get().getTotalPrice()) * (item.getQuantity());
					} else {
						throw new InvalidInputException("Invalid Pizza Id");
					}
					item.setOrder(order);
				}

				order.setTotalPrice(totlaPrice);
				orderRepository.save(order);
			}
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new InvalidInputException("The pizza with the specified id might not be available");
		}
		catch (JpaSystemException ex){
			throw ex;
		}
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{orderId}")
				.buildAndExpand(order.getId()).toUri();
		return CustomHttpResponse.createResponse(HttpStatus.CREATED, "location", location.getRawPath(), "Created new order successfully");
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateOrder(@RequestBody PizzaOrder order, @PathVariable long orderId) throws InvalidInputException {
		if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
			throw new InvalidInputException("At-least one order item is required");
		}
		order.setId(orderId);
		if (StringUtils.isNullOrEmpty(order.getRecipient())) {
			throw new InvalidInputException("Recipient is mandatory");
		}
		if (orderRepository.findOne(orderId) == null) {
			throw new ItemNotFoundException("Order not found");
		}
		try {
			orderRepository.save(order);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new InvalidInputException("The pizza or the order with the specified id might not be available");
		}
		return CustomHttpResponse.createResponse(HttpStatus.NO_CONTENT, "Update okay");
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delteOrder(@PathVariable long orderId) throws InvalidInputException, ItemNotFoundException {
		if (orderRepository.findOne(orderId) == null) {
			throw new ItemNotFoundException("Order not found");
		}
		try {
			orderRepository.delete(orderId);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("The order with the specified id might not be available");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Cannot delete Pizza as it contains toppings");
		}
		return CustomHttpResponse.createResponse(HttpStatus.NO_CONTENT, "deleted");
	}

}
