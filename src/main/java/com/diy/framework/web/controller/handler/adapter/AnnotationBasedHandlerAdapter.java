package com.diy.framework.web.controller.handler.adapter;

import com.diy.framework.web.controller.handler.HandlerMethod;
import com.diy.framework.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationBasedHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object result = ((HandlerMethod) handler).invoke(request, response);
        return result instanceof ModelAndView ? (ModelAndView) result : null;
    }
}
