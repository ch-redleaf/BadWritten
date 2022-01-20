package myself.badwritten.common.exception;

/**
 * 参数设置异常
 *
 */
public class ParameterException extends MvcException {

	private static final long serialVersionUID = -1L;

	public ParameterException() {
		super(400, true, null, null);
	}

	public ParameterException(String message) {
		super(400, true, message, null);
	}

	public ParameterException(String message, Throwable cause) {
		super(400, true, message, cause);
	}

}
