package yanson.reflection;

import yanson.annotation.JsonFieldProcessor;
import yanson.json.Configuration;

import java.lang.reflect.*;
import java.util.*;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/20 18:14
 */
public class ClassUtil extends ClassLoader {

    private ClassUtil() {
        throw new UnsupportedOperationException("The constructor can not be called outside");
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (null != classLoader) {
            return classLoader;
        }
        classLoader = ClassUtil.class.getClassLoader();
        if (null != classLoader) {
            return classLoader;
        }
        return getSystemClassLoader();
    }

    public static <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        try {
            type = resolveInterface(type);
            Constructor<T> constructor;
            if (constructorArgTypes == null || constructorArgs == null) {
                constructor = type.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            }
            constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));
        }
        catch (Exception e) {
            StringBuilder argTypes = new StringBuilder();
            if (constructorArgTypes != null && !constructorArgTypes.isEmpty()) {
                for (Class<?> argType : constructorArgTypes) {
                    argTypes.append(argType.getSimpleName());
                    argTypes.append(",");
                }
                argTypes.deleteCharAt(argTypes.length() - 1);
            }
            StringBuilder argValues = new StringBuilder();
            if (constructorArgs != null && !constructorArgs.isEmpty()) {
                for (Object argValue : constructorArgs) {
                    argValues.append(argValue);
                    argValues.append(",");
                }
                argValues.deleteCharAt(argValues.length() - 1);
            }
            return null;
        }
    }

    public static <T> Class<T> resolveInterface(Class<T> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            classToCreate = ArrayList.class;
        }
        else if (type == Map.class) {
            classToCreate = HashMap.class;
        }
        else if (type == SortedSet.class) {
            classToCreate = TreeSet.class;
        }
        else if (type == Set.class) {
            classToCreate = HashSet.class;
        }
        else {
            classToCreate = type;
        }
        return (Class<T>) classToCreate;
    }

    public static Map<String, Invoker> getInvokerMap(Class<?> clazz, InvokerType invokerType) {
        List<Invoker> fieldInvokers = new ArrayList<>();
        List<Invoker> methodInvokers = new ArrayList<>();
        switch (invokerType) {
            case ALL:
                fieldInvokers = getFieldInvokers(clazz);
                methodInvokers = getMethodInvokers(clazz);
                break;
            case FIELD:
                fieldInvokers = getFieldInvokers(clazz);
                break;
            case METHOD:
                methodInvokers = getMethodInvokers(clazz);
                break;
            default:
        }

        if (Configuration.PREFER_FIELD_VALUE_SET) {
            return getInvokerMap(fieldInvokers, methodInvokers);
        }
        else {
            return getInvokerMap(methodInvokers, fieldInvokers);
        }
    }

    private static Map<String, Invoker> getInvokerMap(List<Invoker> firstInvokers, List<Invoker> secondInvokers){
        Map<String, Invoker> invokerMap = new HashMap<>();
        for (Invoker invoker : firstInvokers) {
            invokerMap.put(invoker.getName(), invoker);
            invokerMap.putAll(JsonFieldProcessor.process(invoker));
        }
        for (Invoker invoker : secondInvokers) {
            invokerMap.put(invoker.getName(), invoker);
            invokerMap.putAll(JsonFieldProcessor.process(invoker));
        }
        return invokerMap;
    }

    private static List<Invoker> getFieldInvokers(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        List<Invoker> invokers = new ArrayList<>();
        for (Field field : fields) {
            FieldInvoker invoker = new FieldInvoker(field);
            if (null != invoker.getName()) {
                invokers.add(invoker);
            }
        }
        return invokers;
    }

    public static List<Invoker> getMethodInvokers(Class<?> clazz, boolean isRead) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Invoker> invokers = new ArrayList<>();
        for (Method method : methods) {
            MethodInvoker invoker = new MethodInvoker(method, isRead);
            if (null != invoker.getName()) {
                invokers.add(invoker);
            }
        }
        return invokers;
    }

    public static List<Invoker> getMethodInvokers(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        List<Invoker> invokers = new ArrayList<>();
        for (Method method : methods) {
            MethodInvoker invoker = new MethodInvoker(method);
            if (null != invoker.getName()) {
                invokers.add(invoker);
            }
        }
        return invokers;
    }

    public static Class getSuperClassGenericType(Class clazz, int index) throws IndexOutOfBoundsException {
        Type genericType = clazz.getGenericSuperclass();
        if (!(genericType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genericType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

}
