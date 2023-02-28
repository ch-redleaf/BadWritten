package myself.badwritten.common.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import myself.badwritten.common.base.ModelReflecter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class ReflectUtils {

	private static Log logger = LogFactory.getLog(ReflectUtils.class);

	private static Map<Class<?>, Reflect> reflects = new ConcurrentSkipListMap<Class<?>, Reflect>(new Comparator<Class<?>>() {
		@Override
		public int compare(Class<?> o1, Class<?> o2) {
			return o1.getName().compareTo(o2.getName());
		}
	});

	public static class Reflect {

		private Class<?> classOfModel;
		private Class<? extends ModelReflecter> modelReflecterClass;
		private ModelReflecter modelReflecter;
		private Field[] fields;
		private Map<String, Field> fieldMap = new ConcurrentSkipListMap<String, Field>();
		private Map<String, Method> methods = new ConcurrentSkipListMap<String, Method>();
		private Map<String, Method> readMethods = new ConcurrentSkipListMap<String, Method>();
		private Map<String, Method> writeMethods = new ConcurrentSkipListMap<String, Method>();

		public Field[] getFields() {
			return fields;
		}

		public Field getField(String fieldName) {
			return fieldMap.get(fieldName);
		}

		public Method getMethod(String methodName) {
			return methods.get(methodName);
		}

		public Method getReadMethod(String fieldName) {
			return readMethods.get(fieldName);
		}

		public Method getWriteMethod(String fieldName) {
			return writeMethods.get(fieldName);
		}

		public void addMethod(Method method) {
			methods.put(method.getName(), method);
		}

		public void addReadMethod(String methodName, Method method) {
			readMethods.put(methodName, method);
			addMethod(method);
		}

		public void addWriteMethod(String methodName, Method method) {
			writeMethods.put(methodName, method);
			addMethod(method);
		}

		public Class<? extends ModelReflecter> getModelReflecterClass() {
			return modelReflecterClass;
		}

		public Class<?> getClassOfModel() {
			return classOfModel;
		}

		public ModelReflecter getModelReflecter() {
			return modelReflecter;
		}
	}

	private static ClassPool classPool = null;

	private static ClassPool getClassPool() {
		if (classPool == null) {
			synchronized (reflects) {
				if (classPool == null) {
					ClassPool pool = ClassPool.getDefault();

					String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
					try {
						pool.insertClassPath(classPath);

						if (classPath.endsWith("/WEB-INF/classes/")) {
							String lib = classPath.substring(0, classPath.lastIndexOf("/classes/")) + "/lib/*";
							pool.insertClassPath(lib);
						}

					} catch (NotFoundException e) {
						e.printStackTrace();
					}

					classPool = pool;
				}
			}
		}
		return classPool;
	}

	public static Reflect getReflect(Class<?> classOfType) {

		Reflect reflect = reflects.get(classOfType);

		if (reflect == null) {

			synchronized (reflects) {

				reflect = reflects.get(classOfType);

				if (reflect == null) {

					try {
						reflect = new Reflect();

						reflect.classOfModel = classOfType;

						Method[] mds = ClassUtils.getAllMethods(classOfType);
						for (int i = mds.length - 1; i >= 0; i--) {
							Method method = mds[i];
							reflect.addMethod(method);
						}

						Map<String, Boolean> fieldMap = new HashMap<String, Boolean>();

						Field[] fields = ClassUtils.getAllFields(classOfType);
						reflect.fields = fields;

						for (Field field : fields) {

							if (fieldMap.containsKey(field.getName())) {
								continue;
							}
							
							reflect.fieldMap.put(field.getName(), field);

							fieldMap.put(field.getName(), true);

							try {
								PropertyDescriptor pd = new PropertyDescriptor(field.getName(), classOfType);
								Method get = pd.getReadMethod();
								Method set = pd.getWriteMethod();

								if (get != null) {
									reflect.addReadMethod(field.getName(), get);
								}

								if (set != null) {
									reflect.addWriteMethod(field.getName(), set);
								}
							} catch (IntrospectionException e) {
							}
						}

						boolean isNotCreateNew = java.lang.reflect.Modifier.isFinal(classOfType.getModifiers()) || classOfType.getName().startsWith("java.lang.");
						//  || classOfType.getName().startsWith("org.apache.");

						try {
							classOfType.getConstructor();
						} catch (NoSuchMethodException e) {
							isNotCreateNew = true;
						} catch (SecurityException e) {
							isNotCreateNew = true;
						}

						if (!isNotCreateNew) {

							try {
								ClassPool pool = getClassPool();

								CtClass interfaceClass = pool.get(ModelReflecter.class.getName());
								CtClass superClass = pool.get(classOfType.getName());
								CtClass ctClass = pool.makeClass(classOfType.getName() + "$Jss$" + System.currentTimeMillis(), superClass);
								ctClass.addInterface(interfaceClass);

								CtConstructor defaultConstructor = CtNewConstructor.defaultConstructor(ctClass);
								defaultConstructor.setModifiers(Modifier.PUBLIC);
								ctClass.addConstructor(defaultConstructor);

								String newConstructor = getConstructorMethod(ctClass, fields, reflect);
								CtConstructor ctNewConstructor = CtNewConstructor.make(newConstructor, ctClass);
								ctNewConstructor.setModifiers(Modifier.PUBLIC);
								ctClass.addConstructor(ctNewConstructor);

								String writeMethodJava = getWriteMethod(fields, reflect, true);
								CtMethod ctWriteMethodJava = CtNewMethod.make(writeMethodJava, ctClass);
								ctClass.addMethod(ctWriteMethodJava);

								String writeMethodDb = getWriteMethod(fields, reflect, false);
								CtMethod ctWriteMethodDb = CtNewMethod.make(writeMethodDb, ctClass);
								ctClass.addMethod(ctWriteMethodDb);

								String readMethod = getReadMethod(fields, reflect);
								CtMethod ctReadMethod = CtNewMethod.make(readMethod, ctClass);
								ctClass.addMethod(ctReadMethod);

								// ClassLoader classClassLoader = classOfType.getClassLoader();
								// ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
								//
								// if (classClassLoader != threadClassLoader) {
								// try {
								// classClassLoader.loadClass(ModelReflecter.class.getName());
								// } catch (ClassNotFoundException e) {
								// interfaceClass.toClass(classClassLoader, ModelReflecter.class.getProtectionDomain());
								// }
								// }
								//
								// @SuppressWarnings("unchecked")
								// Class<? extends ModelReflecter> clz = ctClass.toClass(classClassLoader, classOfType.getProtectionDomain());

								@SuppressWarnings("unchecked")
								Class<? extends ModelReflecter> clz = ctClass.toClass();
								reflect.modelReflecterClass = clz;
								reflect.modelReflecter = clz.newInstance();

							} catch (Throwable e) {
								logger.warn("字节码生成失败 - " + classOfType.getName() + " - " + (e.getMessage().indexOf("\n") > 0 ? e.getMessage().subSequence(0, e.getMessage().indexOf("\n")) : e.getMessage()));
							}

						}

						reflects.put(classOfType, reflect);

					} catch (Throwable e) {
						throw new RuntimeException("初始化反射对象异常", e);
					}

				}
			}
		}
		return reflect;
	}

	private static String getConstructorMethod(CtClass self, Field[] allFields, Reflect reflect) {
		StringBuilder src = new StringBuilder();
		src.append("public " + self.getSimpleName() + "(" + reflect.getClassOfModel().getName() + " o) {\n");

		List<Field> fields = new ArrayList<>();
		for (Field field : allFields) {
			Method m1 = reflect.getWriteMethod(field.getName());
			Method m2 = reflect.getReadMethod(field.getName());
			if (m1 != null && m2 != null) {
				fields.add(field);
			}
		}

		if (fields.size() > 0) {

			for (Field field : fields) {
				String readMethod = reflect.getReadMethod(field.getName()).getName();
				String writeMethod = reflect.getWriteMethod(field.getName()).getName();
				src.append("  this." + writeMethod + "(o." + readMethod + "());\n");
			}

		}

		src.append("}");

		return src.toString();
	}

	private static String getWriteMethod(Field[] allFields, Reflect reflect, boolean isJavaFieldName) {

		List<Field> fields = new ArrayList<>();
		for (Field field : allFields) {
			Method m = reflect.getWriteMethod(field.getName());
			if (m != null && m.getParameterTypes().length == 1) {
				fields.add(field);
			}
		}

		StringBuilder src = new StringBuilder();

		src.append("public void " + (isJavaFieldName ? "$setJavaValues" : "$setDbValues") + "(java.util.Map values) {\n");

		if (fields.size() > 0) {

			src.append("  java.util.Iterator it = values.keySet().iterator();\n");
			src.append("  while (it.hasNext()) {\n");
			src.append("    String fieldName = (String) it.next();\n");
			src.append("    Object value = values.get(fieldName);\n");

			if (!isJavaFieldName) {
				src.append("    fieldName = me.hugo.common.util.StringUtils.toJavaName(fieldName);\n");
			}

			for (int i = 0; i < fields.size(); i++) {

				Field field = fields.get(i);

				src.append("    if (\"" + field.getName() + "\".equals(fieldName)) {\n");

				Method method = reflect.getWriteMethod(field.getName());

				boolean isArray = method.getParameterTypes()[0].isArray();
				String typeName = isArray ? field.getType().getComponentType().getName() : field.getType().getName();
				String parameterType = isArray ? method.getParameterTypes()[0].getComponentType().getName() : method.getParameterTypes()[0].getName();

				src.append("      " + parameterType + (isArray ? "[]" : "") + " v = (" + typeName + (isArray ? "[]" : "") + ") me.hugo.common.util.ConvertUtils.convert(value, " + parameterType + ".class);\n");
				src.append("      this." + method.getName() + "(v);\n");

				src.append("    }\n");

				if (i < fields.size() - 1) {
					src.append("    else \n");
				}

			}

			src.append("  }\n");

		}
		src.append("}");
		return src.toString();
	}

	private static String getReadMethod(Field[] allFields, Reflect reflect) {
		StringBuilder src = new StringBuilder();
		src.append("public Object $getValue(Object obj, String fieldName) {\n");

		List<Field> fields = new ArrayList<>();
		for (Field field : allFields) {
			Method m = reflect.getReadMethod(field.getName());
			if (m != null) {
				fields.add(field);
			}
		}

		if (fields.size() > 0) {

			src.append("  " + reflect.getClassOfModel().getName() + " o = (" + reflect.getClassOfModel().getName() + ")obj;\n");

			for (int i = 0; i < fields.size(); i++) {

				Field field = fields.get(i);

				src.append("  if (\"" + field.getName() + "\".equals(fieldName)) {\n");

				String method = reflect.getReadMethod(field.getName()).getName();
				src.append("    return o." + method + "();\n");

				src.append("  }\n");

				if (i < fields.size() - 1) {
					src.append("    else \n");
				}

			}
		}

		src.append("  return null;\n");

		src.append("}");

		return src.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T setValues(Class<T> classOfModel, Map<String, Object> values, boolean isJavaFieldName) {
		try {
			Reflect reflect = getReflect(classOfModel);
			Class<? extends ModelReflecter> clz = reflect.getModelReflecterClass();
			ModelReflecter obj = clz.newInstance();
			if (isJavaFieldName) {
				obj.$setJavaValues(values);
			} else {
				obj.$setDbValues(values);
			}
			return (T) obj;
		} catch (Exception e) {
			throw new RuntimeException("反射实例化对象异常", e);
		}
	}

	/**
	 * 根据Map设置对象
	 * 
	 * @param classOfModel
	 * @param values
	 * @return
	 */
	public static <T> T setJavaValues(Class<T> classOfModel, Map<String, Object> values) {
		return setValues(classOfModel, values, true);
	}

	/**
	 * 根据数据库字段设置对象
	 * 
	 * @param classOfModel
	 * @param values
	 * @return
	 */
	public static <T> T setDbValues(Class<T> classOfModel, Map<String, Object> values) {
		return setValues(classOfModel, values, false);
	}

	/**
	 * 获取对象指定字段的值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getValue(Object obj, String fieldName) {
		try {
			Reflect reflect = getReflect(obj.getClass());
			ModelReflecter o = reflect.getModelReflecter();
			return o.$getValue(obj, fieldName);
		} catch (Exception e) {
			throw new RuntimeException("反射异常", e);
		}
	}

}
