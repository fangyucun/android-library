/*
 *  Copyright (C) 2012-2015 Jason Fang ( ifangyucun@gmail.com )
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hellofyc.applib.util;

import android.support.annotation.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Reflect
 * Create on 2014年11月23日 下午2:42:00
 *
 * @author Jason Fang
 */
public class ReflectUtils {
	static final boolean DEBUG = false;
	
	public static void setBoolean(Object receiver, String fieldName, boolean trueOrFalse) {
		if (receiver == null || fieldName == null) return;
		
		try {
			Field field = getDeclaredField(receiver.getClass(), fieldName);
			field.setBoolean(receiver, trueOrFalse);
		} catch (Exception e) {
			if (DEBUG) Flog.e(e);
		}
	}
	
	/**
	 * 获取本类所有的方法
	 */
	public static Field getDeclaredField(Class<?> cls, String fieldName) {
		if (cls == null) return null;
		
		try {
			Field field = cls.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			if (DEBUG) Flog.e(e);
			return null;
		}
	}
	
	/**
	 * 获取类所有的公共方法包含父类
	 */
	public static Field getField(Class<?> cls, String fieldName) {
		if (cls == null) return null;
		
		try {
			return cls.getField(fieldName);
		} catch (NoSuchFieldException e) {
			if (DEBUG) Flog.e(e);
		}
        return null;
    }
	
	public static Object invokeConstructor(Class<?> cls) throws Exception {
		return invokeConstructor(cls, 0);
	}
	
	public static Object invokeConstructor(Class<?> cls, int index, Object... constructorArgs) {
		Constructor<?>[] cs = cls.getDeclaredConstructors();
		try {
			return cs[index].newInstance(constructorArgs);
		} catch (Exception e) {
			if (DEBUG) Flog.e(e);
		}
        return null;
    }
	
	public static Object invokeMethod(@NonNull Object receiver, String methodName, Object... methodArgs) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Class<?>[] argsClass = null;
		if (methodArgs != null && methodArgs.length != 0) {
			int length = methodArgs.length;
			argsClass = new Class[length];
			for (int i=0; i<length; i++) {
				argsClass[i] = getBaseTypeClass(methodArgs[i].getClass());
			}
		}

        Method method = receiver.getClass().getMethod(methodName, argsClass);
        return method.invoke(receiver, methodArgs);
    }
	
	public static Object invokeStaticMethod(Object receiver, String methodName, Object... methodArgs) {
		if (receiver == null) return null;
		
		return invokeStaticMethod(receiver.getClass(), methodName, methodArgs);
	}
	
	public static Object invokeStaticMethod(String className, String methodName, Object... methodArgs) {
		try {
			Class<?> cls = Class.forName(className);
			return invokeStaticMethod(cls, methodName, methodArgs);
		} catch (ClassNotFoundException e) {
			Flog.e(e);
		}
        return null;
    }

	public static Object invokeStaticMethod(Class<?> cls, String methodName, Object... methodArgs) {
		Class<?>[] argsClass = null;
		
		if (methodArgs != null && methodArgs.length != 0) {
			int length = methodArgs.length;
			argsClass = new Class[length];
			for (int i=0; i<length; i++) {
				argsClass[i] = getBaseTypeClass(methodArgs[i].getClass());
			}
		}
	
		try {
			Method method = cls.getMethod(methodName, argsClass);
			return method.invoke(null, methodArgs);
		} catch (Exception e) {
			if (DEBUG) Flog.e(e);
			return null;
		}
	}
	
	public static Class<?> getBaseTypeClass(@NonNull Class<?> cls) {
		if ("Boolean".equals(cls.getSimpleName())) {
			cls = Boolean.TYPE;
		} else if ("Integer".equals(cls.getSimpleName())) {
			cls = Integer.TYPE;
		} else if ("Float".equals(cls.getSimpleName())) {
			cls = Float.TYPE;
		} else if ("Double".equals(cls.getSimpleName())) {
			cls = Double.TYPE;
		} else if ("Long".equals(cls.getSimpleName())) {
			cls = Long.TYPE;
		} else if ("Short".equals(cls.getSimpleName())) {
			cls = Short.TYPE;
		} else if ("Character".equals(cls.getSimpleName())) {
			cls = Character.TYPE;
		} else if ("Byte".equals(cls.getSimpleName())) {
			cls = Byte.TYPE;
		}
		return cls;
	}

	public static boolean isByte(Class<?> cls) {
		return Byte.class.isAssignableFrom(cls);
	}
	
	public static boolean isChar(Class<?> cls) {
		return Character.class.isAssignableFrom(cls);
	}
	
	public static boolean isBoolean(Class<?> cls) {
		return Boolean.class.isAssignableFrom(cls);
	}
	
	public static boolean isInteger(Class<?> cls) {
		return Integer.class.isAssignableFrom(cls);
	}
	
	public static boolean isLong(Class<?> cls) {
		return Long.class.isAssignableFrom(cls);
	}
	
	public static boolean isShort(Class<?> cls) {
		return Short.class.isAssignableFrom(cls);
	}
	
	public static boolean isFloat(Class<?> cls) {
		return Float.class.isAssignableFrom(cls);
	}
	
	public static boolean isDouble(Class<?> cls) {
		return Double.class.isAssignableFrom(cls);
	}
	
	private ReflectUtils(){/*Do not new me*/}
}
