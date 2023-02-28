package myself.badwritten.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

public class PropertyUtils {

	private static Log logger = LogFactory.getLog(PropertyUtils.class);
	private static Properties jndiProperties;
	private static Properties properties;
	private static String[] locations;

	public static void loadDefault() {
		if (properties == null) {
			synchronized (PropertyUtils.class) {
				if (properties == null) {
					locations = new String[] { "classpath*:config/**/conf-*.properties", "classpath*:config/**/conf-*.xml" };
					loadProperties();
				}
			}
		}
	}

	/**
	 * 设置值
	 * 
	 * @param properties
	 */
	static void put(Map<String, String> properties) {
		properties.putAll(properties);
	}

	/**
	 * 获取字符串类型
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return getString(key, null);
	}

	/**
	 * 获取字符串类型
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getString(String key, String defaultValue) {

		String value = null;
		if (properties.containsKey(key)) {
			value = properties.getProperty(key, defaultValue);
		} else if (jndiProperties != null) {
			value = jndiProperties.getProperty("conf_" + key, defaultValue);
		}

		if (value == null) {
			if (defaultValue != null) {
				value = defaultValue;
			} else {
				logger.warn("无法找到配置项:" + key);
			}
		}

		return value;
	}
	
	public static String getSingleString(String key, String defaultValue) {

		String value = null;
		if (properties.containsKey(key)) {
			value = properties.getProperty(key, defaultValue);
		} else if (jndiProperties != null) {
			value = jndiProperties.getProperty(key, defaultValue);
		}

		if (value == null) {
			if (defaultValue != null) {
				value = defaultValue;
			} else {
				logger.warn("无法找到配置项:" + key);
			}
		}

		return value;
	}

	/**
	 * 获取布尔类型
	 * 
	 * @param key 属性名
	 * @return
	 */
	public static Boolean getBoolean(String key) {
		String p = getString(key, null);
		return p == null ? null : Boolean.valueOf(p);
	}

	/**
	 * 获取布尔类型
	 * 
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return
	 */
	public static Boolean getBoolean(String key, Boolean defaultValue) {
		String p = getString(key, null);
		return p == null ? defaultValue : Boolean.valueOf(p);
	}

	public static Integer getInteger(String key) {
		String p = getString(key, null);
		return p == null ? null : Integer.valueOf(p);
	}

	public static Integer getInteger(String key, Integer defaultValue) {
		String p = getString(key, null);
		return p == null ? defaultValue : Integer.valueOf(p);
	}

	public static Long getLong(String key) {
		String p = getString(key, null);
		return p == null ? null : Long.valueOf(p);
	}

	public static Long getLong(String key, Long defaultValue) {
		String p = getString(key, null);
		return p == null ? defaultValue : Long.valueOf(p);
	}

	public static Double getDouble(String key) {
		String p = getString(key, null);
		return p == null ? null : Double.valueOf(p);
	}

	public static Double getDouble(String key, Double defaultValue) {
		String p = getString(key, null);
		return p == null ? defaultValue : Double.valueOf(p);
	}

	@PostConstruct
	public static void loadProperties() {

		Properties properties = new Properties();

		if (locations != null && locations.length > 0) {
			PathMatchingResourcePatternResolver resolover = new PathMatchingResourcePatternResolver();

			for (String location : locations) {
				try {
					Resource[] resources = resolover.getResources(location);
					for (Resource resource : resources) {
						try {
							fillProperties(properties, resource);
							if (logger.isDebugEnabled()) {
								logger.debug("加载配置文件 - " + resource.getFilename());
							}
						} catch (Exception e) {
							logger.error("配置文件异常 - " + resource.getURI());
						}
					}
				} catch (Exception e) {
					logger.error("配置文件路径读取异常 - " + location);
				}
			}
		}

		PropertyUtils.properties = properties;
	}

	private static void fillProperties(Properties props, Resource resource) throws IOException {

		PropertiesPersister persister = new DefaultPropertiesPersister();

		InputStream stream = null;
		Reader reader = null;
		try {
			String filename = resource.getFilename();
			if ((filename != null) && (filename.endsWith(".xml"))) {
				stream = resource.getInputStream();
				persister.loadFromXml(props, stream);
			} else {
				stream = resource.getInputStream();
				persister.load(props, stream);
			}
		} finally {
			if (stream != null) {
				stream.close();
			}
			if (reader != null)
				reader.close();
		}
	}

	public void setLocations(String[] locations) {
		PropertyUtils.locations = locations;
	}

	public void setJndiProperties(Properties jndiProperties) {
		if (jndiProperties != null) {
			PropertyUtils.jndiProperties = jndiProperties;

			for (Object key : jndiProperties.keySet()) {
				String k = (String) key;
				if (k.startsWith("sys.") && System.getProperty(k) == null) {
					System.setProperty(k, jndiProperties.getProperty(k));
				}
			}

		}
	}

}
