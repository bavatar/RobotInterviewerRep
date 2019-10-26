package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

public interface QandAsRepository extends CrudRepository<QsAndAs, Long> {
    QsAndAs findByJob(Job job);
//    HashSet<QsAndAs> findAllByJob(Job job);
}
