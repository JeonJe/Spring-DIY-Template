package com.diy.app;

import com.diy.framework.annotation.Component;
import com.diy.framework.annotation.RequestMapping;
import com.diy.framework.beans.factory.BeanFactory;
import com.diy.framework.beans.factory.BeanScanner;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.controller.DispatcherServlet;
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

        Map<String, Controller> controllerMap = createControllerMap(beanFactory, classes);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerMap);

        TomcatWebServer tomcatWebServer = new TomcatWebServer(dispatcherServlet);
        tomcatWebServer.start();
    }

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
