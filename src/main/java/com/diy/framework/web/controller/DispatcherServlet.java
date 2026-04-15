package com.diy.framework.web.controller;

import com.diy.framework.web.view.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@WebServlet("/") // 톰캣이 자동으로 인스턴스 생성, 외부 파라미터 주입 불가 함
public class DispatcherServlet extends HttpServlet {
    private final Map<String, ControllerAndMethodMapping> controllerMap;

    public DispatcherServlet(Map<String, ControllerAndMethodMapping> controllerMap) {
        this.controllerMap = controllerMap;
    }

    private List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init(final ServletConfig config) throws ServletException {

        viewResolvers.add(new JspViewResolver("/", "jsp"));
        viewResolvers.add(new HtmlViewResolver("/", "html"));
        viewResolvers.add(new RedirectViewResolver());

        super.init(config);
    }


    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {

        String methodAndURL = buildKey(req);
        ControllerAndMethodMapping controllerAndMethodMapping = controllerMap.get(methodAndURL);
        if (controllerAndMethodMapping == null) {         //favicon.icon가 옴
            resp.sendError(404);
            return;
        }

        try {
            Object result = controllerAndMethodMapping.invoke(req, resp);

            if (result instanceof ModelAndView) {
                render(req, resp, (ModelAndView) result);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        for (ViewResolver viewResolver : viewResolvers) {
            View view = viewResolver.resolveViewName(modelAndView.getViewName());
            if (view != null) {
                view.render(modelAndView.getModel(), req, resp);
                return;
            }
        }
    }

    private String buildKey(HttpServletRequest request) {
        return String.format("%s:%s", request.getMethod(), request.getRequestURI());
    }
}