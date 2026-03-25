package com.diy.framework.web.controller;

import com.diy.app.controller.LectureController;
import com.diy.app.repository.LectureRepository;
import com.diy.app.service.LectureService;
import com.diy.framework.web.view.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@WebServlet("/") // 톰캣이 자동으로 인스턴스 생성, 외부 파라미터 주입 불가 함
public class DispatcherServlet extends HttpServlet {
    private final Map<String, Controller> controllerMap;

    public DispatcherServlet(Map<String, Controller> controllerMap) {
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
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
//        final Map<String, ?> params = parseParams(req);

        String url = req.getRequestURI();
        Controller controller = controllerMap.get(url);
        if (controller == null) {         //favicon.icon가 옴
            resp.sendError(404);
            return;
        }

        try {
            ModelAndView modelAndView = controller.handleRequest(req, resp);
            render(req, resp, modelAndView);

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


//    private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
//        if ("application/json".equals(req.getHeader("Content-Type"))) {
//            final byte[] bodyBytes = req.getInputStream().readAllBytes();
//            final String body = new String(bodyBytes, StandardCharsets.UTF_8);
//
//            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {
//            });
//        } else {
//            return req.getParameterMap();
//        }
//    }
}