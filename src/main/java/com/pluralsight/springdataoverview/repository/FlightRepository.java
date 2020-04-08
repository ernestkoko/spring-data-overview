package com.pluralsight.springdataoverview.repository;

import com.pluralsight.springdataoverview.entity.Flight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlightRepository extends CrudRepository<Flight, Long> {

    List<Flight> findByOrigin(String london);  // SELECT * FROM flight WHERE  origin= ?  ...parameter

    // SELECT * FROM flight WHERE origin = ? AND destination = ?
    List<Flight> findByOriginAndDestination(String origin, String destination);

}
