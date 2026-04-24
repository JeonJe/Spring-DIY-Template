package com.diy.framework.web.controller.handler.mapping;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    Object getHandler(HttpServletRequest request);

}
