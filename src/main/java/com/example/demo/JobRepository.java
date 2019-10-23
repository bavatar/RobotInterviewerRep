package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface JobRepository extends CrudRepository<Job,Long> {

    Collection<Job> findJobByTitleContainingIgnoreCase(String title);
    Job findByUserId(long id);
//    Job findByUser(User user);

//    ArrayList<Job> findAll();
//    Job findByAuthor_id(long id);
}

