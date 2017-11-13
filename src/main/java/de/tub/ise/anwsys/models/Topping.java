package de.tub.ise.anwsys.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Topping {
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Id
	private long id;
	private String name;
	private double price;

	public Pizza getPizza() {
		return pizza;
	}

	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}

	@ManyToOne
	private Pizza pizza;

	public Topping() {
	}

	public Topping(long id, String name, double price, long pizzaId) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.pizza = new Pizza(pizzaId, "", "", 0);
	}
}
