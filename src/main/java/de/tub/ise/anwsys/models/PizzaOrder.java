package de.tub.ise.anwsys.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pizza_order")
public class PizzaOrder  implements Serializable {

//	private static final long serialVersionUID = 8089601593251025235L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private double totalPrice;

	private String recipient;

	@OneToMany(mappedBy = "pizzaorder", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JsonIgnore
	private List<OrderItem> orderItems;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}




	public PizzaOrder(long id) {
		super();
		id = id;
	}

	public PizzaOrder() {
	}

	public PizzaOrder(long id, String recipient, List<OrderItem> orderItems) {
		super();
		this.id = id;
		this.recipient = recipient;
		this.orderItems = orderItems;
	}
}
