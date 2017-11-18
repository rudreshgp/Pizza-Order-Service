package de.tub.ise.anwsys.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {
	public OrderItem(PizzaOrder order, Pizza pizza, long quantity) {
		super();
		this.pizzaorder = order;
		this.pizza = pizza;
		this.quantity = quantity;
	}

	public OrderItem(long order_id, long pizza_id, long quantity) {
		super();
//		this.pizzaOrder_id = order_id;
		this.pizzaorder = new PizzaOrder(order_id);
//		this.pizzaId = pizza_id;
		this.pizza = new Pizza(pizza_id);
		this.quantity = quantity;
	}

	public OrderItem() {
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "pizza_id")
	@Id
	private Pizza pizza;

//	@Column(name = "pizza_id")
//	private long pizzaId;

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "pizza_order_id")
	private PizzaOrder pizzaorder;

//	@Column(name = "pizza_order_id")
//	private long pizzaOrder_id;


	private long quantity;

	public PizzaOrder getOrder() {
		return pizzaorder;
	}

	public void setOrder(PizzaOrder order) {
		this.pizzaorder = order;
//		if(order!=null)
//			this.pizzaOrder_id = order.getId();
	}

//	public long getOrderId() {
//		return pizzaOrder_id;
//	}

	public void setOrderId(long order_id) {
//		this.pizzaOrder_id = order_id;
		this.pizzaorder = new PizzaOrder(order_id);
	}

	public long getPizzaId() {
		if (this.pizza != null)
			return this.pizza.getId();
		return 0;
	}

	public void setPizzaId(long pizza_Id) {
//		this.pizzaId = pizza_Id;
		this.pizza = new Pizza(pizza_Id);
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
