package com.diy.framework.web.controller;

import com.diy.framework.annotation.DeleteMapping;
import com.diy.framework.annotation.GetMapping;
import com.diy.framework.annotation.PostMapping;
import com.diy.framework.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HTTPMethodResolver {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public Map<String, ControllerAndMethodMapping> resolve(String baseUrl, Object controller) {
        Map<String, ControllerAndMethodMapping> map = new HashMap<>();

        //이 컨트롤러 클래스의 메소드들
        Method[] declaredMethods = controller.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            doGetMapping(baseUrl, controller, declaredMethod, map);
            doPostMapping(baseUrl, controller, declaredMethod, map);
            doPutMapping(baseUrl, controller, declaredMethod, map);
            doDeleteMapping(baseUrl, controller, declaredMethod, map);
        }
        return map;
    }


    //GET:/lectures -> ControllerAndMethodMapping(controller, method)
    private void doGetMapping(String baseUrl, Object controller, Method declaredMethod, Map<String, ControllerAndMethodMapping> map) {
        if (!declaredMethod.isAnnotationPresent(GetMapping.class)) {
            return;

        }
        GetMapping getMapping = declaredMethod.getAnnotation(GetMapping.class);
        String path = getMapping.value();
        String url = baseUrl + path;
        map.put(buildKey(GET, url), new ControllerAndMethodMapping(controller, declaredMethod));
    }

    private void doPostMapping(String baseUrl, Object controller, Method declaredMethod, Map<String, ControllerAndMethodMapping> map) {
        if (!declaredMethod.isAnnotationPresent(PostMapping.class)) {
            return;
        }
        PostMapping postMapping = declaredMethod.getAnnotation(PostMapping.class);
        String path = postMapping.value();
        String url = baseUrl + path;
        map.put(buildKey(POST, url), new ControllerAndMethodMapping(controller, declaredMethod));
    }

    private void doPutMapping(String baseUrl, Object controller, Method declaredMethod, Map<String, ControllerAndMethodMapping> map) {
        if (!declaredMethod.isAnnotationPresent(PutMapping.class)) {
            return;
        }
        PutMapping putMapping = declaredMethod.getAnnotation(PutMapping.class);
        String path = putMapping.value();
        String url = baseUrl + path;
        map.put(buildKey(PUT, url), new ControllerAndMethodMapping(controller, declaredMethod));
    }

    private void doDeleteMapping(String baseUrl, Object controller, Method declaredMethod, Map<String, ControllerAndMethodMapping> map) {
        if (!declaredMethod.isAnnotationPresent(DeleteMapping.class)) {
            return;
        }
        DeleteMapping deleteMapping = declaredMethod.getAnnotation(DeleteMapping.class);
        String path = deleteMapping.value();
        String url = baseUrl + path;
        map.put(buildKey(DELETE, url), new ControllerAndMethodMapping(controller, declaredMethod));
    }

    private String buildKey(String method, String path) {
        return String.format("%s:%s", method, path);
    }
}
