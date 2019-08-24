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
	
		// ����ת��
	private String cusObjTransform(Object o) {
		// TODO Auto-generated method stub
		// �������
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
				// ��� getxxx ����
				Method method = clazz.getDeclaredMethod("get" + name2);
				// ���÷����������ֵ
				Object value = method.invoke(o);
				// �ַ�����ƴ��

				
				// �ж��Ƿ�������
				if(f.getType().isArray()) {
					builder.append(arrayTransform(o, name));
					builder.append(",");
				}
				
				if(value == null) {
					builder.append(String.format("\"%s\":%s,", name, value));
					continue;
				}


				// �жϻ�õ��ֶε������Ƿ���List������
				if(Map.class.isAssignableFrom(f.getType())) {
					builder.append(mapTransform(o, name));
					builder.append(",");
					continue;
				}
				
				// �жϻ�õ��ֶε������Ƿ���List������
				if(List.class.isAssignableFrom(f.getType())) {
					
					builder.append(listTransform(o, name));
					builder.append(",");
					continue;
				}
				// �Զ���Object��
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
		
		// ����Χ�Ƕ���ʼ
		if(builder.charAt(builder.length() - 1) == '}') {
			builder.deleteCharAt(builder.length() - 2);
		} else {
			builder.deleteCharAt(builder.length() - 1);
		}
		System.out.println(builder);
		return builder.toString();
	}
	
	// ��������ת��
	private Object arrayTransform(Object o, String name) {
			// TODO Auto-generated method stub
			return null;
	}

	// ������������ת��
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
	
	
	
	// �б�����ת��
	private String listTransform(Object o, String name) {
		// TODO Auto-generated method stub
		
		// ��ȡ�б�
		StringBuilder builder = new StringBuilder("{");
		builder.append(String.format("\"%s\":[", name));
		// �б���������б�ͼ������
		
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

	
	
	
	// ͼ����ת��
	private String mapTransform(Object o, String name) {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder("{");
		return builder.toString();
	}

	
	
	
	// �ж��Ƿ����Զ�������
	private boolean isCustomed(Class type) {
		// TODO Auto-generated method stub
		if(type.getName().startsWith("java") || type.isPrimitive())
			return false;
		return true;
	}
}
