package com.diy.framework.web.controller.handler.mapping;

import com.diy.framework.web.controller.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AnnotationBasedHandlerMapping implements HandlerMapping {

    private final Map<String, HandlerMethod> controllerAndMethodMappings;

    public AnnotationBasedHandlerMapping(Map<String, HandlerMethod> controllerAndMethodMappings) {
        this.controllerAndMethodMappings = controllerAndMethodMappings;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return controllerAndMethodMappings.get(buildKey(request));
    }

    private String buildKey(HttpServletRequest request) {
        return String.format("%s:%s", request.getMethod(), request.getRequestURI());
    }
}
