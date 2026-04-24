package com.diy.framework.web.controller.handler.adapter;

import com.diy.framework.web.controller.ControllerHandler;
import com.diy.framework.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterfaceBasedHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof ControllerHandler;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ((ControllerHandler) handler).handleRequest(request, response);
    }
}
