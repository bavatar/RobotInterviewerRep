package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("jobService")
public class JobServiceImpl implements JobService{
    @Autowired
    JobRepository jobRepository;

    @Override
    public Job update(Job job) {
        return jobRepository.save(job);
    }
}
