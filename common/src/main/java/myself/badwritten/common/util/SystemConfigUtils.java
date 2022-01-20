package myself.badwritten.common.util;

public class SystemConfigUtils {

	/**
	 * 是否启用调试模式
	 * 
	 * @return
	 */
	public static final boolean isDebug() {
		return PropertyUtils.getBoolean("system.debug", false);
	}

}
