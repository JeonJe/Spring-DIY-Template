package com.diy.app;

import com.diy.app.servlet.HomeServlet;
import com.diy.app.servlet.LectureServlet;
import com.diy.framework.web.server.TomcatWebServer;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.CodeSource;

public class Main {
    public static void main(String[] args) {

        //todo 이걸로 사용
        TomcatWebServer tomcatWebServer = new TomcatWebServer();

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8085);


        try {
            final Context context = setServerContext(tomcat);
            Tomcat.addServlet(context, "homeServlet", new HomeServlet());
            context.addServletMappingDecoded("/home", "homeServlet");

            Tomcat.addServlet(context, "lectureServlet", new LectureServlet());
            context.addServletMappingDecoded("/lectures", "lectureServlet");

            tomcat.start();
            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.start();

            System.out.println("서버 실행");
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }

    }

    private static Context setServerContext(final Tomcat tomcat) {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        final Context context = tomcat.addWebapp("/", absoluteResourcesPath);

        setServerResources(context);
        return context;
    }

    private static void setServerResources(final Context context) {
        final String classPath = getClassPath();

        final StandardRoot resources = new StandardRoot(context);
        resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", classPath, "/"));

        context.setResources(resources);
    }

    private static String getClassPath() {
        try {
            final CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();

            return new File(codeSource.getLocation().toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
