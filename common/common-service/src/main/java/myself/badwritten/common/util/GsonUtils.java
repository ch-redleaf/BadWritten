package myself.badwritten.common.util;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class GsonUtils {

	public static final String DATE_FORMART = "yyyy-MM-dd";
	public static final String DATETIME_FORMART = "yyyy-MM-dd HH:mm:ss";
	public static final Type TYPE_MAP = new TypeToken<Map<String, Object>>() {}.getType();

	public static ExclusionStrategy EXCLUSION_EXPOSE = new ExclusionStrategy() {

		@Override
		public boolean shouldSkipField(FieldAttributes fa) {
			return fa.getAnnotation(Expose.class) != null;
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	};

	private static final Map<String, Gson> gsons = new ConcurrentSkipListMap<String, Gson>();

	/**
	 * 获取gson实例,使用 yyyy-MM-dd HH:mm:ss 转换日期,忽略空值,不转义特殊字符
	 * 
	 * @return
	 */
	public static Gson getInstance() {
		return getInstance(DATETIME_FORMART, false, false, true);
	}

	public static Gson getInstance(String dateformat, boolean serializeNulls, boolean htmlEscaping) {
		return getInstance(dateformat, serializeNulls, htmlEscaping, true);
	}

	public static Gson getInstance(String dateformat, boolean serializeNulls, boolean htmlEscaping, boolean exclusionExpose) {
		return getInstance(serializeNulls, htmlEscaping, exclusionExpose, dateformat);
	}

	/**
	 * 获取gson实例
	 * 
	 * @param serializeNulls 序列号空值
	 * @param htmlEscaping 转义特殊字符为unicode编码串
	 * @param exclusionExpose 是否忽略@Expose注解
	 * @param dateformat 日期格式
	 * @return
	 */
	public static Gson getInstance(boolean serializeNulls, boolean htmlEscaping, boolean exclusionExpose, String... dateformat) {

		StringBuilder key = new StringBuilder();
		final Set<String> datetimeFormarts = new ConcurrentSkipListSet<>();
		String firstFormat = null;
		key.append(serializeNulls + "_" + htmlEscaping + "_" + exclusionExpose);
		if (dateformat != null) {
			for (String fmt : dateformat) {
				if (StringUtils.hasText(fmt)) {
					key.append("_" + fmt);
					datetimeFormarts.add(fmt);
					if (firstFormat == null) {
						firstFormat = fmt;
					}
				}
			}
		}

		if (firstFormat == null) {
			firstFormat = DATETIME_FORMART;
			datetimeFormarts.add(firstFormat);
		}

		if (gsons.containsKey(key.toString())) {
			return gsons.get(key.toString());
		} else {

			GsonBuilder gb = new GsonBuilder();

			if (serializeNulls) {
				gb.serializeNulls();
			}

			if (!htmlEscaping) {
				gb.disableHtmlEscaping();
			}

			if (exclusionExpose) {
				gb.addSerializationExclusionStrategy(EXCLUSION_EXPOSE);
				gb.addDeserializationExclusionStrategy(EXCLUSION_EXPOSE);
			}

			// 日期
			gb.setDateFormat(firstFormat);

			gb.registerTypeHierarchyAdapter(Date.class, new JsonDeserializer<Date>() {

				@Override
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
					String dateStr = json.getAsString();
					Date date = null;
					if (StringUtils.isNotEmpty(dateStr)) {

						for (String fmt : datetimeFormarts) {
							date = DateUtils.parse(dateStr, fmt);
							if (fmt != null) {
								break;
							}
						}

						if (date == null) {
							date = DateUtils.parse(dateStr);
						}
					}
					return date;
				}

			});

			// 数字
			gb.registerTypeHierarchyAdapter(Number.class, new JsonSerializer<Number>() {
				@Override
				public JsonElement serialize(Number number, Type type, JsonSerializationContext ctx) {
					if (number.doubleValue() % 1 == 0) {
						return new JsonPrimitive(number.longValue());
					} else {
						return new JsonPrimitive(number);
					}
				}
			});

			Gson gson = gb.create();

			gsons.put(key.toString(), gson);

			return gson;
		}
	}

	/**
	 * json字符串转map
	 * 
	 * @param gson
	 * @param json
	 * @return
	 */
	public static Map<String, Object> toMap(Gson gson, String json) {
		if (gson == null) {
			gson = getInstance();
		}
		return gson.fromJson(json, TYPE_MAP);
	}

	/**
	 * json字符串转对象
	 * 
	 * @param gson
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> T toObject(Gson gson, String json, Class<T> classOfT) {
		if (gson == null) {
			gson = getInstance();
		}
		return gson.fromJson(json, classOfT);
	}

	/**
	 * json字符串转对象
	 * 
	 * @param gson
	 * @param json
	 * @param typeOfT 对象的类型,泛型可以使用 com.google.gson.reflect.TypeToken(){}.getType() 构建
	 * @return
	 */
	public static <T> T toObject(Gson gson, String json, Type typeOfT) {
		if (gson == null) {
			gson = getInstance();
		}
		return gson.fromJson(json, typeOfT);
	}

	/**
	 * 对象转json字符串
	 * 
	 * @param gson
	 * @param o
	 * @return
	 */
	public static String toJson(Gson gson, Object o) {
		if (gson == null) {
			gson = getInstance();
		}
		try {
			String s = gson.toJson(o);
			return s;
		} catch (JsonParseException e) {
			throw new RuntimeException("json转换异常:" + e.getMessage());
		} catch (Throwable e) {
			throw new RuntimeException("json转换异常:" + (e.getMessage() == null ? e.getClass().getName() : e.getMessage()));
		}
	}

}
