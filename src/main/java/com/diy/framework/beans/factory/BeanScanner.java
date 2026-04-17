package com.diy.framework.beans.factory;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 특정 패키지 하위에 있는 클래스 조회 
public class BeanScanner {
    private final Reflections reflections;

    public BeanScanner(final String... basePackages) {
        reflections = new Reflections((Object) basePackages);
    }

    public Set<Class<?>> scanClassesTypeAnnotatedWith(final Class<? extends Annotation> annotation) {
        Set<Class<? extends Annotation>> annotationTypes = new HashSet<>();
        annotationTypes.add(annotation);

        reflections.getTypesAnnotatedWith(annotation)
                .stream()
                .filter(Class::isAnnotation)
                .map(c -> (Class<? extends Annotation>) c)
                .forEach(annotationTypes::add);

        //어노테이션 붙은 애들 대상으로 클래스 뽑아내버리기 @Controller만 붙은 클래스 찾기
        Set<Class<?>> annotatedClasses = new HashSet<>();
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            List<Class<?>> classes = reflections.getTypesAnnotatedWith(annotationType).stream()
                    .filter(t -> !t.isAnnotation() && !t.isInterface())
                    .toList();
            annotatedClasses.addAll(classes);
        }
        return annotatedClasses;
    }
}
