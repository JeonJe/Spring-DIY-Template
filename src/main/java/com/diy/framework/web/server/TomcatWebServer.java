package com.diy.framework.web.server;

import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.controller.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.Map;


public class TomcatWebServer {

    private final Tomcat tomcat = new Tomcat();
    private final int port = 8085;

    private final Map<String, Controller> controllerMap;

    public TomcatWebServer(Map<String, Controller> controllerMap) {
        this.controllerMap = controllerMap;
    }

    public void start() {
        setServerContext();
        startDaemonAwaitThread();
        startServerInternal();
    }

    public void startServerInternal() {
        try {
            tomcat.setPort(port);
            tomcat.start();
            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.start();
        } catch (LifecycleException e) {
            throw new RuntimeException("톰켓 서버 실행 중 예외가 발생했습니다.", e);
        }
    }

    private void setServerContext() {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        final Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        //디스패처 서블릿 추가
        DispatcherServlet dispatcherServlet = new DispatcherServlet(controllerMap);
        Tomcat.addServlet((context), "dispatcherServlet", dispatcherServlet);
        context.addServletMappingDecoded("/", "dispatcherServlet");

        setServerResources(context);
    }

    private void setServerResources(final Context context) {
        final String classPath = getClassPath();

        final StandardRoot resources = new StandardRoot(context);
        resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", classPath, "/"));

        context.setResources(resources);
    }

    private String getClassPath() {
        try {
            final CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();

            return new File(codeSource.getLocation().toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void startDaemonAwaitThread() {
        final Thread awaitThread = new Thread(() -> TomcatWebServer.this.tomcat.getServer().await());
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
