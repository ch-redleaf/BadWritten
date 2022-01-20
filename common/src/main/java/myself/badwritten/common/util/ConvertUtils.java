package myself.badwritten.common.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConvertUtils {

	private static final Log logger = LogFactory.getLog(ConvertUtils.class);

	static {
		BeanUtilsBean.getInstance().getConvertUtils().register(true, true, 0);
		/*
		 * BeanUtilsBean.getInstance().getConvertUtils().register(new Converter() {
		 * 
		 * @Override public Object convert(@SuppressWarnings("rawtypes") Class type, Object value) { return DateUtils.parse(value); } }, Date.class);
		 */
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(Object obj, Class<T> classOfType) {

		if (obj == null) {
			return null;
		}

		if (obj.getClass().isArray()) {

			Object[] o = (Object[]) obj;

			if (!classOfType.isArray()) {

				if (o.length == 0) {
					return null;
				} else {
					return convert(o[0], classOfType);
				}

			} else {

				Object[] array = (Object[]) Array.newInstance(classOfType.getComponentType(), o.length);
				for (int i = 0; i < o.length; i++) {
					array[i] = convert(o[i], classOfType.getComponentType());
				}
				return (T) array;

			}
		}

		if (obj instanceof String) {

			Class<?> desType = classOfType.isArray() ? classOfType.getComponentType() : classOfType;
			if (!String.class.isAssignableFrom(desType)) {
				if (StringUtils.isEmpty((String) obj)) {
					return null;
				}
			}
		}

		if (classOfType.isArray()) {

			Class<?> decType = classOfType.getComponentType();
			Class<?> srcType = obj.getClass();

			Object[] value;

			if (srcType.isArray()) {
				Object[] src = (Object[]) obj;
				value = (Object[]) Array.newInstance(decType, src.length);
				for (int i = 0; i < src.length; i++) {
					value[i] = convert(src[i], decType);
				}
			} else {
				value = (Object[]) Array.newInstance(decType, 1);
				value[0] = convert(obj, decType);
			}

			return (T) value;

		} else {

			Class<?> srcType = obj.getClass();
			Object src = null;
			if (srcType.isArray()) {
				Object[] srcArray = (Object[]) obj;
				if (srcArray.length > 0) {
					src = srcArray[0];
				}
			} else {
				src = obj;
			}

			if (src == null) {
				return null;
			}

			if (Date.class.isAssignableFrom(classOfType)) {
				return (T) DateUtils.parse(obj);
			} else if (Boolean.class.isAssignableFrom(classOfType) && obj instanceof String) {
				try {
					String s = ((String) obj).trim();
					if ("true".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s)) {
						return (T) Boolean.valueOf(s);
					} else if (s.matches("^[-]?\\d+[.]?\\d*$")) {
						return (T) new Boolean(Double.valueOf(s) > 0);
					}
				} catch (Exception e) {
				}

				return null;

			} else if (Boolean.class.isAssignableFrom(classOfType) && obj instanceof Number) {
				return (T) new Boolean(((Number) obj).doubleValue() > 0);
			} else {
				return (T) org.apache.commons.beanutils.ConvertUtils.convert(src, classOfType);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(String obj, Class<T> classOfType) {
		if (obj == null) {
			return null;
		}

		if (!String.class.isAssignableFrom(classOfType)) {
			if (StringUtils.isEmpty((String) obj)) {
				return null;
			}
		}

		// if (Date.class.isAssignableFrom(classOfType)) {
		// return (T) DateUtils.parse(obj);
		// }

		return (T) org.apache.commons.beanutils.ConvertUtils.convert(obj, classOfType);
	}

	public static String convert(Object obj) {
		if (obj == null) {
			return null;
		}
		return org.apache.commons.beanutils.ConvertUtils.convert(obj);
	}

	/**
	 * 返回map中的唯一列
	 * 
	 * @param map
	 * @return
	 */
	public static <T> T fromOnlyOneField(Map<String, ?> map, Class<T> classOfType) throws IllegalArgumentException {

		if (map.size() > 1) {
			throw new IllegalArgumentException("key numbers greater than 1");
		}

		T value = null;
		for (String key : map.keySet()) {
			value = convert(map.get(key), classOfType);
		}
		return value;
	}

	/**
	 * 将map转化为对象,map字段命名方式为aa_bb_cc,类字段命名方式为aaBbCc
	 * 
	 * @param classOfItem
	 * @param map
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static <T> T fromDB(Map<String, ? extends Object> map, Class<T> classOfItem) throws IllegalArgumentException {

		if (Character.class.isAssignableFrom(classOfItem) || String.class.isAssignableFrom(classOfItem) || Number.class.isAssignableFrom(classOfItem) || Boolean.class.isAssignableFrom(classOfItem) || Date.class.isAssignableFrom(classOfItem)) {
			return fromOnlyOneField(map, classOfItem);
		}

		T obj = null;

		for (String sqlFieldName : map.keySet()) {

			String fieldName = StringUtils.toJavaName(sqlFieldName);
			Method method = ClassUtils.getWriteMethod(classOfItem, fieldName);
			Object value = null;

			if (method != null) {
				Type[] types = method.getGenericParameterTypes();

				try {
					value = ConvertUtils.convert(map.get(sqlFieldName), (Class<?>) types[0]);
				} catch (Exception e) {
					logger.warn("set方法出错,无法转换类型:" + classOfItem.getName() + "@" + method.getName(), e);
				}

				if (obj == null) {
					obj = ClassUtils.newInstance(classOfItem);
				}

				if (value != null) {
					try {
						ClassUtils.invoke(method, obj, value);
					} catch (Exception e) {
						logger.warn("set方法出错", e);
					}
				}
			}
		}
		return obj;
	}

	/**
	 * 处理非数组
	 * 
	 * @param map
	 * @param classOfItem
	 * @return
	 * @throws IllegalArgumentException
	 */
	private static <T> T basicfromJava(Map<String, ? extends Object> map, Class<T> classOfItem) throws IllegalArgumentException {

		if (String.class.isAssignableFrom(classOfItem) || Number.class.isAssignableFrom(classOfItem) || Boolean.class.isAssignableFrom(classOfItem) || Date.class.isAssignableFrom(classOfItem)) {
			return fromOnlyOneField(map, classOfItem);
		}

		T obj = null;

		Map<String, Class<?>> subType = new HashMap<>();
		Map<String, Map<String, Object>> subContent = new HashMap<>();

		for (String javaFieldName : map.keySet()) {

			String fieldName;

			if (!javaFieldName.contains(".")) {
				fieldName = javaFieldName;
				if (fieldName.endsWith("[]")) {
					fieldName = fieldName.substring(0, fieldName.length() - 2);
				}
				Method method = ClassUtils.getWriteMethod(classOfItem, fieldName);
				Object value = null;

				if (method != null) {
					Type[] types = method.getGenericParameterTypes();

					try {
						value = ConvertUtils.convert(map.get(javaFieldName), (Class<?>) types[0]);
					} catch (Exception e) {
						logger.warn("set方法出错,无法转换类型:" + classOfItem.getName() + "@" + method.getName(), e);
					}

					if (obj == null) {
						obj = ClassUtils.newInstance(classOfItem);
					}

					if (value != null) {
						try {
							ClassUtils.invoke(method, obj, value);
						} catch (Exception e) {
							logger.warn("set方法出错", e);
						}
					}
				}

			} else {

				String[] fieldNames = StringUtils.splitFirst(javaFieldName, ".");
				fieldName = fieldNames[0];

				Class<?> type = subType.get(fieldName);
				if (type == null) {
					Method writeMethod = ClassUtils.getWriteMethod(classOfItem, fieldName);

					if (writeMethod != null) {
						type = (Class<?>) writeMethod.getGenericParameterTypes()[0];
						subType.put(fieldName, type);
						subContent.put(fieldName, new HashMap<String, Object>());
					}
				}

				if (type != null) {
					String subFiledName = fieldNames[1];
					subContent.get(fieldName).put(subFiledName, map.get(javaFieldName));
				}
			}
		}

		for (String fieldName : subType.keySet()) {
			Class<?> type = subType.get(fieldName);
			Map<String, Object> subMap = subContent.get(fieldName);
			Object value = fromJava(subMap, type);

			if (obj == null) {
				obj = ClassUtils.newInstance(classOfItem);
			}
			if (value != null) {
				try {
					ClassUtils.invokeWriteMethod(classOfItem, fieldName, obj, value);
				} catch (Exception e) {
					logger.warn("set方法出错", e);
				}
			}
		}

		return obj;
	}

	/**
	 * 将map转化为对象,map字段命名方式为aaBbCc,类字段命名方式为aaBbCc
	 * 
	 * @param classOfItem
	 * @param map
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static <T> T fromJava(Map<String, ? extends Object> map, Class<T> classOfItem) throws IllegalArgumentException {
		if (classOfItem.isArray()) {

			Class<?> subType = classOfItem.getComponentType();
			List<Object> list = new ArrayList<>();

			for (int i = 0;; i++) {
				Map<String, Object> subMap = new HashMap<>();

				for (String key : map.keySet()) {

					Object o = map.get(key);
					Object subValue = null;

					if (o != null) {

						if (key.contains(".") && i == 0) {
							subValue = o;

						} else {

							if (o.getClass().isArray()) {
								if (((Object[]) o).length > i) {
									subValue = ((Object[]) o)[i];
								}

							} else if (o instanceof Collection) {

								if (((Collection<?>) o).size() > i) {

									Iterator<?> it = ((Collection<?>) o).iterator();
									int m = 0;
									while (m <= i) {
										subValue = it.next();
										m++;
									}
								}

							} else if (i == 0) {
								subValue = o;
							}

						}
					}

					if (subValue != null) {
						subMap.put(key, subValue);
					}
				}

				if (subMap.size() == 0) {
					break;
				}

				list.add(basicfromJava(subMap, subType));

			}

			Object[] array = (Object[]) Array.newInstance(subType, list.size());
			for (int i = 0; i < list.size(); i++) {
				array[i] = list.get(i);
			}

			@SuppressWarnings("unchecked")
			T v = (T) array;
			return v;

		} else {
			return basicfromJava(map, classOfItem);
		}
	}

}
