package com.pluralsight.springdataoverview;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.FlightRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.test.context.junit4.SpringRunner;


import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
class CrudTests {

	@Autowired
	private FlightRepository flightRepository;




	@Test
	public void shoudlPerformCRUDOperations(){
		Flight flight = new Flight();
		flight.setOrigin("London");
		flight.setDestination("New York");
		flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));

		flightRepository.save(flight);

		//checking if the flight is saved
		Assertions.assertThat(flightRepository.findAll())  //loads all the flights
				.hasSize(1)   //checks if the size is 1
				.first()       //picks the first flight
				.isEqualToComparingFieldByField(flight);   //and compare the flight to the flight we have here

		flightRepository.deleteById(flight.getId());  //this deletes the flight

		Assertions.assertThat(flightRepository.count()).isEqualTo(0); // checks if there is no more stored flight after deleting the on we created

	}

//	@Autowired
//	private EntityManager entityManager;
//
//	@Test
//	public void verifyFlightCanBeSaved() {
//		final Flight flight = new Flight();
//		flight.setOrigin("Amsterdam");
//		flight.setDestination("New York");
//		flight.setScheduledAt(LocalDateTime.parse("2011-12-13T12:12:00"));
//
//		entityManager.persist(flight);  // this stores it in h2 db
//     //creating a query
//		final TypedQuery<Flight> results = entityManager
//				.createQuery("SELECT f FROM Flight f", Flight.class);
//
//		final List<Flight> resultList = results.getResultList();
//
//		//comparing the two flights
//		Assertions.assertThat(resultList)
//				.hasSize(1)
//				.first()
//				.isEqualTo(flight);
//
//	}

}
