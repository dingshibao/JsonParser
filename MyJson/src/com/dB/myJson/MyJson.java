package com.dB.myJson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class MyJson {
	

	// 对象序列化
	public String toJson(Object o) {
		StringBuilder builder = new StringBuilder();
		String str;
		if(o == null) {
			return null;
		}
		if(o.getClass().isArray() || List.class.isAssignableFrom(o.getClass())) {
			
			str = listTransform(o, null);
		}
		else if(Map.class.isAssignableFrom(o.getClass()))
			str = mapTransform(o, null);
		else if(isCustomed(o.getClass()))
			str = cusObjTransform(o);
		else {
			if(o.getClass() == char.class || o.getClass() == Character.class)
				str = String.format("'%s'", o);

			else if(o.getClass()!= String.class) {
				str = String.format("%s", o);
			}
			else {
				str = String.format("\"%s\"", o);
			}
		}
		builder.append(str);
		return builder.toString();

	}
	
		// 类型转换
	private String cusObjTransform(Object o) {
		// TODO Auto-generated method stub
		// 获得类型
		Class clazz = o.getClass();
		Field[] fs = clazz.getDeclaredFields();
		Method[] ms = clazz.getMethods();
		
		
		
		StringBuilder builder = new StringBuilder("{");
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
		
			String name = f.getName();
			String name2 = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			try {
				// 获得 getxxx 方法
				Method method = clazz.getDeclaredMethod(name2);
				// 调用方法获得属性值
				Object value = method.invoke(o);
				// 字符串的拼接
				
				if(value == null) {
					builder.append(String.format("\"%s\":%s,", name, value));
					continue;
				}
				
				// 获得对应字段的对象
				f.setAccessible(true);
				Object obj = f.get(o);

				// 判断获得的字段的类型是否是Map的子类
				if(Map.class.isAssignableFrom(f.getType())) {
					builder.append(mapTransform(obj, name));
					builder.append(",");
					continue;
				}
				
				// 判断获得的字段的类型是否是List的子类 或 数组
				if(List.class.isAssignableFrom(f.getType()) || f.getType().isArray()) {
					
					builder.append(listTransform(obj, name));
					builder.append(",");
					continue;
				}
				// 自定义Object类
				if(isCustomed(f.getType())) {
					
					builder.append(cusObjTransform(obj));
					builder.append(",");
					continue;
				}
				
				builder.append(baseTransform(obj, name, value));
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
		return builder.toString();
	}

	// 基本数据类型转换
	private String baseTransform(Object obj, String name, Object value) {
		StringBuilder builder = new StringBuilder();
		if(obj.getClass() == char.class || obj.getClass() == Character.class)
			builder.append(String.format("\"%s\":'%s',", name, value));

		else if(obj.getClass()!= String.class) {
			builder.append(String.format("\"%s\":%s,", name, value));
		}
		else {
			builder.append(String.format("\"%s\":\"%s\",", name, value));
		}
		return builder.toString();
	}
	
	
	
	// 列表类型、数组转换
	private String listTransform(Object obj, String name) {
		// TODO Auto-generated method stub
		
		// 获取列表
		StringBuilder builder = new StringBuilder();
		if(name == null)
			builder.append("[");
		else
			builder.append(String.format("\"%s\":[", name));
		// 列表里可能是列表、图、数组
		Object[] o;
		if(!obj.getClass().isArray()) {
			List<Object> list = (List)obj;
			o = list.toArray();
		} else {
			
			// 获取数组的值并转化为Object数组
			int len = Array.getLength(obj);
			o = new Object[len];
			for (int i = 0; i < len; i++) {
				o[i] = Array.get(obj, i);
			}
		}
		for (int i = 0; i < o.length; i++) {
			if(o[i] == null) {
				continue;
			} else if(List.class.isAssignableFrom(o[i].getClass()) || Map.class.isAssignableFrom(o[i].getClass())
					|| o[i].getClass().getClassLoader() != null || o[i].getClass().isArray()) {
				
				// 自定义类型 列表 图 数组 不能直接赋值
				builder.append(cusObjTransform(o[i]));
				builder.append(",");
				continue;
			} else {
				if(o[i].getClass() == char.class || o[i].getClass() == Character.class)
					builder.append(String.format("'%s',", o[i]));

				else if(o[i].getClass()!= String.class) {
					builder.append(String.format("%s,", o[i]));
				}
				else {
					builder.append(String.format("\"%s\",", o[i]));
				}
			}
			
			
		}
		
		builder.append("]");
		if(builder.charAt(builder.length() - 2) == ',')
			builder.deleteCharAt(builder.length() - 2);
		return builder.toString();
	}

	
	
	
	// 图类型转换
	private String mapTransform(Object obj, String name) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		if(name == null) 
			builder.append("{");
		else
			builder.append(String.format("\"%s\":{", name));
		Map<Object, Object> map = (HashMap<Object, Object>)obj;
		// 获得键集合
		Set<Object> keySet = map.keySet();
		// 获得迭代器
		Iterator<Object> it = keySet.iterator();
		while(it.hasNext()) {
			Object k = it.next();
			Object v = map.get(k);
			builder.append(String.format("\"%s\":", k));
			if(List.class.isAssignableFrom(v.getClass()) || Map.class.isAssignableFrom(v.getClass())
					|| v.getClass().getClassLoader() != null || v.getClass().isArray()) {
				builder.append(cusObjTransform(v));
				builder.append(",");
				continue;
			}
			if(v.getClass() == char.class || v.getClass() == Character.class)
				builder.append(String.format("'%s',", v));

			else if(obj.getClass()!= String.class) {
				builder.append(String.format("%s,", v));
			}
			else {
				builder.append(String.format("\"%s\",", v));
			}
		}
		builder.append(String.format("}"));
		if(builder.length() > 2)
			builder.deleteCharAt(builder.length() - 2);
		return builder.toString();
	}

	
		
	
	// 判断是否是自定义类型
	private boolean isCustomed(Class type) {
		// TODO Auto-generated method stub
		if(type.getName().startsWith("java") || type.isPrimitive())
			return false;
		return true;
	}


	
	// JSON格式字符串反序列化成对应对象
	// 判断类型
	
	
	public Object fromJson(String json, Class clazz) throws mismatchException {
		Object obj = null;
		if(json == null)
			return null;
		// 判断所给字符是否与参数所给类型相同
		if((! List.class.isAssignableFrom(clazz) && ! clazz.isArray() && json.startsWith("["))
				||(! Map.class.isAssignableFrom(clazz) && ! isCustomed(clazz) && json.startsWith("{"))) {
			throw new mismatchException();
		}
		
		// 判断格式是否正确
		if((json.startsWith("[") && ! json.endsWith("]"))
				|| (json.startsWith("{") && ! json.endsWith("}")))
			throw new mismatchException();
		if(json.startsWith("[")) {
				obj = listDeserialization(json.substring(1, json.length() - 1), clazz, Object.class);
		} else if(json.startsWith("{")) {
				if(Map.class.isAssignableFrom(clazz))
					obj = mapDeserialization(json.substring(1, json.length() - 1), clazz, Object.class, Object.class);
				else
					obj = cusObjDeserialization(json.substring(1, json.length() - 1), clazz);
		} else
			obj = baseDeserialization(json, clazz);
		
		return obj;
	}
	
	
	private Object cusObjDeserialization(String json, Class clazz) throws mismatchException {
		// TODO Auto-generated method stub
		Object o = null;
		if(json == null || json.equals("null")) {
			return o;
		}
		try {
			// 创建对象
			o = clazz.getConstructor().newInstance();
			Field[] fs = clazz.getDeclaredFields();
			
			// 存放[、{的下标和值
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				Object obj = null;
				// 判断是否是对象 复杂类型
				if(List.class.isAssignableFrom(f.getType())
						|| Map.class.isAssignableFrom(f.getType())
						|| f.getType().isArray()
						|| isCustomed(f.getType())) {
						
					Stack<Integer> index = new Stack<Integer>();
					Stack<Character> special = new Stack<Character>();
					int j = 0;
					while(j < json.length()) {
						if(json.charAt(0) == ':')
							throw new mismatchException();
						if(json.charAt(j) == '{' || json.charAt(j) == '[') {
							special.push(json.charAt(j));
							index.push(j);
						}
						if(json.charAt(j) == '}' || json.charAt(j) == ']') {
							if(('{' != special.peek() && json.charAt(j)  == '}') 
									|| ('[' != special.peek() && json.charAt(j) == ']') 
									|| special.size() == 0)  
								throw new mismatchException();
							special.pop();
							if(special.size() > 0) {
								index.pop();
							}
							if(special.size() == 0) {
								int k = index.pop();
								String str = json.substring(k + 1, j);
								if(List.class.isAssignableFrom(f.getType())) {
									
									System.out.println(f.getName());
									Type t = f.getGenericType();
									ParameterizedType pt = (ParameterizedType)t;
									Type[] type = pt.getActualTypeArguments();
									System.out.println(type[0]);
									obj = listDeserialization(str, f.getType(), Class.forName(type[0].getTypeName()));
									
								} else if(Map.class.isAssignableFrom(f.getType())) {
									Type t = f.getGenericType();
									ParameterizedType pt = (ParameterizedType)t;
									Type[] type = pt.getActualTypeArguments();
									obj = mapDeserialization(str, f.getType(), Class.forName(type[0].getTypeName()), Class.forName(type[1].getTypeName()));
								} else if(f.getType().isArray()) {
									obj = listDeserialization(str, f.getType(), f.getType().getComponentType());
								} else
									obj = cusObjDeserialization(json.substring(k, j + 1), f.getType());
								if(j != json.length() - 1) {
									json = json.substring(j + 2);
									break;
								}
							}
						}
						j++;
					}
					if(special.size() != 0) {
						throw new mismatchException();
					}
				} else {		// 基本类型 简单类型
					if(json.charAt(0) == ':' || json.charAt(json.length() - 1) == ',')
						throw new mismatchException();
					int j = 0;
					Stack<Integer> stack = new Stack<Integer>();
					
					while(j < json.length() && json.charAt(j) != ','){
						if(json.charAt(j) == ':')
							stack.push(j);
						j++;
					}
					if(stack.size() != 1)
						throw new mismatchException();
					int k = stack.pop();
					obj = baseDeserialization(json.substring(k + 1, j), f.getType());
					if(j < json.length() - 1)
						json = json.substring(j + 1);
				}
					
				
				String name = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
				Method m = clazz.getDeclaredMethod(name, f.getType());
				if(obj != null) {
					if(f.getType().isArray()) {
						m = clazz.getDeclaredMethod(name, f.getType());
						int len = Array.getLength(obj);
						Object oc = Array.newInstance(f.getType().getComponentType(), len);
							for (int j = 0; j < len; j++) {
								Array.set(oc, j, Array.get(obj, j));
							}
						
						if( ! f.getType().getComponentType().isPrimitive())
							m.invoke(o, oc);
						else
							m.invoke(o, obj);
					} else
						m.invoke(o, obj);
				}
			}
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return o;
	}

	private Object baseDeserialization(String json, Class clazz) throws mismatchException {
		// TODO Auto-generated method stub
		Object obj = null;
		
		
		if(json == null || json.equals("null"))
			return null;
		if(json.contains("{") || json.contains("}") || json.contains("[") || json.contains("]")
				|| json.contains(",") || json.contains(":")) 
			throw new mismatchException();
		if(clazz == char.class || clazz == Character.class || clazz == String.class) {
			json = json.substring(1, json.length() - 1);
		}
		if(clazz == String.class) {
			return json;
		}
		// 变成包装类
		if(clazz.isPrimitive()) {
			if(clazz == boolean.class) {
				if( ! json.equals("true") && ! json.equals("false"))
					throw new mismatchException();
				clazz = Boolean.class;
			} else if(clazz == char.class) {
				char[] c = json.toCharArray();
				if(c.length != 1)
					throw new mismatchException();
				return c[0];
			}
			else if(clazz == byte.class)
				clazz = Byte.class;
			else if(clazz == short.class)
				clazz = short.class;
			else if(clazz == int.class)
				clazz = Integer.class;
			else if(clazz == long.class)
				clazz = Long.class;
			else if(clazz == float.class)
				clazz = Float.class;
			else if(clazz == double.class)
				clazz = Double.class;
		}
		if(clazz == Character.class) {
			char[] c = json.toCharArray();
			if(c.length != 1)
				throw new mismatchException();
			return c[0];
		}
		try {
				obj = clazz.getConstructor(String.class).newInstance(json);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}


	private Object mapDeserialization(String json, Class clazz, Class k_type, Class v_type) throws mismatchException {
		// TODO Auto-generated method stub
		Object obj = null;
		if(json == null || json.equals("null")) {
			return null;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		if(List.class.isAssignableFrom(v_type)
				|| Map.class.isAssignableFrom(v_type)
				|| v_type.isArray()
				|| isCustomed(v_type)) {
			
			
		} else {
			String[] kv = json.split(",");
			for (int i = 0; i < kv.length; i++) {
				String k = kv[i].substring(1, kv[i].indexOf(':') - 1);
				String v = kv[i].substring(kv[i].indexOf(':') + 1);
				map.put(baseDeserialization(k, k_type), baseDeserialization(v, v_type));
			}
		}
		return obj;
	}

	private Object listDeserialization(String json, Class clazz, Class type) throws mismatchException {
		// TODO Auto-generated method stub
		if(json == null || json.equals("null")) {
			return null;
		}
		Object obj = null;
		List<Object> list = new ArrayList<Object>();
		if(List.class.isAssignableFrom(type)
				|| Map.class.isAssignableFrom(type)
				|| type.isArray()
				|| isCustomed(type)) {
			
			
		} else {
			String[] values = json.split(",");
			for (String string : values) {
				Object o = baseDeserialization(string, type);
				list.add(o);
			}
		}
		
		if(clazz.isArray()) {
			obj = list.toArray();
		} else {
			obj = list.subList(0, list.size());
		}
		return obj;
	}
	
}
