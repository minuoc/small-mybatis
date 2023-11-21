package com.chen.mybatis.reflection;

import com.chen.mybatis.reflection.invoker.GetFieldInvoker;
import com.chen.mybatis.reflection.invoker.Invoker;
import com.chen.mybatis.reflection.invoker.MethodInvoker;
import com.chen.mybatis.reflection.invoker.SetFieldInvoker;
import com.chen.mybatis.reflection.property.PropertyNamer;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Reflector {

    private static boolean classCacheEnabled = true;

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * 线程安全的缓存
     */
    private static final Map<Class<?>, Reflector> REFLECTOR_MAP = new ConcurrentHashMap<>();

    private Class<?> type;
    /**
     * get 属性列表
     */
    private String[] readablePropertyNames = EMPTY_STRING_ARRAY;

    /**
     * set 属性列表
     */
    private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;

    /**
     * set 方法列表
     */
    private Map<String, Invoker> setMethods = new HashMap<>();

    /**
     * get 方法列表
     */
    private Map<String, Invoker> getMethods = new HashMap<>();

    /**
     * set 类型列表
     */
    private Map<String,Class<?>> setTypes = new HashMap<>();

    /**
     * get 类型列表
     */
    private Map<String,Class<?>> getTypes = new HashMap<>();

    /**
     * 构造函数
     */
    private Constructor<?> defaultConstructor;

    private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

    public Reflector(Class<?> clazz) {
        this.type = clazz;

        // 加入构造函数
        addDefaultConstructor(clazz);
        //加入getter
        addGetMethods(clazz);
        // 加入setter
        addSetMethods(clazz);
        // 加入字段
        addFields(clazz);
        readablePropertyNames = getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
        writeablePropertyNames = setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
        for (String propName : readablePropertyNames) {
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
        for (String propName : writeablePropertyNames) {
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }

    }


    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] consts = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : consts) {
            if (constructor.getParameterTypes().length == 0) {
                if (canAccessPrivateMethods()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (Exception ignore) {
                        // Ignored. This is only a final precaution, nothing we can do
                    }
                }
                if (constructor.isAccessible()) {
                    this.defaultConstructor = constructor;
                }
            }
        }


    }

    private void addGetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") && name.length() > 3) {
                if (method.getParameterTypes().length == 0) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingGetters, name, method);
                }
            } else if (name.startsWith("is") && name.length() > 2) {
                if (method.getParameterTypes().length == 0) {
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingGetters, name, method);
                }
            }
        }
        resolveGetterConflicts(conflictingGetters);
    }

    private void addSetMethods(Class<?> clazz) {
        Map<String,List<Method>> conflictingSetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);
        for (Method method : methods){
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3){
                if (method.getParameterTypes().length == 1){
                    name = PropertyNamer.methodToProperty(name);
                    addMethodConflict(conflictingSetters, name, method);
                }
            }
        }
        resolveSetterConflicts(conflictingSetters);

    }

    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        for (String propName : conflictingSetters.keySet()) {
            List<Method> setters = conflictingSetters.get(propName);
            Method firstMethod = setters.get(0);
            if (setters.size() == 1){
                addSetMethod(propName,firstMethod);
            } else {
                Class<?> expectedType = getTypes.get(propName);
                if (expectedType!= null) {
                    throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                            + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                            "specification and can cause unpredicatble results.");
                } else {
                    Iterator<Method> methods = setters.iterator();
                    Method setter = null;
                    while (methods.hasNext()) {
                        Method method = methods.next();
                        if(method.getParameterTypes().length == 1 &&
                        expectedType.equals(method.getParameterTypes()[0])){
                            setter = method;
                            break;
                        }
                    }
                    if (setter == null) {
                        throw new RuntimeException("Illegal overloaded setter method with ambiguous type for property "
                                + propName + " in class " + firstMethod.getDeclaringClass() + ".  This breaks the JavaBeans " +
                                "specification and can cause unpredicatble results.");

                    }
                    addSetMethod(propName,setter);
                }
            }
        }
    }

    private void addSetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            setMethods.put(name, new MethodInvoker(method));
            setTypes.put(name, method.getParameterTypes()[0]);
        }
    }

    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {
                    // Ignored.
                }
            }
            if (field.isAccessible()) {
                if (!setMethods.containsKey(field.getName())) {
                    // issue #379 - removed the check for final because JDK 1.5 allows
                    // modification of final fields through reflection (JSR-133). (JGB)
                    // pr #16 - final static can only be set by the classloader
                    int modifier = field.getModifiers();
                    if(!(Modifier.isFinal(modifier) || Modifier.isStatic(modifier))) {
                        addSetField(field);
                    }
                }
                if (!getMethods.containsKey(field.getName())) {
                    addGetField(field);
                }
            }


        }
        if (clazz.getSuperclass()!= null) {
            addFields(clazz.getSuperclass());
        }

    }


    private void addSetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            setMethods.put(field.getName(), new SetFieldInvoker(field));
            setTypes.put(field.getName(), field.getType());
        }
    }

    private void addGetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            getMethods.put(field.getName(), new GetFieldInvoker(field));
            getTypes.put(field.getName(), field.getType());
        }
    }



    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        for (String propName : conflictingGetters.keySet()) {
            List<Method> getters = conflictingGetters.get(propName);
            Iterator<Method> iterator = getters.iterator();
            Method firstMethod = iterator.next();
            if (getters.size() == 1) {
                addGetMethod(propName,firstMethod);
            } else {
                Method getter = firstMethod;
                Class<?> getterType = firstMethod.getReturnType();
                while (iterator.hasNext()) {
                    Method method = iterator.next();
                    Class<?> methodType = method.getReturnType();
                    if (methodType.equals(getterType)) {
                        throw new RuntimeException("Illegal overloaded getter method with ambiuous type for property " +
                                propName + " in class " + firstMethod.getDeclaringClass() +
                                ".  This breaks the JavaBeans " + " specification and can cause unpredicatble results.");
                    } else if (methodType.isAssignableFrom(getterType)) {
                        // OK getter type is descendant
                    } else if (getterType.isAssignableFrom(methodType)) {
                        getter = method;
                        getterType = methodType;
                    } else {
                        throw new RuntimeException("Illegal overloaded getter method with ambiguous type for property " +
                                propName + " in class " + firstMethod.getDeclaringClass() + ". This breaks the JavaBeans "
                        + "specification and can cause unpredicatble results.");
                    }
                }
                addGetMethod(propName,getter);
            }
        }
    }

    private void addGetMethod(String name, Method method) {
        if (isValidPropertyName(name)){
            getMethods.put(name, new MethodInvoker(method));
            getTypes.put(name,method.getReturnType());
        }
    }

    private boolean isValidPropertyName(String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }

    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        List<Method> list = conflictingMethods.computeIfAbsent(name, k -> new ArrayList<Method>());
        list.add(method);
    }


    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            // we also need to look for interface methods
            // because the class may be abstract
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
            currentClass = currentClass.getSuperclass();
        }
        Collection<Method> methods = uniqueMethods.values();

        return methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            // ？？
            if (!currentMethod.isBridge()) {
                //取得签名
                String signature = getSignature(currentMethod);

                if (!uniqueMethods.containsKey(signature)) {
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {
                            //Ignore. This is only a final precaution, nothing we can do.
                        }
                    }
                    uniqueMethods.put(signature, currentMethod);
                }

            }
        }
    }

    private String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append("#");
        }
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());

        }
        return sb.toString();
    }

    private boolean canAccessPrivateMethods() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }

        } catch (SecurityException e) {
            return false;
        }
        return true;
    }


}
