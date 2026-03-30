package com.diy.app;

import com.diy.app.controller.LectureController;
import com.diy.app.repository.LectureRepository;
import com.diy.app.service.LectureService;
import com.diy.framework.annotation.Autowired;
import com.diy.framework.annotation.Component;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.server.TomcatWebServer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        LectureRepository lectureRepository = null;
        LectureService lectureService = null;

        //빈 스캐너에서 lecture repository 하위 클래스를 찾는다
        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        lectureRepository = getLectureRepository(classes, lectureRepository);
        lectureService = getLectureService(classes, lectureRepository);

        LectureController lectureController = new LectureController(lectureService);

        Map<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put("/lectures", lectureController);


        TomcatWebServer tomcatWebServer = new TomcatWebServer(controllerMap);
        tomcatWebServer.start();
    }

    private static LectureService getLectureService(Set<Class<?>> classes,  LectureRepository lectureRepository) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        for (Class<?> aClass : classes) {
            Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
            for (Constructor<?> declaredConstructor : declaredConstructors) {
                if(declaredConstructor.isAnnotationPresent(Autowired.class)) {
                    Object instance = declaredConstructor.newInstance(lectureRepository);
                    if(instance instanceof LectureService) {
                        return  (LectureService) instance;
                    }
                }
            }
        }
        return null;
    }

    private static LectureRepository getLectureRepository(Set<Class<?>> classes, LectureRepository lectureRepository) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Class<?> aClass : classes) {

            //instance = com.diy.app.repository.LectureRepository@277c0f21
            Object instance = aClass.getDeclaredConstructor().newInstance();
            if (instance instanceof LectureRepository) {
                lectureRepository = (LectureRepository) instance;
            }
        }
        return lectureRepository;
    }

}
