package myself.badwritten.common.exception;

/**
 * Action异常
 *
 */
public class ActionException extends MvcException {

	private static final long serialVersionUID = 1L;
	private String actionPath;

	public ActionException() {
	}

	public ActionException(String actionPath, String message, Throwable cause) {
		super(500, true, message, cause);
		this.actionPath = actionPath;
	}

	@Override
	public String getFullMessage() {
		return actionPath + " : " + super.getMessage();
	}

	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

}
