package myself.badwritten.common.exception;

/**
 * 框架异常
 *
 */
public abstract class MvcException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 页面响应编码
	 */
	private int responseCode;

	/**
	 * 是否打印异常栈
	 */
	private boolean printStackTrace;

	public MvcException() {
	}

	public MvcException(int responseCode, boolean printStackTrace, String message) {
		super(message);
		this.responseCode = responseCode;
		this.printStackTrace = printStackTrace;
	}

	public MvcException(int responseCode, boolean printStackTrace, String message, Throwable cause) {
		super(message, cause);
		this.responseCode = responseCode;
		this.printStackTrace = printStackTrace;
	}

	public String getFullMessage() {
		return super.getMessage();
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public static String getExceptionMessage(Exception e) {
		if (e == null) {
			return "";
		}

		if (e instanceof MvcException) {
			return e.getMessage() == null ? "" : ((MvcException) e).getFullMessage();
		}

		return e == null || e.getMessage() == null ? "" : e.getMessage();
	}

	public boolean isPrintStackTrace() {
		return printStackTrace;
	}

	public void setPrintStackTrace(boolean printStackTrace) {
		this.printStackTrace = printStackTrace;
	}

}
