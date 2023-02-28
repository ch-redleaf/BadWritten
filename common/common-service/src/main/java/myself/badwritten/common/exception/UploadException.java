package myself.badwritten.common.exception;

/**
 * 文件上传异常
 *
 */
public class UploadException extends MvcException {

	private static final long serialVersionUID = 1L;

	public UploadException() {
	}

	public UploadException(String message) {
		super(500, false, message);
	}

	public UploadException(String message, Throwable cause) {
		super(500, false, message, cause);
	}

	@Override
	public String getFullMessage() {
		return super.getMessage();
	}

}
