package com.diy.app;

import com.diy.framework.annotation.Component;
import com.diy.framework.annotation.RequestMapping;
import com.diy.framework.beans.factory.BeanFactory;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.controller.ControllerAndMethodMapping;
import com.diy.framework.web.controller.DispatcherServlet;
import com.diy.framework.web.controller.HTTPMethodResolver;
import com.diy.framework.web.server.TomcatWebServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    private static final HTTPMethodResolver httpMethodResolver = new HTTPMethodResolver();

    public static void main(String[] args) {
        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        BeanFactory beanFactory = new BeanFactory();
        classes.forEach(beanFactory::registerBeanDefinitions);

        Map<String, ControllerAndMethodMapping> controllerAndMethodMappingMap = createControllerAndMethodMappingMap(beanFactory, classes);
        Map<String, Controller> controllerMap = createControllerMap(beanFactory, classes);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerAndMethodMappingMap, controllerMap);

        TomcatWebServer tomcatWebServer = new TomcatWebServer(dispatcherServlet);
        tomcatWebServer.start();
    }

    private static Map<String, ControllerAndMethodMapping> createControllerAndMethodMappingMap(BeanFactory beanFactory, Set<Class<?>> classes) {
        Map<String, ControllerAndMethodMapping> controllerAndMethodMappingMap = new HashMap<>();
        for (Class<?> beanClass : classes) {
            RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }
            String requestUrl = requestMapping.value();
            Object controller = beanFactory.getBean(beanClass);
            Map<String, ControllerAndMethodMapping> ControllerAndMethodWithUrl = httpMethodResolver.resolve(requestUrl, controller);
            controllerAndMethodMappingMap.putAll(ControllerAndMethodWithUrl);
        }
        return controllerAndMethodMappingMap;
    }

    // 인터페이스의 구현체로 만드는 버전
    private static Map<String, Controller> createControllerMap(BeanFactory beanFactory, Set<Class<?>> classes) {
        Map<String, Controller> controllerMap = new HashMap<>();
        for (Class<?> beanClass : classes) {
            if (!Controller.class.isAssignableFrom(beanClass)) {
                continue;
            }

            RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            Controller controller = (Controller) beanFactory.getBean(beanClass);
            //url이랑 컨트롤러 맵핑
            controllerMap.put(requestMapping.value(), controller);
        }
        return controllerMap;
    }
}
