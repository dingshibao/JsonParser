package com.dB.myJson;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyJson {
	
	
	public String toJson(Object o) {
		
		cusObjTransform(o);
		return null;

	}
	
		// 类型转换
	private String cusObjTransform(Object o) {
		// TODO Auto-generated method stub
		// 获得类型
		Class clazz = o.getClass();
		Field[] fs = clazz.getDeclaredFields();
		Method[] ms = clazz.getDeclaredMethods();
		
		StringBuilder builder = new StringBuilder("{");
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
		
			String name = f.getName();
			String name2 = name.substring(0, 1).toUpperCase() + name.substring(1);
//			System.out.println(name2);
			try {
				// 获得 getxxx 方法
				Method method = clazz.getDeclaredMethod("get" + name2);
				// 调用方法获得属性值
				Object value = method.invoke(o);
				// 字符串的拼接

				
				// 判断是否是数组
				if(f.getType().isArray()) {
					builder.append(arrayTransform(o, name));
					builder.append(",");
				}
				
				if(value == null) {
					builder.append(String.format("\"%s\":%s,", name, value));
					continue;
				}


				// 判断获得的字段的类型是否是List的子类
				if(Map.class.isAssignableFrom(f.getType())) {
					builder.append(mapTransform(o, name));
					builder.append(",");
					continue;
				}
				
				// 判断获得的字段的类型是否是List的子类
				if(List.class.isAssignableFrom(f.getType())) {
					
					builder.append(listTransform(o, name));
					builder.append(",");
					continue;
				}
				// 自定义Object类
				if(isCustomed(f.getType())) {
					
					builder.append(cusObjTransform(o));
					builder.append(",");
					continue;
				}
				
				builder.append(baseTransform(f, name, value));
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		builder.append("}");
		
		// 最外围是对象开始
		if(builder.charAt(builder.length() - 1) == '}') {
			builder.deleteCharAt(builder.length() - 2);
		} else {
			builder.deleteCharAt(builder.length() - 1);
		}
		System.out.println(builder);
		return builder.toString();
	}
	
	// 数组类型转换
	private Object arrayTransform(Object o, String name) {
			// TODO Auto-generated method stub
			return null;
	}

	// 基本数据类型转换
	private String baseTransform(Field f, String name, Object value) {
		StringBuilder builder = new StringBuilder();
		if(f.getType() == char.class || f.getType() == Character.class)
			builder.append(String.format("\"%s\":'%s',", name, value));

		else if(f.getType() != String.class) {
			builder.append(String.format("\"%s\":%s,", name, value));
		}
		else {
			builder.append(String.format("\"%s\":\"%s\",", name, value));
		}
		return builder.toString();
	}
	
	
	
	// 列表类型转换
	private String listTransform(Object o, String name) {
		// TODO Auto-generated method stub
		
		// 获取列表
		StringBuilder builder = new StringBuilder("{");
		builder.append(String.format("\"%s\":[", name));
		// 列表里可能是列表、图、数组
		
		List<Object> list = (ArrayList<Object>)o;
		for (Object object : list) {
			if(List.class.isAssignableFrom(object.getClass())) {
				
				continue;
			}
			if(Map.class.isAssignableFrom(object.getClass())) {
				
				continue;
			}
			if(isCustomed(object.getClass())) {
				continue;
			}
		}
		
		builder.append("]");
		builder.append("}");
		return builder.toString();
	}

	
	
	
	// 图类型转换
	private String mapTransform(Object o, String name) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder("{");
		return builder.toString();
	}

	
	
	
	// 判断是否是自定义类型
	private boolean isCustomed(Class type) {
		// TODO Auto-generated method stub
		if(type.getName().startsWith("java") || type.isPrimitive())
			return false;
		return true;
	}
}
