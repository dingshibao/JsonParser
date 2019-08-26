package com.dB.test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dB.myJson.MyJson;

public class Test {
	public static void main(String[] args) {
//		Class clazz = Book.class;
//	
//		Field[] fs = clazz.getDeclaredFields();
//		for (int i = 0; i < fs.length; i++) {
//			Field f = fs[i];
////			System.out.println(f.getType().isAssignableFrom(List.class));
//			System.out.println(List.class.isAssignableFrom(f.getType()));
//		}
		
		
//		BigDecimal d = new BigDecimal(1);
//		System.out.println(d.toString());
		
		
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		Book book = new Book();
		book.id = 3;
		book.setPrice(new BigDecimal(3));
		book.setAuthors(list);
		new MyJson().toJson(book);
		BookList bookList = new BookList();
		bookList.add(book);
		new MyJson().toJson(bookList);
		
	
		
//		int a[] = {1,2};
//		new MyJson().toJson(a);
		
//		HashMap<String, Integer> map = new HashMap<String, Integer>();
//		Class clazz = Integer.class;
//		List<Integer> list = new ArrayList<Integer>();
//		list.add(1);
//		list.add(2);

//		Object o = new Book();
//		System.out.println(o.getClass());
		
//		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
//		map.put(1, 2);
//		map.put(3, 4);
//		new MyJson().toJson(map);
		
		
//		new MyJson().toJson('c');
	}
}
