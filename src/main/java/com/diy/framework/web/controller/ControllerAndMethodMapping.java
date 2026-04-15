package com.diy.framework.web.controller;

import java.lang.reflect.Method;

public class ControllerAndMethodMapping {
    private final Object controller;
    private final Method method;

    public ControllerAndMethodMapping(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object invoke(Object... args) throws Exception {
        // http mapping annotation이 달린 method
        // 컨트롤러에게 파라미터 전달
        return method.invoke(controller, args);
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }
}
