package com.diy.framework.web.model;

import java.util.HashMap;
import java.util.Map;

// controller가 HttpRequest에 의존하지 않기 위해 사용
public class Model {
    private final Map<String, Object> map = new HashMap<>();

    public void addAttribute(String name, Object value) {
        map.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return map;
    }
}
