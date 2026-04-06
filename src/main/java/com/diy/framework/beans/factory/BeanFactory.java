package com.diy.framework.beans.factory;

import com.diy.framework.annotation.Bean;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {

    private final Map<Class<?>, BeanDefinition> beanDefinitions = new LinkedHashMap<>();
    private final Map<String, BeanDefinition> beanDefinitionsByName = new LinkedHashMap<>();
    private final Map<Class<?>, Object> beanInstances = new HashMap<>();

    private final List<BeanCreationStrategy> strategies = List.of(
            new BeanMethodCreationStrategy(),
            new ComponentCreationStrategy()
    );

    public <T> T getBean(Class<T> beanClass) {
        if (beanClass == null) {
            throw new NullPointerException("beanClass is null");
        }

        BeanDefinition beanDefinition = findBeanDefinitionByType(beanClass);
        if (beanDefinition == null) {
            throw new RuntimeException("등록된 빈 정의를 찾을 수 없습니다: " + beanClass.getName());
        }
        return beanClass.cast(getOrCreateBeanInstance(beanDefinition));
    }

    //이름으로 빈 조회
    public <T> T getBean(String name) {
        BeanDefinition beanDefinition = findBeanDefinitionByName(name);
        if (beanDefinition == null) {
            return null;
        }
        return (T) getOrCreateBeanInstance(beanDefinition);
    }

    public void registerBeanDefinitions(Class<?> beanClass) {
        registerComponentDefinition(beanClass);
        registerBeanMethodDefinitions(beanClass);
    }

    public Set<Class<?>> getBeanClasses() {
        return Collections.unmodifiableSet(beanDefinitions.keySet());
    }

    private Object getOrCreateBeanInstance(BeanDefinition beanDefinition) {
        Class<?> beanType = beanDefinition.getBeanType();
        //싱글톤
        if (beanInstances.containsKey(beanType)) {
            return beanInstances.get(beanType);
        }

        Object newInstance = createBeanInstance(beanDefinition);
        beanInstances.put(beanType, newInstance);
        return newInstance;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        try {
            for (BeanCreationStrategy strategy : strategies) {
                if (strategy.supports(beanDefinition)) {
                    return strategy.createBean(beanDefinition, this);
                }
            }
            throw new RuntimeException("빈 생성 전략을 찾을 수 없습니다: " + beanDefinition.getBeanType().getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BeanDefinition findBeanDefinitionByType(Class<?> beanClass) {
        BeanDefinition beanDefinition = beanDefinitions.get(beanClass);
        if (beanDefinition != null) {
            return beanDefinition;
        }

        for (Map.Entry<Class<?>, BeanDefinition> entry : beanDefinitions.entrySet()) {
            //인터페이스/부모 타입으로 조회 -> 등록된 구현체 정의 확인
            if (beanClass.isAssignableFrom(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private BeanDefinition findBeanDefinitionByName(String name) {
        return beanDefinitionsByName.get(name);
    }

    private void registerComponentDefinition(Class<?> beanClass) {
        saveBeanDefinition(BeanDefinition.createDefinitionForComponent(beanClass));
    }

    private void registerBeanMethodDefinitions(Class<?> beanClass) {
        for (Method method : beanClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Bean.class)) {
                saveBeanDefinition(BeanDefinition.createDefinitionForBeanMethod(beanClass, method));
            }
        }
    }

    private void saveBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitions.put(beanDefinition.getBeanType(), beanDefinition);
        beanDefinitionsByName.put(beanDefinition.getName(), beanDefinition);
    }
}
