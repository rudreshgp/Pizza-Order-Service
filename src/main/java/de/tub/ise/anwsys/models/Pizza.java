package de.tub.ise.anwsys.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Pizza {

	@Id
	private long id;
	private String name;
	private String size;
	private double price;
	@OneToMany(mappedBy = "pizza")
	private Set<OrderItem> orderItems;

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Pizza() {
	}

	public Pizza(long id, String name, String size, double price) {
		super();
		this.id = id;
		this.name = name;
		this.size = size;
		this.price = price;
	}
}
