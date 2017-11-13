package de.tub.ise.anwsys.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {
	public OrderItem(Order order, Pizza pizza, long quantity) {
		this.order = order;
		this.pizza = pizza;
		this.quantity = quantity;
	}

	@Id
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;


	@Id
	@ManyToOne
	@JoinColumn(name = "pizza_id")
	private Pizza pizza;


	private long quantity;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Pizza getPizza() {
		return pizza;
	}

	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
}
