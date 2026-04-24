package com.diy.framework.web.controller.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerMethod {
    private final Object controller;
    private final Method method;

    public HandlerMethod(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object invoke(Object... args) throws Exception {
        // http mapping annotation이 달린 method
        // 컨트롤러에게 파라미터 전달
        return method.invoke(controller, resolveArguments(args));
    }

    private Object[] resolveArguments(Object... args) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] resolvedArgs = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            resolvedArgs[i] = resolveArgument(parameterTypes[i], args);
        }
        return resolvedArgs;
    }

    private Object resolveArgument(Class<?> parameterType, Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                continue;
            }
            if (parameterType.isAssignableFrom(arg.getClass())) {
                return arg;
            }
            if (parameterType == HttpServletRequest.class && arg instanceof HttpServletRequest) {
                return arg;
            }
            if (parameterType == HttpServletResponse.class && arg instanceof HttpServletResponse) {
                return arg;
            }
        }
        return null;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }
}
