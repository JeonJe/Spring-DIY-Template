package com.diy.framework.beans.factory;

import com.diy.framework.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanFactory {


    //빈등록
    private final Set<Class<?>> beanClasses = new HashSet<>();

    //객체 저장소
    private final Map<Class<?>, Object> beanInstances = new HashMap<>();

    public BeanFactory() {
    }

    public <T> T getBean(Class<T> beanClass) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (beanClass == null) {
            throw new NullPointerException("beanClass is null");
        }

        if(beanClass.isInterface()) {
            for (Class<?> aClass : beanClasses) {
                if(beanClass.isAssignableFrom(aClass)) {
                    beanClass = (Class<T>) aClass;
                    break;
                }
            }
        }

        //객체 있음?
        Object beanInstance = beanInstances.get(beanClass);
        if (beanInstance != null) {
            return (T) beanInstance;
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

        //생성자들의 파라미터도 만들어줘야함
        Class<?>[] parameterTypes = selected.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            args[i] = getBean(parameterTypes[i]);
        }

        //인스턴스 생성
        Object newInstance = selected.newInstance(args);

        //만들어진 객체 저장
        beanInstances.put(beanClass, newInstance);

        return (T) newInstance;


    }

    public void registerBean(Class<?> beanClass) {
        beanClasses.add(beanClass);
    }


}
