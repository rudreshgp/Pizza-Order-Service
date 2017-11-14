package de.tub.ise.anwsys.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "order_table")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long Id;

	private double totalPrice;

	private String recipient;


	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
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

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	@OneToMany(mappedBy = "order")
	private Set<OrderItem> orderItems;


	public Order(long id) {
		Id = id;
	}

	public Order() {
	}
}
