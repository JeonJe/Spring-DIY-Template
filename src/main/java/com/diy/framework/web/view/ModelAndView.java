package com.diy.framework.web.view;

import com.diy.framework.web.model.Model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private final String viewName;
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(final String viewName, final Model model) {
        this.viewName = viewName;
        this.model.putAll(model.getAttributes());
    }

    public ModelAndView(final String viewName, final Map<String, Object> model) {
        this.viewName = viewName;
        this.model.putAll(model);
    }


    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(this.model);
    }

}
