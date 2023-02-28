package myself.badwritten.common.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Spring容器帮助类
 * 
 */
@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	private static ConfigurableApplicationContext configurableApplicationContext;
	private static DefaultListableBeanFactory beanFactory;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.applicationContext = applicationContext;
		if (applicationContext instanceof ConfigurableApplicationContext) {
			configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
			beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
		}
	}

	/**
	 * 获取spring ApplicationContext
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取spring bean
	 * 
	 * @param classOfType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> classOfType) {
		T bean = applicationContext.getBean(classOfType);
		if (bean == null) {
			String[] names = applicationContext.getBeanDefinitionNames();
			for (String name : names) {
				Object springBean = applicationContext.getBean(name);
				Object target = getTarget(springBean);
				if (classOfType.isInstance(target)) {
					return (T) target;
				}
			}
		}
		return bean;
	}

	/**
	 * 获取spring bean
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 获取spring bean
	 * 
	 * @param classOfType
	 * @return
	 */
	public static <T> Map<String, T> getBeans(Class<T> classOfType) {
		return applicationContext.getBeansOfType(classOfType);
	}

	/**
	 * 获取目标对象
	 * 
	 * @param proxy 代理对象
	 * @return
	 * @throws RuntimeException
	 */
	public static Object getTarget(Object proxy) throws RuntimeException {

		// 不是代理对象
		if (!AopUtils.isAopProxy(proxy)) {
			return proxy;
		}

		if (AopUtils.isJdkDynamicProxy(proxy)) {
			// JDK动态代理
			return getJdkDynamicProxyTarget(proxy);

		} else if (AopUtils.isCglibProxy(proxy)) {
			// cglib代理
			return getCglibProxyTarget(proxy);

		} else {
			return proxy;
		}

	}

	/**
	 * 获取JDK动态代理目标对象
	 * 
	 * @param proxy
	 * @return
	 * @throws RuntimeException
	 */
	private static Object getJdkDynamicProxyTarget(Object proxy) throws RuntimeException {

		try {
			Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
			h.setAccessible(true);
			AopProxy aopProxy = (AopProxy) h.get(proxy);

			Field advised = aopProxy.getClass().getDeclaredField("advised");
			advised.setAccessible(true);

			Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
			return target;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取Cglib代理的目标对象
	 * 
	 * @param proxy
	 * @return
	 * @throws RuntimeException
	 */
	private static Object getCglibProxyTarget(Object proxy) throws RuntimeException {
		try {
			Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
			h.setAccessible(true);
			Object dynamicAdvisedInterceptor = h.get(proxy);

			Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
			advised.setAccessible(true);

			Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
			return target;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除bean定义
	 * 
	 * @param beanName
	 */
	public static void removeBeanDefinition(String beanName) {
		beanFactory.removeBeanDefinition(beanName);
	}

}
