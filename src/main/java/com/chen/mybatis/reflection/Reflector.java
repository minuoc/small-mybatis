package com.chen.mybatis.reflection;

import com.chen.mybatis.reflection.invoker.Invoker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ReflectPermission;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class Reflector {

    private static boolean classCacheEnabled = true;

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final Map<Class<?>,Reflector> REFLECTOR_MAP = new ConcurrentHashMap<>();

    private Class<?> type;

    private String[] readablePropertyNames = EMPTY_STRING_ARRAY;

    private String[] writeablePropertyNames = EMPTY_STRING_ARRAY;

    private Map<String, Invoker> setMethods = new HashMap<>();

    private Map<String,Invoker> getSetMethods = new HashMap<>();

    private Constructor<?> defaultConstructor;

    private Map<String,String> caseInsensitivePropertyMap = new HashMap<>();

    public Reflector(Class<?> clazz) {
        this.type = clazz;
        
        // 加入构造函数
        addDefaultConstructor(clazz);
    }

    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] consts = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : consts) {
            if (constructor.getParameterTypes().length == 0){
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

    private void addGetMethods(Class<?> clazz){
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        Method[] methods = getClassMethods(clazz);

    }

    private Method[] getClassMethods(Class<?> cls) {
        Map<String,Method> uniqueMethods = new HashMap<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            addUniqueMethods(uniqueMethods,currentClass.getDeclaredMethods());
            // we also need to look for interface methods
            // because the class may be abstract
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods,anInterface.getMethods());
            }
            currentClass = currentClass.getSuperclass();
        }
        Collection<Method> methods = uniqueMethods.values();

        return methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            // ？？
            if (!currentMethod.isBridge()){
                //取得签名
                String signature = getSignature(currentMethod);

                if (!uniqueMethods.containsKey(signature)){
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {
                            //Ignore. This is only a final precaution, nothing we can do.
                        }
                    }
                    uniqueMethods.put(signature,currentMethod);
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
            if (i == 0){
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

        } catch (SecurityException e){
            return false;
        }
        return true;
    }


}
