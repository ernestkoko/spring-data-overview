package com.pluralsight.springdataoverview;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class DerivedQueryTests {

    @Autowired
    private FlightRepository flightRepository;

    @Before
    public void setUp(){
        flightRepository.deleteAll();
    }

    @Test
    public void shouldFindFlightsFromLondon(){
        Flight flight1 = createFlight("London");
        Flight flight2 = createFlight("London");
        Flight flight3 = createFlight("New York");

        //saving the flights
        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        List<Flight> flightsToLondon = flightRepository.findByOrigin("London");

        Assertions.assertThat(flightsToLondon).hasSize(2);

        Assertions.assertThat(flightsToLondon.get(0)).isEqualToComparingFieldByField(flight1);
        Assertions.assertThat(flightsToLondon.get(1)).isEqualToComparingFieldByField(flight2);

    }

    @Test
    public void shouldFindFlightsFromLondonToParis(){
        final Flight flight1 = createFlight("London", "Paris");
        final Flight flight2 = createFlight("London", "New York");
        final Flight flight3 = createFlight("Madrid", "Paris");


        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

       //derived query name
        final List<Flight>  londonToParis = flightRepository
                .findByOriginAndDestination("London", "Paris");


        Assertions.assertThat(londonToParis)
                .hasSize(1)
                .first()
                 .isEqualToComparingFieldByField(flight1);
    }

    @Test
    public void shouldFindFlightFromLondonOrMadrid(){
        Flight flight1 = createFlight("London", "New York");
        Flight flight2 = createFlight("Tokyo", "New York");
        Flight flight3 = createFlight("Madrid", "New York");

        flightRepository.save(flight1);
        flightRepository.save(flight2);
        flightRepository.save(flight3);

        //derived query name
        List<Flight> londonOrMadrid = flightRepository
                .findByOriginIn("London", "Madrid");

        Assertions.assertThat(londonOrMadrid).hasSize(2);
        Assertions.assertThat(londonOrMadrid.get(0)).isEqualToComparingFieldByField(flight1);
        Assertions.assertThat(londonOrMadrid.get(1)).isEqualToComparingFieldByField(flight3);


    }
    @Test
    public void shouldFindFlightsFromLondonIgnoringCase(){
        final Flight flight = createFlight("London");

        flightRepository.save(flight);

        final List<Flight> flightsToLondon =flightRepository.findByOriginIgnoreCase("London");
        Assertions.assertThat(flightsToLondon).hasSize(1)
        .first()
        .isEqualToComparingFieldByField(flight);

    }



    private Flight createFlight(String origin, String destination) {
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));

        return flight;
    }

    private Flight createFlight(String origin){
        final Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination("Madrid");
        flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:10:00"));
        return flight;
    }
}
