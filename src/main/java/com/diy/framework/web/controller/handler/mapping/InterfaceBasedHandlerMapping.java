package com.diy.framework.web.controller.handler.mapping;

import com.diy.framework.web.controller.ControllerHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class InterfaceBasedHandlerMapping implements HandlerMapping {

    private final Map<String, ControllerHandler> controllerMap;

    public InterfaceBasedHandlerMapping(Map<String, ControllerHandler> controllerMap) {
        this.controllerMap = controllerMap;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return controllerMap.get(request.getRequestURI());
    }
}
