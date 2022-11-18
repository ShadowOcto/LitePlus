package me.rhys.base.event;

import lombok.AllArgsConstructor;
import lombok.val;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.util.container.MapContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class EventBus extends MapContainer<Class<?>, List<EventBus.EventCall>> {

    public void register(Object handle) {
        Arrays.stream(handle.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(EventTarget.class)).forEach(method -> registerMethod(handle, method));
    }

    public void unRegister(Object handle) {
        getMap().forEach((aClass, eventCalls) -> eventCalls.removeIf(eventCall -> eventCall.handle == handle));
    }

    public <T> T call(Event event) {
        List<EventBus.EventCall> calls = get(event.getClass());
        if (calls != null && !calls.isEmpty()) {
            calls.stream().sorted(Comparator.comparingInt(value -> value.priority)).forEachOrdered(eventCall -> eventCall.call(event));
        }
        return (T) event;
    }

    private void registerMethod(Object handle, Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        val paramTypes = method.getParameterTypes();
        if (paramTypes.length > 0) {

            val eventType = paramTypes[0];
            if (eventType != null) {
                val target = method.getAnnotation(EventTarget.class);
                if (target == null) {
                    return;
                }

                val eventCall = new EventCall(handle, method, target.value().ordinal());

                getMap().putIfAbsent(eventType, new ArrayList<>());

                val calls = get(eventType);

                calls.add(eventCall);

                put(eventType, calls);
            }
        }
    }

    @AllArgsConstructor
    static class EventCall {

        Object handle;
        Method method;
        int priority;

        void call(Event event) {
            try {
                method.invoke(handle, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}