package myself.badwritten.common.exception;


import myself.badwritten.common.util.SpringUtils;
import myself.badwritten.common.util.StringUtils;
import org.omg.PortableInterceptor.Interceptor;

/**
 * 拦截器异常
 *
 */
public class InterceptorException extends MvcException {

	private static final long serialVersionUID = 1L;

	/**
	 * 拦截器名称
	 */
	private String interceptorName;

	public InterceptorException() {
	}

	public InterceptorException(boolean printStackTrace, Interceptor interceptor, String message, Throwable cause) {
		this(interceptor, 500, printStackTrace, message, cause);
	}

	public InterceptorException(boolean printStackTrace, String interceptorName, String message, Throwable cause) {
		this(interceptorName, 500, printStackTrace, message, cause);
	}

	public InterceptorException(Interceptor interceptor, int responseCode, boolean printStackTrace, String message, Throwable cause) {
		this((String) null, responseCode, printStackTrace, message, cause);

		String interceptorName = null;

		if (interceptor != null) {
			Object target;
			Package pkg = interceptor.getClass().getPackage();
			if (pkg != null && pkg.getName().startsWith(Interceptor.class.getPackage().getName())) {
				target = interceptor;
			} else {
				target = SpringUtils.getTarget(interceptor);
			}

			if (target != null) {
				interceptorName = StringUtils.getFileExtension(target.getClass().getName());
			}
		}

		this.interceptorName = interceptorName;
	}

	public InterceptorException(String interceptorName, int responseCode, boolean printStackTrace, String message, Throwable cause) {
		super(responseCode, printStackTrace, message, cause);
		this.interceptorName = interceptorName;
	}

	@Override
	public String getFullMessage() {
		return interceptorName + " : " + super.getMessage();
	}

	public String getInterceptorName() {
		return interceptorName;
	}

	public void setInterceptorName(String interceptorName) {
		this.interceptorName = interceptorName;
	}

}
