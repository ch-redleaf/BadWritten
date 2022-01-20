package myself.badwritten.common.exception;

/**
 * 数据集异常
 *
 */
public class DatasetException extends MvcException {

	private static final long serialVersionUID = -1L;

	public DatasetException() {
		super(500, true, null, null);
	}

	public DatasetException(String message) {
		super(500, true, message, null);
	}

	public DatasetException(String message, Throwable cause) {
		super(500, true, message, cause);
	}

}
