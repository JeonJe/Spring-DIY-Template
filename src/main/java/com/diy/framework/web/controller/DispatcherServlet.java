package com.diy.framework.web.controller;

import com.diy.framework.web.controller.handler.adapter.HandlerAdapter;
import com.diy.framework.web.controller.handler.mapping.HandlerMapping;
import com.diy.framework.web.view.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@WebServlet("/") // 톰캣이 자동으로 인스턴스 생성, 외부 파라미터 주입 불가 함
public class DispatcherServlet extends HttpServlet {

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    public DispatcherServlet(List<HandlerMapping> handlerMappings, List<HandlerAdapter> handlerAdapters) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {

        viewResolvers.add(new JspViewResolver("/", "jsp"));
        viewResolvers.add(new HtmlViewResolver("/", "html"));
        viewResolvers.add(new RedirectViewResolver());

        super.init(config);
    }


    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {

        try {
            Object handler = getHandler(req);
            if (handler == null) {
                resp.sendError(404);
                return;
            }

            HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(handler, req, resp);
            if (modelAndView != null) {
                render(req, resp, modelAndView);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private Object getHandler(HttpServletRequest req) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 핸들러: " + handler));
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

}
