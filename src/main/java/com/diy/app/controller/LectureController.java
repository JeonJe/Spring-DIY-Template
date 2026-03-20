package com.diy.app.controller;

import com.diy.app.domain.Lecture;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.model.Model;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class LectureController implements Controller {
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";
    public static final String POST = "POST";
    public static final String GET = "GET";
    private final Map<Long, Lecture> lectureRepository = new HashMap<>();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();

        if (method.equals(GET)) {
            return doGet();
        }
        if (method.equals(POST)) {
            return doPost(request);

        }
        if (method.equals(PUT)) {
            return doPut(request);
        }
        if (method.equals(DELETE)) {
            return doDelete(request);
        }

        return null;
    }

    @NotNull
    private ModelAndView doGet() {
        final Collection<Lecture> lectures = lectureRepository.values();

        Model model = new Model();
        model.addAttribute("lectures", lectures);

//            final JspView jspView = new JspView("lecture-list.jsp");
//            jspView.render(model, request, response);
        return new ModelAndView("lecture-list", model);
    }


    @Nullable
    private ModelAndView doPost(HttpServletRequest request) throws IOException {
        Lecture lecture = extractLecture(request);

        final long id = lectureRepository.size();
        lecture.setId(id);
        lectureRepository.put(id, lecture);
        return null;
    }

    @Nullable
    private ModelAndView doDelete(HttpServletRequest request) throws IOException {
        Lecture lecture = extractLecture(request);
        if (lecture == null || lecture.getId() == null) {
            throw new IllegalArgumentException("lecture is null or seq is null");
        }

        Lecture target = lectureRepository.get(lecture.getId());
        if (target == null) {
            throw new NoSuchElementException("No such element");
        }

        lectureRepository.remove(lecture.getId());
        return null;
    }

    @Nullable
    private ModelAndView doPut(HttpServletRequest request) throws IOException {
        Lecture lecture = extractLecture(request);

        if (lecture == null || lecture.getId() == null) {
            throw new IllegalArgumentException("lecture is null or seq is null");
        }

        Lecture target = lectureRepository.get(lecture.getId());
        if (target == null) {
            throw new NoSuchElementException("No such element");
        }

        lectureRepository.put(lecture.getId(), lecture);
        return null;
    }


    private static Lecture extractLecture(HttpServletRequest request) throws IOException {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        return new ObjectMapper().readValue(body, Lecture.class);
    }


}
