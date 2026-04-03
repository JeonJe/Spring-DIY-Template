package com.diy.framework.beans.factory;

public interface BeanCreationStrategy {
    boolean supports(Class<?> beanClass, BeanFactory beanFactory);
    Object createBean(Class<?> beanClass, BeanFactory beanFactory) throws Exception;
}
