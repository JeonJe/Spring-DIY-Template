package com.diy.framework.beans.factory;

import java.util.*;

public class BeanFactory {

    //빈등록
    private final Set<Class<?>> beanClasses = new HashSet<>();

    //객체 저장소 (타입 기반)
    private final Map<Class<?>, Object> beanInstances = new HashMap<>();
    private final Map<String, Object> beanByName = new HashMap<>();


    private final List<BeanCreationStrategy> strategies = List.of(
            new BeanMethodCreationStrategy(), //빈이 우선?
            new ComponentCreationStrategy()
    );

    public BeanFactory() {
    }

    public <T> T getBean(Class<T> beanClass) {
        if (beanClass == null) {
            throw new NullPointerException("beanClass is null");
        }

        //구현체 만들기
        if (beanClass.isInterface()) {
            for (Class<?> aClass : beanClasses) {
                if (beanClass.isAssignableFrom(aClass)) {
                    beanClass = (Class<T>) aClass;
                    break;
                }
            }
        }

        //객체 있음?
        if (beanInstances.containsKey(beanClass)) {
            return (T) beanInstances.get(beanClass);
        }

        try {
            //@Bean 없으면 @Component ??
            for (BeanCreationStrategy strategy : strategies) {
                if (strategy.supports(beanClass, this)) {
                    Object newInstance = strategy.createBean(beanClass, this);
                    beanInstances.put(beanClass, newInstance);
                    return (T) newInstance;
                }
            }
            throw new RuntimeException("빈 생성 전략을 찾을 수 없습니다: " + beanClass.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getBean(String name) {
        return (T) beanByName.get(name);
    }

    public void registerBean(Class<?> beanClass) {
        beanClasses.add(beanClass);
    }

    public Set<Class<?>> getBeanClasses() {
        return Collections.unmodifiableSet(beanClasses);
    }
}
