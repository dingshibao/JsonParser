package com.dB.test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dB.myJson.MyJson;
import com.dB.myJson.mismatchException;

public class Test {
	public static void main(String[] args) {

		
		
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		Book book = new Book();
		book.id = 3;
		book.setPrice(new BigDecimal(4));
		book.setAuthors(list);
		new MyJson().toJson(book);
		BookList bookList = new BookList();
		bookList.add(book);
		System.out.println(new MyJson().toJson(bookList));
		String json = new MyJson().toJson(book);
		System.out.println(json);
		try {
			System.out.println(new MyJson().fromJson(json, book.getClass()));
		} catch (mismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		

		
	}
}
