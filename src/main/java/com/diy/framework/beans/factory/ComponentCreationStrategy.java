package com.diy.framework.beans.factory;

import com.diy.framework.annotation.Autowired;

import java.lang.reflect.Constructor;

public class ComponentCreationStrategy implements BeanCreationStrategy {

    @Override
    public boolean supports(BeanDefinition beanDefinition) {
        return !beanDefinition.isFactoryMethodBean();
    }

    @Override
    public Object createBean(BeanDefinition beanDefinition, BeanFactory beanFactory) throws Exception {
        Class<?> beanClass = beanDefinition.getBeanType();
        if (beanClass == null) {
            throw new NullPointerException("beanClass is null");
        }

        //생성자 선택
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
        Constructor<?> selected = null;

        for (Constructor<?> constructor : constructors) {
            //autowired있으면 그거 씀
            if (constructor.isAnnotationPresent(Autowired.class)) {
                constructor.setAccessible(true);
                selected = constructor;
            }
        }

        if (selected == null) {
            selected = constructors[0];
        }
        selected.setAccessible(true);

        //생성자들의 파라미터도 만들어줘야함
        Class<?>[] parameterTypes = selected.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = beanFactory.getBean(parameterTypes[i]);  // BeanFactory에 위임
        }

        //인스턴스 생성
        return selected.newInstance(args);
    }
}
