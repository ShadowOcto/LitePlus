package me.rhys.base.util.accessor.impl;

import me.rhys.base.util.accessor.Accessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodAccessor extends Accessor {

    private Method method;

    public MethodAccessor(Class<?> target, int index) {
        super(target);
        this.method = target.getDeclaredMethods()[index];
        this.method.setAccessible(true);
    }

    public MethodAccessor(Class<?> target, String name, Class<?>... parameterTypes) {
        super(target);
        try {
            this.method = target.getDeclaredMethod(name, parameterTypes);
            this.method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public MethodAccessor(Class<?> target, Method method) {
        super(target);
        this.method = method;
        this.method.setAccessible(true);
    }

    public <T> T invoke(Object parent, Object... params) {
        try {
            return (T) method.invoke(parent, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Method getMethod() {
        return method;
    }

}