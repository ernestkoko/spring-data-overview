package com.pluralsight.springdataoverview.repository;

import com.pluralsight.springdataoverview.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

//Paging and sorting helps share the db into different sets/pages for queries not to overload the app
//PagingAndSortingRepository takes Flight and the Flight id type(String)

public interface FlightRepository extends PagingAndSortingRepository<Flight, String>, DeleteByOriginRepository {

    List<Flight> findByOrigin(String origin);  // SELECT * FROM flight WHERE  origin= ?  ...parameter

    // SELECT * FROM flight WHERE origin = ? AND destination = ?
    List<Flight> findByOriginAndDestination(String origin, String destination);

    //SELECT * FROM flight WHERE origin IN (?)
    List<Flight> findByOriginIn(String ... origin);

   //SELECT * FROM flight WHERE upper(origin) = UPPER(?)
    List<Flight> findByOriginIgnoreCase(String origin);

    Page<Flight> findByOrigin(String origin, Pageable pageRequest);


}
