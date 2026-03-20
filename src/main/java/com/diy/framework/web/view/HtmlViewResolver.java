package com.diy.framework.web.view;

public class HtmlViewResolver implements ViewResolver {

    private final String prefix;
    private final String suffix;

    public HtmlViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public View resolveViewName(String viewName) {
        return new HtmlView(this.prefix + viewName + this.suffix);
    }
}
