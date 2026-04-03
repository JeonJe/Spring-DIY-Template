package com.diy.framework.beans.factory;

import com.diy.framework.annotation.Bean;

import java.lang.reflect.Method;

public class BeanMethodCreationStrategy implements BeanCreationStrategy {

    @Override
    public boolean supports(Class<?> beanClass, BeanFactory beanFactory) {
        // 등록된 클래스 중 @Bean 메서드의 반환타입이 beanClass인 게 있는지 확인
        for (Class<?> aClass : beanFactory.getBeanClasses()) {
            for (Method method : aClass.getMethods()) {
                if (method.isAnnotationPresent(Bean.class) && method.getReturnType().equals(beanClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object createBean(Class<?> beanClass, BeanFactory beanFactory) throws Exception {
        // 1. beanFactory에 등록된 모든 클래스 순회
        for (Class<?> aClass : beanFactory.getBeanClasses()) {
            for (Method method : aClass.getMethods()) {
                // 2. 각 클래스의 메서드 중 @Bean이 붙어 있고 반환타입이 beanClass인 메서드 탐색
                if (method.isAnnotationPresent(Bean.class) && method.getReturnType().equals(beanClass)) {
                    // 3. 해당 메서드를 가진 클래스의 인스턴스를 beanFactory.getBean()으로 가져옴
                    Object configInstance = beanFactory.getBean(aClass);

                    // 4. @Bean 메서드의 파라미터도 beanFactory에서 주입
                    Class<?>[] paramTypes = method.getParameterTypes();
                    Object[] args = new Object[paramTypes.length];
                    for (int i = 0; i < paramTypes.length; i++) {
                        args[i] = beanFactory.getBean(paramTypes[i]);
                    }

                    // 5. method.invoke(instance, args) 로 빈 생성 후 반환
                    return method.invoke(configInstance, args);
                }
            }
        }
        return null;
    }


}
