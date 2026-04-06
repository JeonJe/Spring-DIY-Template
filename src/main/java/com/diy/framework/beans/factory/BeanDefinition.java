package com.diy.framework.beans.factory;

import com.diy.framework.annotation.Bean;

import java.beans.Introspector;
import java.lang.reflect.Method;

public class BeanDefinition {
    private final String name;
    private final Class<?> beanType;
    private final Class<?> declaringClass; //빈을 만드는 클래스
    private final Method factoryMethod; // 빈 만드는 클래스 안에서 빈 생성 메서드

    private BeanDefinition(String name, Class<?> beanType, Class<?> declaringClass, Method factoryMethod) {
        this.name = name;
        this.beanType = beanType;
        this.declaringClass = declaringClass;
        this.factoryMethod = factoryMethod;
    }

    public static BeanDefinition createDefinitionForComponent(Class<?> beanClass) {
        return new BeanDefinition(defaultName(beanClass), beanClass, beanClass, null);
    }

    public static BeanDefinition createDefinitionForBeanMethod(Class<?> declaringClass, Method factoryMethod) {
        Bean bean = factoryMethod.getAnnotation(Bean.class);
        String beanName = bean.name().isBlank() ? factoryMethod.getName() : bean.name();
        factoryMethod.setAccessible(true);
        return new BeanDefinition(beanName, factoryMethod.getReturnType(), declaringClass, factoryMethod);
    }

    public String getName() {
        return name;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    public Method getFactoryMethod() {
        return factoryMethod;
    }

    public boolean isFactoryMethodBean() {
        return factoryMethod != null;
    }

    private static String defaultName(Class<?> beanClass) {
        return Introspector.decapitalize(beanClass.getSimpleName());
    }
}
