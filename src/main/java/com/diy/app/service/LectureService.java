package com.diy.app.service;

import com.diy.app.domain.Lecture;
import com.diy.app.repository.LectureRepository;
import com.diy.framework.annotation.Autowired;
import com.diy.framework.annotation.Component;

import java.util.Collection;
import java.util.NoSuchElementException;

@Component
public class LectureService {
    private final LectureRepository lectureRepository;

    @Autowired
    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Collection<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    public void save(Lecture lecture) {
        long id = lectureRepository.size();
        lecture.setId(id);
        lectureRepository.save(lecture);
    }

    public void update(Lecture lecture) {
        if (lecture.getId() == null) {
            throw new IllegalArgumentException("lecture id is null");
        }
        Lecture target = lectureRepository.findById(lecture.getId());
        if (target == null) {
            throw new NoSuchElementException("No such element");
        }
        lectureRepository.save(lecture);
    }

    public void delete(Long id) {
        Lecture target = lectureRepository.findById(id);
        if (target == null) {
            throw new NoSuchElementException("No such element");
        }
        lectureRepository.remove(id);
    }
}
