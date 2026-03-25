package com.diy.app;

import com.diy.app.controller.LectureController;
import com.diy.app.repository.LectureRepository;
import com.diy.app.service.LectureService;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        LectureRepository lectureRepository = new LectureRepository();
        LectureService lectureService = new LectureService(lectureRepository);
        LectureController lectureController = new LectureController(lectureService);

        Map<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put("/lectures", lectureController);


        TomcatWebServer tomcatWebServer = new TomcatWebServer(controllerMap);
        tomcatWebServer.start();
    }

}
