package com.pluralsight.springdataoverview;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@DataMongoTest
public class PagingAndSortingTests {

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
    @Test
    public void shouldSortFlightsByScheduledAndThenName(){
        final LocalDateTime now = LocalDateTime.now();
        Flight paris1 = createFlight("Paris", now);
        Flight paris2 = createFlight("Paris", now.plusHours(2));
        Flight paris3 = createFlight("Paris", now.minusHours(1));
        Flight london1 = createFlight("London", now.plusHours(1));
        Flight london2 = createFlight("London", now);

        flightRepository.save(paris1);
        flightRepository.save(paris2);
        flightRepository.save(paris3);
        flightRepository.save(london1);
        flightRepository.save(london2);

        final Iterable<Flight> flights = flightRepository
                .findAll(Sort.by("destination", "ScheduledAt"));

        Assertions.assertThat(flights).hasSize(5);  //checking is it is 5 fight we saved to db

        final Iterator<Flight> iterator = flights.iterator(); //iterate through the 5 flights

        Assertions.assertThat(iterator.next()).isEqualToComparingFieldByField(london2);
        Assertions.assertThat(iterator.next()).isEqualToComparingFieldByField(london1);
        Assertions.assertThat(iterator.next()).isEqualToComparingFieldByField(paris3);
        Assertions.assertThat(iterator.next()).isEqualToComparingFieldByField(paris1);
        Assertions.assertThat(iterator.next()).isEqualToComparingFieldByField(paris2);
    }

    //3rd test.  paging test
    @Test
    public void shouldPageResults(){
        for (int i = 0; i<50; i++){
            flightRepository.save(createFlight(String.valueOf(i)));
        }

        //paging. grouping them into sections
        final Page<Flight> page = flightRepository.findAll(PageRequest.of(2,5));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(50); //checks if the total flights is 50
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(10);

        //test if the page content returns the correct elements
        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("10","11","12","13","14");  //page 2 because we are requesting for page 2 above


    }
    //4th test.  paging  and sorting test
    @Test
    public void shouldPageAndSortResults(){
        for (int i = 0; i<50; i++){
            flightRepository.save(createFlight(String.valueOf(i)));
        }

        //paging. grouping them into sections
        final Page<Flight> page = flightRepository
                .findAll(PageRequest.of(2,5, Sort.by(Sort.Direction.DESC,"destination")));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(50); //checks if the total flights is 50
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(10);

        //test if the page content returns the correct elements
        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("44","43","42","41","40");  //page 2  from behind because we are requesting for page 2 behind  above


    }
    //4th test.  paging  and sorting test
    @Test
    public void shouldPageAndSortADerivedQuery(){
        for (int i = 0; i<10; i++){
            final Flight flight = createFlight(String.valueOf(i));
            flight.setOrigin("paris");


            flightRepository.save(flight);
        }
        for (int i = 0; i<10; i++){
            final Flight flight = createFlight(String.valueOf(i));
            flight.setOrigin("London");


            flightRepository.save(flight);
        }


        //paging. grouping them into sections
        final Page<Flight> page = flightRepository
                .findByOrigin("London",
                        PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"destination")));

        Assertions.assertThat(page.getTotalElements()).isEqualTo(10); //checks if the total flights is 10
        Assertions.assertThat(page.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);

        //test if the page content returns the correct elements
        Assertions.assertThat(page.getContent())
                .extracting(Flight::getDestination)
                .containsExactly("9","8","7","6","5");


    }

    private Flight createFlight(String destination, LocalDateTime scheduledAt) {
        Flight flight  = new Flight();
        flight.setDestination(destination);
        flight.setOrigin("London");
        flight.setScheduledAt(scheduledAt);

        return flight;
    }

    private Flight createFlight(String destination) {
//        Flight flight =  new Flight();
//        flight.setDestination(destination);
//        flight.setOrigin("London");
        return createFlight( destination,LocalDateTime.parse("2011-12-13T12:12:00"));


    }
}
