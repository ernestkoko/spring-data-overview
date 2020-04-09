package com.pluralsight.springdataoverview;


import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomImplTests {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shoudlSaveCutomImpl(){
        Flight toDelete = createFlight("London");
        Flight toKeep = createFlight("Paris");


        flightRepository.save(toDelete);
        flightRepository.save(toKeep);

        flightRepository.deleteByOrigin("London");

        Assertions.assertThat(flightRepository.findAll())
        .hasSize(1)
        .first()
        .isEqualToComparingFieldByField(toKeep);

    }

    private Flight createFlight(String origin) {
        Flight flight = new Flight();
        flight.setDestination("Tokyo");
        flight.setOrigin(origin);
        flight.setScheduledAt(LocalDateTime.now());
        return flight;
    }
}
