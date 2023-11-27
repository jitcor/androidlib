package com.github.humenger.xreflecthelpers;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
/**
 * 作者:humenger
 * 版本:1.1
 * 测试:No
 * 修改时间: 2022/09/08
 * 目的:
 * 更新日志:bugfix
 */

public class XReflectHelpers {
    public static final String TAG = "XReflectHelpers";
    private static final HashMap<String, Field> fieldCache = new HashMap();
    private static final HashMap<String, Method> methodCache = new HashMap();
    private static final HashMap<String, Constructor<?>> constructorCache=new HashMap<>();

    public static Class<?> findClass(String className, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        try {
            return ClassUtils.getClass(classLoader, className, false);
        } catch (ClassNotFoundException var3) {
            throw new ClassNotFoundError(var3.getCause());
        }
    }

    public static Object callMethod(Object obj, String methodName, Object... args) {
        try {
            return findMethodBestMatch(obj.getClass(), methodName, args).invoke(obj, args);
        } catch (IllegalAccessException var4) {
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        } catch (InvocationTargetException var6) {
            throw new InvocationTargetError(var6.getCause());
        }
    }

    public static Object callMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            return findMethodBestMatch(obj.getClass(), methodName, parameterTypes, args).invoke(obj, args);
        } catch (IllegalAccessException var5) {
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        } catch (InvocationTargetException var7) {
            throw new InvocationTargetError(var7.getCause());
        }
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args) {
        try {
            return findMethodBestMatch(clazz, methodName, args).invoke(null, args);
        } catch (IllegalAccessException var4) {
            throw new IllegalAccessError(var4.getMessage());
        } catch (IllegalArgumentException var5) {
            throw var5;
        } catch (InvocationTargetException var6) {
            throw new InvocationTargetError(var6.getCause());
        }
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            return findMethodBestMatch(clazz, methodName, parameterTypes, args).invoke(null, args);
        } catch (IllegalAccessException var5) {
            throw new IllegalAccessError(var5.getMessage());
        } catch (IllegalArgumentException var6) {
            throw var6;
        } catch (InvocationTargetException var7) {
            throw new InvocationTargetError(var7.getCause());
        }
    }

    /**
     * Thrown when a class loader is unable to find a class. Unlike {@link ClassNotFoundException},
     * callers are not forced to explicitly catch this. If uncaught, the error will be passed to the
     * next caller in the stack.
     */
    public static final class ClassNotFoundError extends Error {
        private static final long serialVersionUID = -1070936889459514628L;

        /**
         * @hide
         */
        public ClassNotFoundError(Throwable cause) {
            super(cause);
        }

        /**
         * @hide
         */
        public ClassNotFoundError(String detailMessage, Throwable cause) {
            super(detailMessage, cause);
        }
    }

    /**
     * This class provides a wrapper for an exception thrown by a method invocation.
     *
     * @see #callMethod(Object, String, Object...)
     * @see #callStaticMethod(Class, String, Object...)
     */
    public static final class InvocationTargetError extends Error {
        private static final long serialVersionUID = -1070936889459514628L;

        /**
         * @hide
         */
        public InvocationTargetError(Throwable cause) {
            super(cause);
        }
    }

    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Object... args) {
        return findMethodBestMatch(clazz, methodName, getParameterTypes(args));
    }

    /**
     * Look up a method in a class and set it to accessible.
     *
     * <p>See {@link #findMethodBestMatch(Class, String, Class...)} for details. This variant
     * determines the parameter types from the classes of the given objects. For any item that is
     * {@code null}, the type is taken from {@code parameterTypes} instead.
     */
    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Class<?>[] argsClasses = null;
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] != null)
                continue;
            if (argsClasses == null)
                argsClasses = getParameterTypes(args);
            parameterTypes[i] = argsClasses[i];
        }
        return findMethodBestMatch(clazz, methodName, parameterTypes);
    }

    /**
     * Look up a method in a class and set it to accessible.
     *
     * <p>This does'nt only look for exact matches, but for the best match. All considered candidates
     * must be compatible with the given parameter types, i.e. the parameters must be assignable
     * to the method's formal parameters. Inherited methods are considered here.
     *
     * @param clazz The class which declares, inherits or overrides the method.
     * @param methodName The method name.
     * @param parameterTypes The types of the method's parameters.
     * @return A reference to the best-matching method.
     * @throws NoSuchMethodError In case no suitable method was found.
     */
    public static Method findMethodBestMatch(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        String fullMethodName = clazz.getName() + '#' + methodName + getParametersString(parameterTypes) + "#bestmatch";

        if (methodCache.containsKey(fullMethodName)) {
            Method method = methodCache.get(fullMethodName);
            if (method == null)
                throw new NoSuchMethodError(fullMethodName);
            return method;
        }

        try {
            Method method = findMethodExact(clazz, methodName, parameterTypes);
            methodCache.put(fullMethodName, method);
            return method;
        } catch (NoSuchMethodError ignored) {}

        Method bestMatch = null;
        Class<?> clz = clazz;
        boolean considerPrivateMethods = true;
        do {
            for (Method method : clz.getDeclaredMethods()) {
                // don't consider private methods of superclasses
                if (!considerPrivateMethods && Modifier.isPrivate(method.getModifiers()))
                    continue;

                // compare name and parameters
                if (method.getName().equals(methodName) && ClassUtils.isAssignable(parameterTypes, method.getParameterTypes(), true)) {
                    // get accessible version of method
                    if (bestMatch == null || MemberUtils.compareParameterTypes(
                            method.getParameterTypes(),
                            bestMatch.getParameterTypes(),
                            parameterTypes) < 0) {
                        bestMatch = method;
                    }
                }
            }
            considerPrivateMethods = false;
        } while ((clz = clz.getSuperclass()) != null);

        if (bestMatch != null) {
            bestMatch.setAccessible(true);
            methodCache.put(fullMethodName, bestMatch);
            return bestMatch;
        } else {
            NoSuchMethodError e = new NoSuchMethodError(fullMethodName);
            methodCache.put(fullMethodName, null);
            throw e;
        }
    }

    private static String getParametersString(Class... clazzes) {
        StringBuilder sb = new StringBuilder("(");
        boolean first = true;
        for (Class<?> clazz : clazzes) {
            if (first)
                first = false;
            else
                sb.append(",");

            if (clazz != null)
                sb.append(clazz.getCanonicalName());
            else
                sb.append("null");
        }
        sb.append(")");
        return sb.toString();
    }

    private static Class<?>[] getParameterTypes(Object... args) {
        Class[] clazzes = new Class[args.length];

        for (int i = 0; i < args.length; ++i) {
            clazzes[i] = args[i] != null ? args[i].getClass() : null;
        }

        return clazzes;
    }

    /**
     * Creates a new instance of the given class.
     * See {@link #newInstance(Class, Object...)}.
     *
     * <p>This variant allows you to specify parameter types, which can help in case there are multiple
     * constructors with the same name, especially if you call it with {@code null} parameters.
     */
    public static Object newInstance(Class<?> clazz, Class<?>[] parameterTypes, Object... args) {
        try {
            return findConstructorBestMatch(clazz, parameterTypes, args).newInstance(args);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw new InvocationTargetError(e.getCause());
        } catch (InstantiationException e) {
            throw new InstantiationError(e.getMessage());
        }
    }

    /**
     * Creates a new instance of the given class.
     * The constructor is resolved using {@link #findConstructorBestMatch(Class, Object...)}.
     *
     * @param clazz The class reference.
     * @param args The arguments for the constructor call.
     * @throws NoSuchMethodError In case no suitable constructor was found.
     * @throws InvocationTargetError In case an exception was thrown by the invoked method.
     * @throws InstantiationError In case the class cannot be instantiated.
     */
    public static Object newInstance(Class<?> clazz, Object... args) {
        try {
            return findConstructorBestMatch(clazz, args).newInstance(args);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (InvocationTargetException e) {
            throw new InvocationTargetError(e.getCause());
        } catch (InstantiationException e) {
            throw new InstantiationError(e.getMessage());
        }
    }

    /**
     * Look up a constructor of a class and set it to accessible.
     * See {@link #findMethodExact(String, ClassLoader, String, Object...)} for details.
     */
    public static Constructor<?> findConstructorExact(Class<?> clazz, Class<?>... parameterTypes) {
        String fullConstructorName = clazz.getName() + getParametersString(parameterTypes) + "#exact";

        if (constructorCache.containsKey(fullConstructorName)) {
            Constructor<?> constructor = constructorCache.get(fullConstructorName);
            if (constructor == null)
                throw new NoSuchMethodError(fullConstructorName);
            return constructor;
        }

        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            constructorCache.put(fullConstructorName, constructor);
            return constructor;
        } catch (NoSuchMethodException e) {
            constructorCache.put(fullConstructorName, null);
            throw new NoSuchMethodError(fullConstructorName);
        }
    }

    /**
     * Look up a constructor in a class and set it to accessible.
     *
     * <p>See {@link #findMethodBestMatch(Class, String, Class...)} for details.
     */
    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Class<?>... parameterTypes) {
        String fullConstructorName = clazz.getName() + getParametersString(parameterTypes) + "#bestmatch";

        if (constructorCache.containsKey(fullConstructorName)) {
            Constructor<?> constructor = constructorCache.get(fullConstructorName);
            if (constructor == null)
                throw new NoSuchMethodError(fullConstructorName);
            return constructor;
        }

        try {
            Constructor<?> constructor = findConstructorExact(clazz, parameterTypes);
            constructorCache.put(fullConstructorName, constructor);
            return constructor;
        } catch (NoSuchMethodError ignored) {}

        Constructor<?> bestMatch = null;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            // compare name and parameters
            if (ClassUtils.isAssignable(parameterTypes, constructor.getParameterTypes(), true)) {
                // get accessible version of method
                if (bestMatch == null || MemberUtils.compareParameterTypes(
                        constructor.getParameterTypes(),
                        bestMatch.getParameterTypes(),
                        parameterTypes) < 0) {
                    bestMatch = constructor;
                }
            }
        }

        if (bestMatch != null) {
            bestMatch.setAccessible(true);
            constructorCache.put(fullConstructorName, bestMatch);
            return bestMatch;
        } else {
            NoSuchMethodError e = new NoSuchMethodError(fullConstructorName);
            constructorCache.put(fullConstructorName, null);
            throw e;
        }
    }

    /**
     * Look up a constructor in a class and set it to accessible.
     *
     * <p>See {@link #findMethodBestMatch(Class, String, Class...)} for details. This variant
     * determines the parameter types from the classes of the given objects.
     */
    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Object... args) {
        return findConstructorBestMatch(clazz, getParameterTypes(args));
    }

    /**
     * Look up a constructor in a class and set it to accessible.
     *
     * <p>See {@link #findMethodBestMatch(Class, String, Class...)} for details. This variant
     * determines the parameter types from the classes of the given objects. For any item that is
     * {@code null}, the type is taken from {@code parameterTypes} instead.
     */
    public static Constructor<?> findConstructorBestMatch(Class<?> clazz, Class<?>[] parameterTypes, Object[] args) {
        Class<?>[] argsClasses = null;
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] != null)
                continue;
            if (argsClasses == null)
                argsClasses = getParameterTypes(args);
            parameterTypes[i] = argsClasses[i];
        }
        return findConstructorBestMatch(clazz, parameterTypes);
    }


    public static Method findMethodExact(Class<?> clazz, String methodName, Object... parameterTypes) {
        return findMethodExact(clazz, methodName, getParameterClasses(clazz.getClassLoader(), parameterTypes));
    }

    public static Method findMethodExact(String className, ClassLoader classLoader, String methodName, Object... parameterTypes) {
        return findMethodExact(findClass(className, classLoader), methodName, getParameterClasses(classLoader, parameterTypes));
    }

    /**
     * Look up a method in a class and set it to accessible.
     * See {@link #findMethodExact(String, ClassLoader, String, Object...)} for details.
     *
     * <p>This variant requires that you already have reference to all the parameter types.
     */
    public static Method findMethodExact(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        String fullMethodName = clazz.getName() + '#' + methodName + getParametersString(parameterTypes) + "#exact";

        if (methodCache.containsKey(fullMethodName)) {
            Method method = methodCache.get(fullMethodName);
            if (method == null)
                throw new NoSuchMethodError(fullMethodName);
            return method;
        }

        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            methodCache.put(fullMethodName, method);
            return method;
        } catch (NoSuchMethodException e) {
            methodCache.put(fullMethodName, null);
            throw new NoSuchMethodError(fullMethodName);
        }
    }

    /**
     * Look up a field in a class and set it to accessible.
     *
     * @param clazz     The class which either declares or inherits the field.
     * @param fieldName The field name.
     * @return A reference to the field.
     * @throws NoSuchFieldError In case the field was not found.
     */
    public static Field findField(Class<?> clazz, String fieldName) {
        String fullFieldName = clazz.getName() + '#' + fieldName;

        if (fieldCache.containsKey(fullFieldName)) {
            Field field = fieldCache.get(fullFieldName);
            if (field == null)
                throw new NoSuchFieldError(fullFieldName);
            return field;
        }

        try {
            Field field = findFieldRecursiveImpl(clazz, fieldName);
            field.setAccessible(true);
            fieldCache.put(fullFieldName, field);
            return field;
        } catch (NoSuchFieldException e) {
            fieldCache.put(fullFieldName, null);
            throw new NoSuchFieldError(fullFieldName);
        }
    }

//#################################################################################################

    /**
     * Sets the value of an object field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setObjectField(Object obj, String fieldName, Object value) {
        try {
            findField(obj.getClass(), fieldName).set(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a {@code boolean} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setBooleanField(Object obj, String fieldName, boolean value) {
        try {
            findField(obj.getClass(), fieldName).setBoolean(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a {@code byte} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setByteField(Object obj, String fieldName, byte value) {
        try {
            findField(obj.getClass(), fieldName).setByte(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a {@code char} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setCharField(Object obj, String fieldName, char value) {
        try {
            findField(obj.getClass(), fieldName).setChar(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a {@code double} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setDoubleField(Object obj, String fieldName, double value) {
        try {
            findField(obj.getClass(), fieldName).setDouble(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a {@code float} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setFloatField(Object obj, String fieldName, float value) {
        try {
            findField(obj.getClass(), fieldName).setFloat(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of an {@code int} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setIntField(Object obj, String fieldName, int value) {
        try {
            findField(obj.getClass(), fieldName).setInt(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a {@code long} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setLongField(Object obj, String fieldName, long value) {
        try {
            findField(obj.getClass(), fieldName).setLong(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a {@code short} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static void setShortField(Object obj, String fieldName, short value) {
        try {
            findField(obj.getClass(), fieldName).setShort(obj, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    //#################################################################################################

    /**
     * Returns the value of an object field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static Object getObjectField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).get(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /** For inner classes, returns the surrounding instance, i.e. the {@code this} reference of the surrounding class. */
    public static Object getSurroundingThis(Object obj) {
        return getObjectField(obj, "this$0");
    }

    /**
     * Returns the value of a {@code boolean} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean getBooleanField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getBoolean(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the value of a {@code byte} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static byte getByteField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getByte(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the value of a {@code char} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static char getCharField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getChar(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the value of a {@code double} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static double getDoubleField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getDouble(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the value of a {@code float} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static float getFloatField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getFloat(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the value of an {@code int} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static int getIntField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getInt(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the value of a {@code long} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static long getLongField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getLong(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the value of a {@code short} field in the given object instance. A class reference is not sufficient! See also {@link #findField}.
     */
    public static short getShortField(Object obj, String fieldName) {
        try {
            return findField(obj.getClass(), fieldName).getShort(obj);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    //#################################################################################################

    /**
     * Sets the value of a static object field in the given class. See also {@link #findField}.
     */
    public static void setStaticObjectField(Class<?> clazz, String fieldName, Object value) {
        try {
            findField(clazz, fieldName).set(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code boolean} field in the given class. See also {@link #findField}.
     */
    public static void setStaticBooleanField(Class<?> clazz, String fieldName, boolean value) {
        try {
            findField(clazz, fieldName).setBoolean(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code byte} field in the given class. See also {@link #findField}.
     */
    public static void setStaticByteField(Class<?> clazz, String fieldName, byte value) {
        try {
            findField(clazz, fieldName).setByte(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code char} field in the given class. See also {@link #findField}.
     */
    public static void setStaticCharField(Class<?> clazz, String fieldName, char value) {
        try {
            findField(clazz, fieldName).setChar(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code double} field in the given class. See also {@link #findField}.
     */
    public static void setStaticDoubleField(Class<?> clazz, String fieldName, double value) {
        try {
            findField(clazz, fieldName).setDouble(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code float} field in the given class. See also {@link #findField}.
     */
    public static void setStaticFloatField(Class<?> clazz, String fieldName, float value) {
        try {
            findField(clazz, fieldName).setFloat(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code int} field in the given class. See also {@link #findField}.
     */
    public static void setStaticIntField(Class<?> clazz, String fieldName, int value) {
        try {
            findField(clazz, fieldName).setInt(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code long} field in the given class. See also {@link #findField}.
     */
    public static void setStaticLongField(Class<?> clazz, String fieldName, long value) {
        try {
            findField(clazz, fieldName).setLong(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code short} field in the given class. See also {@link #findField}.
     */
    public static void setStaticShortField(Class<?> clazz, String fieldName, short value) {
        try {
            findField(clazz, fieldName).setShort(null, value);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    //#################################################################################################

    /**
     * Returns the value of a static object field in the given class. See also {@link #findField}.
     */
    public static Object getStaticObjectField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).get(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Returns the value of a static {@code boolean} field in the given class. See also {@link #findField}.
     */
    public static boolean getStaticBooleanField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getBoolean(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code byte} field in the given class. See also {@link #findField}.
     */
    public static byte getStaticByteField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getByte(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code char} field in the given class. See also {@link #findField}.
     */
    public static char getStaticCharField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getChar(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code double} field in the given class. See also {@link #findField}.
     */
    public static double getStaticDoubleField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getDouble(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code float} field in the given class. See also {@link #findField}.
     */
    public static float getStaticFloatField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getFloat(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code int} field in the given class. See also {@link #findField}.
     */
    public static int getStaticIntField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getInt(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code long} field in the given class. See also {@link #findField}.
     */
    public static long getStaticLongField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getLong(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    /**
     * Sets the value of a static {@code short} field in the given class. See also {@link #findField}.
     */
    public static short getStaticShortField(Class<?> clazz, String fieldName) {
        try {
            return findField(clazz, fieldName).getShort(null);
        } catch (IllegalAccessException e) {
            // should not happen
            throw new IllegalAccessError(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    //###########################内部方法，请勿调用及修改#################
    private static Field findFieldRecursiveImpl(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            while (true) {
                clazz = clazz.getSuperclass();
                if (clazz == null || clazz.equals(Object.class))
                    break;

                try {
                    return clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException ignored) {
                }
            }
            throw e;
        }
    }

    private static Class<?>[] getParameterClasses(ClassLoader classLoader, Object[] parameterTypesAndCallback) {
        Class[] parameterClasses = null;

        for (int i = parameterTypesAndCallback.length - 1; i >= 0; --i) {
            Object type = parameterTypesAndCallback[i];
            if (type == null) {
                throw new ClassNotFoundError("parameter type must not be null", null);
            }

            if (!(findClass("de.robv.android.xposed.XC_MethodHook", classLoader).isInstance(type))) {
                if (parameterClasses == null) {
                    parameterClasses = new Class[i + 1];
                }

                if (type instanceof Class) {
                    parameterClasses[i] = (Class) type;
                } else {
                    if (!(type instanceof String)) {
                        throw new ClassNotFoundError("parameter type must either be specified as Class or String", null);
                    }

                    parameterClasses[i] = findClass((String) type, classLoader);
                }
            }
        }

        if (parameterClasses == null) {
            parameterClasses = new Class[0];
        }

        return parameterClasses;
    }

    static abstract class MemberUtils {
        private static final int ACCESS_TEST = 7;
        private static final Class<?>[] ORDERED_PRIMITIVE_TYPES;

        MemberUtils() {
        }

        static {
            Class[] clsArr = new Class[ACCESS_TEST];
            clsArr[0] = Byte.TYPE;
            clsArr[1] = Short.TYPE;
            clsArr[2] = Character.TYPE;
            clsArr[3] = Integer.TYPE;
            clsArr[4] = Long.TYPE;
            clsArr[5] = Float.TYPE;
            clsArr[6] = Double.TYPE;
            ORDERED_PRIMITIVE_TYPES = clsArr;
        }

        static void setAccessibleWorkaround(AccessibleObject o) {
            if (o != null && !o.isAccessible()) {
                Member m = (Member) o;
                if (Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                    try {
                        o.setAccessible(true);
                    } catch (SecurityException e) {
                    }
                }
            }
        }

        static boolean isPackageAccess(int modifiers) {
            return (modifiers & ACCESS_TEST) == 0;
        }

        static boolean isAccessible(Member m) {
            return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
        }

        public static int compareParameterTypes(Class<?>[] left, Class<?>[] right, Class<?>[] actual) {
            float leftCost = getTotalTransformationCost(actual, left);
            float rightCost = getTotalTransformationCost(actual, right);
            if (leftCost < rightCost) {
                return -1;
            }
            return rightCost < leftCost ? 1 : 0;
        }

        private static float getTotalTransformationCost(Class<?>[] srcArgs, Class<?>[] destArgs) {
            float totalCost = 0.0f;
            for (int i = 0; i < srcArgs.length; i++) {
                totalCost += getObjectTransformationCost(srcArgs[i], destArgs[i]);
            }
            return totalCost;
        }

        private static float getObjectTransformationCost(Class<?> srcClass, Class<?> destClass) {
            if (destClass.isPrimitive()) {
                return getPrimitivePromotionCost(srcClass, destClass);
            }
            float cost = 0.0f;
            Class destClass2 = null;
            while (destClass2 != null && !destClass2.equals(srcClass)) {
                if (destClass2.isInterface() &&ClassUtils.isAssignable(srcClass, destClass2)) {
                    cost += 0.25f;
                    break;
                }
                cost += 1.0f;
                destClass2 = destClass2.getSuperclass();
            }
            if (destClass2 == null) {
                return cost + 1.5f;
            }
            return cost;
        }

        private static float getPrimitivePromotionCost(Class<?> srcClass, Class<?> destClass) {
            float cost = 0.0f;
            Class<?> cls = srcClass;
            if (!cls.isPrimitive()) {
                cost = 0.0f + 0.1f;
                cls = ClassUtils.wrapperToPrimitive(cls);
            }
            int i = 0;
            while (cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length) {
                if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                    cost += 0.1f;
                    if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                        cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                    }
                }
                i++;
            }
            return cost;
        }
    }
}
