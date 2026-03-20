package com.diy.framework.web.view;

public class JspViewResolver implements ViewResolver {
    private final String prefix;
    private final String suffix;

    public JspViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public View resolveViewName(String viewName) {
        return new JspView(this.prefix + viewName + this.suffix);
    }
}
