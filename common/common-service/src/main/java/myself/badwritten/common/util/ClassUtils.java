package myself.badwritten.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassUtils {

	/**
	 * 将指定path添加到java.lang.ClassLoader 的java path中
	 * 
	 * @param path
	 */
	public static void addJavaLibraryPath(String path) {

		String[] paths = (String[]) getFieldValue(ClassLoader.class, "usr_paths", null);
		for (int i = 0; i < paths.length; i++) {
			if (path.equals(paths[i])) {
				return;
			}
		}
		String[] tmp = new String[paths.length + 1];
		System.arraycopy(paths, 0, tmp, 0, paths.length);
		tmp[paths.length] = path;

		setFieldValue(ClassLoader.class, "usr_paths", null, tmp);
	}

	/**
	 * 获取指定方法
	 * 
	 * @param classOfType
	 * @param methodName
	 * @return
	 */
	public static Method getMethod(Class<?> classOfType, String methodName) {
		return ReflectUtils.getReflect(classOfType).getMethod(methodName);
	}

	/**
	 * 获取get方法
	 * 
	 * @param classOfType
	 * @param fieldName
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Method getReadMethod(Class<?> classOfType, String fieldName) {
		return ReflectUtils.getReflect(classOfType).getReadMethod(fieldName);
	}

	/**
	 * 调用get方法
	 * 
	 * @param classOfType
	 * @param fieldName
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Object invokeReadMethod(Class<?> classOfType, String fieldName, Object obj) throws IllegalArgumentException {
		Method method = getReadMethod(classOfType, fieldName);
		if (method != null) {
			return invoke(method, obj);
		} else {
			throw new IllegalArgumentException("Can not find read method from " + classOfType.getName() + " on " + fieldName);
		}
	}

	/**
	 * 获取set方法
	 * 
	 * @param classOfType
	 * @param fieldName
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Method getWriteMethod(Class<?> classOfType, String fieldName) throws IllegalArgumentException {
		return ReflectUtils.getReflect(classOfType).getWriteMethod(fieldName);
	}

	/**
	 * 调用set方法
	 * 
	 * @param classOfType
	 * @param fieldName
	 * @param obj
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public static void invokeWriteMethod(Class<?> classOfType, String fieldName, Object obj, Object... value) throws IllegalArgumentException {
		Method method = getWriteMethod(classOfType, fieldName);
		if (method != null) {

			if (value != null && value.length == 1 && value[0] != null) {
				Class<?> parameterType = method.getParameterTypes()[0];
				value[0] = ConvertUtils.convert(value[0], parameterType);
			}

			invoke(method, obj, value);
		} else {
			throw new IllegalArgumentException("Can not find write method from " + classOfType.getName() + " on " + fieldName);
		}
	}

	/**
	 * 调用方法
	 * 
	 * @param method
	 * @param obj
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public static Object invoke(Method method, Object obj, Object... value) throws IllegalArgumentException {
		try {
			return method.invoke(obj, value);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * 调用方法
	 * 
	 * @param
	 * @param obj
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public static Object invoke(Class<?> classOfType, String methodName, Object obj, Object... value) throws IllegalArgumentException {
		try {
			Method method = ReflectUtils.getReflect(classOfType).getMethod(methodName);

			if (method == null) {
				throw new IllegalArgumentException("Can not find method " + methodName + " on " + classOfType.getName());
			}

			if (!Modifier.isPublic(method.getModifiers())) {
				method.setAccessible(true);
			}

			try {
				return method.invoke(obj, value);

			} finally {
				if (!Modifier.isPublic(method.getModifiers())) {
					method.setAccessible(false);
				}
			}

		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * 强制设置类或对象属性的值
	 * 
	 * @param classOfType
	 * @param fieldName
	 * @param obj
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public static void setFieldValue(Class<?> classOfType, String fieldName, Object obj, Object value) throws IllegalArgumentException {
		try {
			Field field = ReflectUtils.getReflect(classOfType).getField(fieldName);

			if (field == null) {
				throw new IllegalArgumentException("Can not find field " + fieldName + " on " + classOfType.getName());
			}

			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not set value on " + classOfType.getName() + "." + fieldName);
		}
	}

	/**
	 * 获取类或对象属性的值
	 * 
	 * @param classOfType
	 * @param fieldName
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Object getFieldValue(Class<?> classOfType, String fieldName, Object obj) throws IllegalArgumentException {
		try {

			Field field = ReflectUtils.getReflect(classOfType).getField(fieldName);

			if (field == null) {
				throw new IllegalArgumentException("Can not find field " + fieldName + " on " + classOfType.getName());
			}

			field.setAccessible(true);
			Object value = field.get(obj);
			field.setAccessible(false);
			return value;
		} catch (Exception e) {
			throw new IllegalArgumentException("Can not read value on " + classOfType.getName() + "." + fieldName);
		}
	}

	/**
	 * 新实例
	 * 
	 * @param classOfType
	 * @return
	 */
	public static <T> T newInstance(Class<T> classOfType) {
		T obj;
		try {
			obj = classOfType.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("denies creation of new instances of the class " + classOfType.getName());
		}
		return obj;
	}

	/**
	 * 新实例
	 * 
	 * @param className
	 * @return
	 */
	public static Object newInstance(String className) {
		Object obj;
		try {
			Class<?> classOfType = Class.forName(className);
			obj = classOfType.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("denies creation of new instances of the class " + className, e);
		}
		return obj;
	}

	/**
	 * 克隆
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T object) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(object);
			ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
			ObjectInputStream oi = new ObjectInputStream(bi);
			return (T) oi.readObject();
		} catch (Exception e) {
			throw new IllegalArgumentException("Something is wrong with a class used by serialization");
		}
	}

	/**
	 * 在Map或对象中取字段
	 * 
	 * @param name
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getValue(String name, Object obj) {
		if (obj == null) {
			return null;
		}

		if (obj instanceof Map) {
			return ((Map) obj).get(name);
		} else {
			try {
				return ClassUtils.invokeReadMethod(obj.getClass(), name, obj);
			} catch (Exception e) {
				return null;
			}
		}

	}

	/**
	 * 获取所有属性
	 * 
	 * @param classOfType
	 * @return
	 */
	public static Field[] getAllFields(Class<?> classOfType) {
		Field[] fields = classOfType.getDeclaredFields();
		Class<?> superClass = classOfType.getSuperclass();
		if (superClass != null) {
			List<Field> list = new ArrayList<>();
			for (Field field : fields) {
				if (!field.getName().equals("serialVersionUID")) {
					list.add(field);
				}
			}
			Field[] list2 = getAllFields(superClass);
			for (Field field : list2) {
				list.add(field);
			}
			fields = list.toArray(new Field[list.size()]);
		}
		return fields;
	}

	/**
	 * 获取所有方法
	 * 
	 * @param classOfType
	 * @return
	 */
	public static Method[] getAllMethods(Class<?> classOfType) {
		Method[] methods = classOfType.getDeclaredMethods();
		Class<?> superClass = classOfType.getSuperclass();
		if (superClass != null) {
			List<Method> list = new ArrayList<>();
			for (Method method : methods) {
				list.add(method);
			}
			Method[] list2 = getAllMethods(superClass);
			for (Method method : list2) {
				list.add(method);
			}
			methods = list.toArray(new Method[list.size()]);
		}
		return methods;
	}

	/**
	 * 根据表达式获取值
	 * 
	 * @param exp
	 * @param obj
	 * @return
	 */
	public static Object invokeExpression(String exp, Object obj) {

		if (obj == null || StringUtils.isEmpty(exp)) {
			return null;
		}

		String key;
		String subKey = null;
		Object value = null;

		if (exp.contains(".")) {
			String[] pair = StringUtils.splitFirst(exp, ".");
			key = pair[0];
			subKey = pair[1];
		} else {
			key = exp;
		}

		if (key.matches(".+\\[\\d+\\]")) {

			String name = exp.substring(0, exp.indexOf("["));
			Integer no = Integer.valueOf(exp.substring(exp.indexOf("[") + 1, exp.indexOf("]")));
			Object o = ClassUtils.getValue(name, obj);
			if (o != null && o.getClass().isArray() && ((Object[]) o).length > no) {
				value = ((Object[]) o)[no];
			}

		} else {
			value = ClassUtils.getValue(key, obj);
		}

		if (value != null && subKey != null) {
			value = invokeExpression(subKey, value);
		}

		return value;

	}

}
