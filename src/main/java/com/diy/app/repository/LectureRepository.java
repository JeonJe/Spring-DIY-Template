package com.diy.app.repository;

import com.diy.app.domain.Lecture;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
