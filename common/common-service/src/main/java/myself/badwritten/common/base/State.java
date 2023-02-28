package myself.badwritten.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC响应实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class State<T> implements Serializable {

	private boolean success;

	private String message;

	private String type;

	private Object messageContent;

	private T data;

	/**
	 * 成功
	 * 
	 * @param data
	 * @return
	 */
	public static <T> State<T> success(T data) {
		return new State<>(true, "成功", "200", null, data);
	}


	/**
	 * 失败
	 * 
	 * @param type
	 *            业务类型
	 * @param message
	 *            消息
	 * @return
	 */
	public static <T> State<T> failed(String type, String message) {
		return new State<>(false, message, type, null , null);
	}


}
