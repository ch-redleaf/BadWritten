package myself.badwritten.common.exception;

/**
 * 结果处理异常
 *
 */
public class ResultException extends MvcException {

	private static final long serialVersionUID = 1L;

	public ResultException() {
	}

	public ResultException(String message) {
		this(500, message, null);
	}

	public ResultException(String message, Throwable cause) {
		this(500, message, cause);
	}

	public ResultException(int responseCode, String message, Throwable cause) {
		super(responseCode, true, message, cause);
	}

}
