package com.diy.framework.beans.factory;

public interface BeanCreationStrategy {
    boolean supports(BeanDefinition beanDefinition);
    Object createBean(BeanDefinition beanDefinition, BeanFactory beanFactory) throws Exception;
}
