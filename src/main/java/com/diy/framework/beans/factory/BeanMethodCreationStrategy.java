package com.diy.framework.beans.factory;

import java.lang.reflect.Method;

public class BeanMethodCreationStrategy implements BeanCreationStrategy {

    @Override
    public boolean supports(BeanDefinition beanDefinition) {
        return beanDefinition.isFactoryMethodBean();
    }

    @Override
    public Object createBean(BeanDefinition beanefinition, BeanFactory beanFactory) throws Exception {
        //어떤 메소드로 빈을 만들건지 꺼냄
        Method factoryMethod = beanDefinition.getFactoryMethod();
        Object configInstance = beanFactory.getBean(beanDefinition.getDeclaringClass());

        //메서드가 필요로 하는 파라미터 타입 확인
        Class<?>[] paramTypes = factoryMethod.getParameterTypes();
        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            args[i] = beanFactory.getBean(paramTypes[i]);
        }

        //빈 생성 메서드 호출
        return factoryMethod.invoke(configInstance, args);
    }
}
