package de.tub.ise.anwsys.controllers;


import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.CustomExceptions.ItemNotFoundException;
import de.tub.ise.anwsys.CustomStatus.CustomHttpResponse;
import de.tub.ise.anwsys.models.Order;
import de.tub.ise.anwsys.repos.OrderRepository;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Long>> getAllOrders() throws ItemNotFoundException {
		ArrayList<Long> orders = new ArrayList<>();
		orderRepository.findAll().forEach(x -> orders.add(x.getId()));
		if (orders.size() == 0)
			throw new ItemNotFoundException("No orders found");
		return CustomHttpResponse.createResponse(HttpStatus.OK, orders);
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Order> getOrder(@PathVariable long orderId) throws ItemNotFoundException {
		Order order = orderRepository.findOne(orderId);
		if (order == null) {
			throw new ItemNotFoundException("Order not found");
		}
		return CustomHttpResponse.createResponse(HttpStatus.OK, order);
	}

	@RequestMapping(path = "/order", method = RequestMethod.POST)
	public ResponseEntity<String> createOrder(@RequestBody Order order) throws InvalidInputException {
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
		return CustomHttpResponse.createResponse(HttpStatus.CREATED, "location", location.getRawPath(), "Created new order successfully");
	}

	@RequestMapping(path = "/order/{orderId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateOrder(@RequestBody Order order, @PathVariable long orderId) throws InvalidInputException {
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
