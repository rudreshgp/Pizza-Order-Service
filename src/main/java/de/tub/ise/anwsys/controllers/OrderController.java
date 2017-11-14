package de.tub.ise.anwsys.controllers;


import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.CustomExceptions.ItemNotFoundException;
import de.tub.ise.anwsys.models.Order;
import de.tub.ise.anwsys.repos.OrderRepository;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

	@Autowired
	private OrderRepository orderRepository;

	@RequestMapping(path = "/order", method = RequestMethod.GET)
	public List<Long> getAllOrders() throws ItemNotFoundException {
		ArrayList<Long> orders = new ArrayList<>();
		orderRepository.findAll().forEach(x -> orders.add(x.getId()));
		if (orders.size() == 0)
			throw new ItemNotFoundException("No orders found");
		return orders;
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.GET)
	public Order getOrder(@PathVariable long orderId) throws ItemNotFoundException {
		Order order = orderRepository.findOne(orderId);
		if (order == null) {
			throw new ItemNotFoundException("Order not found");
		}
		return order;
	}

	@RequestMapping(path = "/order", method = RequestMethod.POST)
	public String createOrder(@RequestBody Order order) throws InvalidInputException {
		if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
			throw new InvalidInputException("At-least one order item is required");
		}
		if (StringUtils.isNullOrEmpty(order.getRecipient())) {
			throw new InvalidInputException("Recipient is mandatory");
		}
		try {
			orderRepository.save(order);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new InvalidInputException("The pizza with the specified id might not be available");
		}
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{orderId}")
				.buildAndExpand(order.getId()).toUri();
		return location.getRawPath();
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.PUT)
	public String updateOrder(@RequestBody Order order, @PathVariable long orderId) throws InvalidInputException {
		if (order == null || order.getOrderItems() == null || order.getOrderItems().size() == 0) {
			throw new InvalidInputException("At-least one order item is required");
		}
		order.setId(orderId);
		if (StringUtils.isNullOrEmpty(order.getRecipient())) {
			throw new InvalidInputException("Recipient is mandatory");
		}
		try {
			orderRepository.save(order);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new InvalidInputException("The pizza or the order with the specified id might not be available");
		}
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{orderId}")
				.buildAndExpand(order.getId()).toUri();
		return location.getRawPath();
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.DELETE)
	public String delteOrder(@PathVariable long orderId) throws InvalidInputException, ItemNotFoundException {
		try {
			orderRepository.delete(orderId);
		} catch (JpaObjectRetrievalFailureException ex) {
			throw new ItemNotFoundException("The order with the specified id might not be available");
		} catch (DataIntegrityViolationException ex) {
			throw new InvalidInputException("Cannot delete Pizza as it contains toppings");
		}
//		URI location = ServletUriComponentsBuilder
//				.fromCurrentRequest().path("/{orderId}")
//				.buildAndExpand(order.getId()).toUri();
		return "Deleted successfully";
	}

}
