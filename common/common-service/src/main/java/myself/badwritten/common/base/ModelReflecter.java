package myself.badwritten.common.base;

import java.util.Map;

public interface ModelReflecter {

	public abstract void $setJavaValues(Map<String, Object> values);

	public abstract void $setDbValues(Map<String, Object> values);

	public abstract Object $getValue(String fieldName);

	public abstract Object $getValue(Object o, String fieldName);

}
