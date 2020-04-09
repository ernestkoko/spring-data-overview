package com.pluralsight.springdataoverview;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PaginAndSortingTests {

    @Autowired
    private FlightRepository flightRepository;

    @Before
    public void setUp(){
        flightRepository.deleteAll();
    }

    @Test
    public void shouldSortFlightsByDestination(){
        final  Flight madrid = createFlight("Madrid");
        final  Flight london = createFlight("London");
        final  Flight paris = createFlight("Paris");

        flightRepository.save(madrid);
        flightRepository.save(london);
        flightRepository.save(paris);

        //sort query. This finds al flights by destination in ascending order
        final  Iterable<Flight> flights = flightRepository.findAll(Sort.by("destination"));

        Assertions.assertThat(flights).hasSize(3); //checking if the total number of flights is 3

        //iterating though the flights
        final Iterator<Flight> iterator = flights.iterator();

        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("London");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Madrid");
        Assertions.assertThat(iterator.next().getDestination()).isEqualTo("Paris");




    }

    //2nd test

    private Flight createFlight(String destination) {
        Flight flight =  new Flight();
        flight.setDestination(destination);
        flight.setOrigin("London");
        flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));

        return flight;
    }
}
