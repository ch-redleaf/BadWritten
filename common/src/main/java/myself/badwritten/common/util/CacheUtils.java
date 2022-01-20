package myself.badwritten.common.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import myself.badwritten.common.base.TraceThreadPoolExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 缓存帮助类
 * 
 * @author Hugo
 *
 */
public class CacheUtils {

	private static Log logger = LogFactory.getLog(CacheUtils.class);
	private static ExecutorService executorService = new TraceThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

	/**
	 * 缓存对象
	 * 
	 */
	public static class Cache<T> {

		private long expireTime;

		/**
		 * 缓存时间(分钟)
		 */
		private Double cacheMinutes;

		/**
		 * 缓存值
		 */
		private T object;

		public T getObject() {
			return object;
		}

		public void setObject(T object) {
			this.object = object;
		}

		public Double getCacheMinutes() {
			return cacheMinutes;
		}

		public void setCacheMinutes(Double cacheMinutes) {
			this.cacheMinutes = cacheMinutes;
		}

	}

	/**
	 * 更新缓存接口
	 */
	public static interface CacheValueFinder<T> {
		/**
		 * 获取结果
		 * 
		 * @return
		 */
		public abstract Cache<T> getCache();
	}

	/**
	 * 默认缓存时间,3分钟
	 */
	private static double DEFAULT_CACHE_TIME = 3.0;

	/**
	 * 所有缓存对象
	 */
	private static Map<String, Cache<?>> caches = new ConcurrentHashMap<String, Cache<?>>();

	/**
	 * 键锁
	 */
	private static Map<String, String> keyLocks = new Hashtable<String, String>();

	/**
	 * 设置缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	private static void put(String key, Cache<?> value) {
		if (key == null || value == null) {
			throw new NullPointerException("不能缓存空的键和值");
		}
		if (value.getCacheMinutes() == null) {
			value.setCacheMinutes(DEFAULT_CACHE_TIME);
		}
		value.expireTime = System.currentTimeMillis() + Double.valueOf(String.valueOf(value.getCacheMinutes() * 60000)).longValue();

		caches.put(key, value);
	}

	/**
	 * 更新缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static boolean update(String key, Object value) {
		boolean flag = false;

		if (value != null) {

			@SuppressWarnings("unchecked")
			Cache<Object> c = (Cache<Object>) caches.get(key);

			if (c != null) {
				Object old = c.getObject();
				if (old == null || old.getClass().isAssignableFrom(value.getClass())) {
					c.setObject(value);
					put(key, c);
				}
			}
		}

		return flag;
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	public static void remove(String key) {
		keyLocks.remove(key);
		caches.remove(key);
	}

	/**
	 * 清空缓存
	 */
	public static void removeAll() {
		keyLocks.clear();
		caches.clear();
	}

	/**
	 * 获取key列表
	 * 
	 * @return
	 */
	public static List<String> getKeys() {
		List<String> keys = new ArrayList<String>();

		for (String key : caches.keySet()) {
			keys.add(key);
		}

		return keys;
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 *            缓存名
	 * @param async
	 *            是否异步
	 * @param finder
	 *            缓存更新方法
	 * @return
	 */
	public static <T> T get(final String key, boolean async, final CacheValueFinder<T> finder) {

		async = false;
		
		if (key == null || finder == null) {
			throw new NullPointerException();
		}

		boolean update = false;
		Cache<?> c = caches.get(key);
		String keyLock = keyLocks.get(key);
		Object value;
		long now = System.currentTimeMillis();

		if (c == null) {
			update = true;

		} else {
			long expireTime = c.expireTime;
			if (now > expireTime) {
				update = true;
			}
		}

		if (update) {

			// 获取键锁，避免锁定整个缓存map
			synchronized (CacheUtils.class) {
				if (keyLock == null) {
					keyLock = new String(key);
					keyLocks.put(key, keyLock);
				}
			}

			// 锁定键
			synchronized (keyLock) {

				c = caches.get(key);
				if (c != null && now <= c.expireTime) {
					value = c.getObject();

				} else {

					if (c == null || !async) {

						if (logger.isDebugEnabled()) {
							logger.debug("update cache " + key);
						}
						c = finder.getCache();

						// 获取值为空
						if (c != null) {
							put(key, c);
							value = c.getObject();
						} else {
							value = null;
						}

					} else {

						value = c.getObject();

						Runnable task = new Runnable() {

							@Override
							public void run() {
								Cache<?> currentValue = caches.get(key);
								if (currentValue == null || System.currentTimeMillis() > currentValue.expireTime) {

									if (logger.isDebugEnabled()) {
										logger.debug("update cache in new thread " + key);
									}
									Cache<T> newValue = finder.getCache();

									if (newValue != null) {
										CacheUtils.put(key, newValue);
									}

								}
							}
						};

						executorService.execute(task);
					}
				}
			}

		} else {
			value = c.getObject();
		}

		@SuppressWarnings("unchecked")
		T rValue = (T) value;
		return rValue;
	}

	/**
	 * 获取缓存线程池大小
	 * 
	 * @return
	 */
	public static int getThreadCount() {
		int count = ((TraceThreadPoolExecutor) executorService).getPoolSize();
		return count;
	}

	/**
	 * 获取缓存线程池活动线程数
	 * 
	 * @return
	 */
	public static int getActiveCount() {
		int count = ((TraceThreadPoolExecutor) executorService).getActiveCount();
		return count;
	}
}
