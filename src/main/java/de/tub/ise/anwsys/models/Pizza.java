package de.tub.ise.anwsys.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;

@Entity
public class Pizza {

	@Id
	private int id;
	private String name;
	private String size;
	private double price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
}
