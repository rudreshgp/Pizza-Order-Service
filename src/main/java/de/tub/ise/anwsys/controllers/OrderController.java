package de.tub.ise.anwsys.controllers;


import de.tub.ise.anwsys.CustomExceptions.InvalidInputException;
import de.tub.ise.anwsys.models.Order;
import de.tub.ise.anwsys.repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public class OrderController {

	@Autowired
	private OrderRepository orderRepository;

	@RequestMapping(path = "/order/{orderId}")
	public Order getOrder(@PathVariable long orderId) throws InvalidInputException {
		Order order = orderRepository.findOne(orderId);
		if (order == null) {
			throw new InvalidInputException("Order not found");
		}
		return order;
	}

}
