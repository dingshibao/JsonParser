package com.dB.test;

import java.util.ArrayList;


public class BookList {
	ArrayList<Book> list;
	public BookList() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<Book>();
	}
	
	public void add(Book book) {
		list.add(book);
	}

	public ArrayList<Book> getList() {
		return list;
	}

	public void setList(ArrayList<Book> list) {
		this.list = list;
	}
	
	
}
