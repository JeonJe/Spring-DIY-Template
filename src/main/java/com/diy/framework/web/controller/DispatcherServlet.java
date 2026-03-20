package com.diy.framework.web.controller;

import com.diy.app.controller.LectureController;
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

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private Map<String, Controller> controllerMap = new HashMap<>();
    private List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init(final ServletConfig config) throws ServletException {
        controllerMap.put("/lectures", new LectureController());

        viewResolvers.add(new JspViewResolver("/", "jsp"));
        viewResolvers.add(new HtmlViewResolver("/", "html"));
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
            // 강의 등록 로직
            // 강의 목록 로직
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