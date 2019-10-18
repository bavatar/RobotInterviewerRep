package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface JobRepository extends CrudRepository<Job,Long> {
    //    ArrayList<Flight> f.(String endAirport);    // Search results
    //ArrayList<Flight> flightArrayList = new ArrayList<>();
//        ArrayList<Job> findFlightByEndAirportContainingIgnoreCase(String endAirport);    // Search results
//        ArrayList<Flight> findFlightByDate(Date date);
//        ArrayList<Flight> findFlightByPriceContaining(String price);
//        ArrayList<Flight> findFlightByAirlineContainingIgnoreCase(String airline);
//        ArrayList<Flight> findFlightByDateAndEndAirportIgnoreCase(Date date, String dest);
    ArrayList<Job> findJobByTitleContainingIgnoreCase(String title);
    Job findByUser(User user);

    ArrayList<Job> findAll();
//    Job findByAuthor_id(long id);
}

