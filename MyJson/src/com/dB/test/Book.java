package com.dB.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book {
	int id;
	
	String title;
	
	BigDecimal price;
	
	List<String> authors = new ArrayList<String>();

	String info;
	
	String[] array;
	
	int[] ar = {1,2,3};
	
	Map<String, Integer> map = new HashMap<String, Integer>();
	



	
	
	
	// ÎÞ²ÎÊý
	public Book() {
		// TODO Auto-generated constructor stub
		price = new BigDecimal(0);
		array = new String[3];
		for (int i = 0; i < array.length; i++) {
			array[i] = "i";
		}
		map.put("1", 1);
		map.put("2", 2);
		
	}

	public Book(int id, String title, double price) {
		super();
		this.id = id;
		this.title = title;
		this.price = new BigDecimal(price);
	}

	public void addAuthor(String author) {
		authors.add(author);
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

	public String[] getArray() {
		return array;
	}

	public void setArray(String[] array) {
		this.array = array;
	}

	public int[] getAr() {
		return ar;
	}

	public void setAr(int[] ar) {
		this.ar = ar;
	}

	public Map<String, Integer> getMap() {
		return map;
	}

	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", price=" + price + ", authors=" + authors + ", info=" + info
				+ ", array=" + Arrays.toString(array) + ", ar=" + Arrays.toString(ar) + ", map=" + map + "]";
	}



	
	
}
