package com.diy.framework.web.controller;

import com.diy.framework.web.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//헷갈려서 이름교체
@FunctionalInterface
public interface ControllerHandler {
    ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}
