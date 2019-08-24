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
}
