package myself.badwritten.common.exception;

/**
 * 系统初始化异常
 *
 */
public class ApplicationInitializeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ApplicationInitializeException(String message) {
		super(message);
	}

	public ApplicationInitializeException(String message, Throwable cause) {
		super(message, cause);
	}

}
