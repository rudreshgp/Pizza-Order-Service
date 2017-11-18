package de.tub.ise.anwsys.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Pizza implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	//	@Column(unique = true)
	private String name;

	private String size;

	private double price;

	@OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private Set<OrderItem> orderItems;


	@OneToMany(mappedBy = "pizza")
	@JsonIgnore
	private Set<Topping> toppings;

	public Set<Topping> getToppings() {
		return toppings;
	}

	public void setToppings(Set<Topping> toppings) {
		this.toppings = toppings;
	}

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
		return this.size;
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

	@JsonIgnore
	public double getTotalPrice() {
		double totalPrice = this.price;
		if (this.toppings != null && this.toppings.size() > 0) {
			for (Topping topping : this.toppings) {
				totalPrice += topping.getPrice();
			}
		}
		return totalPrice;
	}

	public Pizza() {
	}

	public Pizza(long id) {
		this.id = id;
	}

	public Pizza(long id, String name, String size, double price) {
		super();
		this.id = id;
		this.name = name;
		this.setSize(size);
		this.price = price;
	}

	public enum Size {
		Standard(1), Large(2);
		private int value;

		Size(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static Size parse(int value) {
			Size size = null;
			for (Size item : Size.values()) {
				if (item.getValue() == value) {
					size = item;
					break;
				}
			}
			return size;
		}

		public static Size parse(String sizeName) {
			Size size = null;
			for (Size item : Size.values()) {
				if (item.name().toUpperCase().equals(sizeName.toUpperCase())) {
					size = item;
					break;
				}
			}
			return size;
		}
	}
}
