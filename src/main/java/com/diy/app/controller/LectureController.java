package com.diy.app.controller;

import com.diy.app.domain.Lecture;
import com.diy.app.service.LectureService;
import com.diy.framework.annotation.*;
import com.diy.framework.web.model.Model;
import com.diy.framework.web.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;


@Component
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("")
    public ModelAndView getLectures() {
        Collection<Lecture> lectures = lectureService.findAll();

        Model model = new Model();
        model.addAttribute("lectures", lectures);
        return new ModelAndView("lecture-list", model);
    }

    @PostMapping("")
    public void createLecture(HttpServletRequest request) throws IOException {
        Lecture lecture = extractLecture(request);
        lectureService.save(lecture);
    }

    @PutMapping("")
    public void updateLecture(HttpServletRequest request) throws IOException {
        Lecture lecture = extractLecture(request);
        lectureService.update(lecture);
    }

    @DeleteMapping("")
    public void deleteLecture(HttpServletRequest request) throws IOException {
        Lecture lecture = extractLecture(request);
        lectureService.delete(lecture.getId());
    }


    private static Lecture extractLecture(HttpServletRequest request) throws IOException {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        return new ObjectMapper().readValue(body, Lecture.class);
    }

}
