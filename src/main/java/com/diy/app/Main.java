package com.diy.app;

import com.diy.app.controller.LectureController;
import com.diy.framework.annotation.Component;
import com.diy.framework.beans.factory.BeanFactory;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        BeanFactory beanFactory = new BeanFactory();
        classes.forEach(beanFactory::registerBeanDefinitions);

        LectureController lectureController = beanFactory.getBean(LectureController.class);

        Map<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put("/lectures", lectureController);

        TomcatWebServer tomcatWebServer = new TomcatWebServer(controllerMap);
        tomcatWebServer.start();
    }
}
