package myself.badwritten.common.base;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import myself.badwritten.common.util.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 线程池<br>
 * 复制当前线程的ThreadLocal变量到子线程
 *
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {

	private Log logger = LogFactory.getLog(getClass());

	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	@Override
	public void execute(final Runnable command) {

		final Map<ThreadLocal<Object>, Object> locals = new HashMap<ThreadLocal<Object>, Object>();
		final Object threadLocals = ClassUtils.getFieldValue(Thread.class, "threadLocals", Thread.currentThread());

		if (threadLocals != null) {

			try {
				@SuppressWarnings("unchecked")
				Reference<ThreadLocal<Object>>[] tables = (Reference<ThreadLocal<Object>>[]) ClassUtils.getFieldValue(threadLocals.getClass(), "table", threadLocals);

				if (tables != null) {
					for (Reference<ThreadLocal<Object>> entry : tables) {
						if (entry != null) {
							ThreadLocal<Object> key = entry.get();
							if (key != null) {
								Object value = key.get();
								if (value != null) {
									locals.put(key, value);
								}
							}
						}
					}
				}
			} catch (Throwable e) {
				logger.warn("ThreadLocal读取出错 - " + e.getMessage());
			}
		}

		Runnable task = new Runnable() {
			@Override
			public void run() {

				for (ThreadLocal<Object> key : locals.keySet()) {
					key.set(locals.get(key));
				}

				try {
					command.run();
				} finally {
					for (ThreadLocal<Object> key : locals.keySet()) {
						key.remove();
					}
				}
			}
		};
		super.execute(task);
	}

}