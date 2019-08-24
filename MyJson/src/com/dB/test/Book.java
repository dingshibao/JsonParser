package com.dB.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Book {
	int id;
	
	String title;
	
	BigDecimal price;
	
	List<String> authors = new ArrayList<String>();

	String info;
	
	public void addAuthor(String author) {
		authors.add(author);
	}
	
	
	// ÎÞ²ÎÊý
	public Book() {
		// TODO Auto-generated constructor stub
		price = new BigDecimal(0);
	}

	public Book(int id, String title, double price) {
		super();
		this.id = id;
		this.title = title;
		this.price = new BigDecimal(price);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	

	public List<String> getAuthors() {
		return authors;
	}


	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", price=" + price + ", authors=" + authors + ", info=" + info + "]";
	}
	
	
}
