package com.diy.app;

import com.diy.framework.annotation.Component;
import com.diy.framework.annotation.RequestMapping;
import com.diy.framework.beans.factory.BeanFactory;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.web.controller.ControllerHandler;
import com.diy.framework.web.controller.DispatcherServlet;
import com.diy.framework.web.controller.HTTPMethodResolver;
import com.diy.framework.web.controller.handler.HandlerMethod;
import com.diy.framework.web.controller.handler.adapter.AnnotationBasedHandlerAdapter;
import com.diy.framework.web.controller.handler.adapter.HandlerAdapter;
import com.diy.framework.web.controller.handler.adapter.InterfaceBasedHandlerAdapter;
import com.diy.framework.web.controller.handler.mapping.AnnotationBasedHandlerMapping;
import com.diy.framework.web.controller.handler.mapping.HandlerMapping;
import com.diy.framework.web.controller.handler.mapping.InterfaceBasedHandlerMapping;
import com.diy.framework.web.server.TomcatWebServer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    private static final HTTPMethodResolver httpMethodResolver = new HTTPMethodResolver();

    public static void main(String[] args) {
        BeanScanner beanScanner = new BeanScanner("com.diy.app");
        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        BeanFactory beanFactory = new BeanFactory();
        classes.forEach(beanFactory::registerBeanDefinitions);

        Map<String, HandlerMethod> controllerAndMethodMappingMap = createControllerAndMethodMappingMap(beanFactory, classes);
        Map<String, ControllerHandler> controllerMap = createControllerMap(beanFactory, classes);
        List<HandlerMapping> handlerMappings = getHandlerMappings(controllerAndMethodMappingMap, controllerMap);
        List<HandlerAdapter> handlerAdapters = getHandlerAdapters();
        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters);

        TomcatWebServer tomcatWebServer = new TomcatWebServer(dispatcherServlet);
        tomcatWebServer.start();
    }

    @NotNull
    private static List<HandlerMapping> getHandlerMappings(Map<String, HandlerMethod> controllerAndMethodMappingMap, Map<String, ControllerHandler> controllerMap) {
        return List.of(
                new AnnotationBasedHandlerMapping(controllerAndMethodMappingMap),
                new InterfaceBasedHandlerMapping(controllerMap)
        );
    }

    private static List<HandlerAdapter> getHandlerAdapters() {
        return List.of(
                new AnnotationBasedHandlerAdapter(),
                new InterfaceBasedHandlerAdapter()
        );
    }

    private static Map<String, HandlerMethod> createControllerAndMethodMappingMap(BeanFactory beanFactory, Set<Class<?>> classes) {
        Map<String, HandlerMethod> controllerAndMethodMappingMap = new HashMap<>();
        for (Class<?> beanClass : classes) {
            RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }
            String requestUrl = requestMapping.value();
            Object controller = beanFactory.getBean(beanClass);
            Map<String, HandlerMethod> ControllerAndMethodWithUrl = httpMethodResolver.resolve(requestUrl, controller);
            controllerAndMethodMappingMap.putAll(ControllerAndMethodWithUrl);
        }
        return controllerAndMethodMappingMap;
    }

    // 인터페이스의 구현체로 만드는 버전
    private static Map<String, ControllerHandler> createControllerMap(BeanFactory beanFactory, Set<Class<?>> classes) {
        Map<String, ControllerHandler> controllerMap = new HashMap<>();
        for (Class<?> beanClass : classes) {
            if (!ControllerHandler.class.isAssignableFrom(beanClass)) {
                continue;
            }

            RequestMapping requestMapping = beanClass.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }

            ControllerHandler controller = (ControllerHandler) beanFactory.getBean(beanClass);
            //url이랑 컨트롤러 맵핑
            controllerMap.put(requestMapping.value(), controller);
        }
        return controllerMap;
    }
}
