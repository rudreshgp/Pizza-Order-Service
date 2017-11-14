package de.tub.ise.anwsys.models;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"PIZZA_ID", "NAME"})})
public class Topping {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@ManyToOne
	private Pizza pizza;

	private double price;

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


	public Pizza getPizza() {
		return pizza;
	}

	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}


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
