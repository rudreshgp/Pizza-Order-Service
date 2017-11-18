package de.tub.ise.anwsys.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"PIZZA_ID", "NAME"})})
public class Topping implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY,optional=false)
	@JoinColumn(name = "pizza_id",insertable = false,updatable = false)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Pizza pizza;

	public long getPizza_id() {
		return pizza_id;
	}

	public void setPizza_id(long pizza_id) {
		this.pizza_id = pizza_id;
	}

	@Column(name="pizza_id")
	private long pizza_id;

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
		this.pizza_id = pizzaId;
	}
}
