package com.diy.app.repository;

import com.diy.app.domain.Lecture;
import com.diy.framework.annotation.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
public class LectureRepository {

    private final Map<Long, Lecture> repository = new HashMap<>();


    public void save(Lecture lecture) {
        repository.put(lecture.getId(), lecture);
    }

    public Lecture findById(Long id) {
        return repository.get(id);
    }

    public long size() {
        return repository.size();
    }

    public Collection<Lecture> findAll() {
        return repository.values();
    }

    public void remove(Long id) {
        repository.remove(id);
    }




}
