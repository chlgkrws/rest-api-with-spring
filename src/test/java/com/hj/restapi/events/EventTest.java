package com.hj.restapi.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


public class EventTest {

	@Test
	public void builder() {
		Event event = Event.builder()
				.name("hj")
				.description("REST API development with Spring")
				.build();
		assertThat(event).isNotNull();
	}

	@Test
	public void javaBean() {
		// Given
		String name = "Event";
		String description = "Spring";

		// When
		Event event = new Event();
		event.setName(name);
		event.setDescription(description);

		// Then
		assertThat(event.getName()).isEqualTo(name);
		assertThat(event.getDescription()).isEqualTo(description);
	}
	
	@Test
	public void testFree() {
		//Given
		Event event = Event.builder()
				.basePrice(0)
				.maxPrice(0)
				.build();
		
		// When
		event.update();
		
		// Then
		assertThat(event.isFree()).isTrue();
		
		
		//Given
		Event event2 = Event.builder()
				.basePrice(100)
				.maxPrice(0)
				.build();
		
		// When
		event2.update();
		
		// Then
		assertThat(event2.isFree()).isFalse();
		
		//Given
		Event event3 = Event.builder()
				.basePrice(0)
				.maxPrice(100)
				.build();
		
		// When
		event3.update();
		
		// Then
		assertThat(event3.isFree()).isFalse();
	}
	
	@Test
	public void testOffline() {
		//Given
		Event event = Event.builder()
				.location("종로 3가역")
				.build();
		
		//When
		event.update();
		
		//Then 
		assertThat(event.isOffline()).isTrue();
		
		//Given
		event = Event.builder()
				.build();
		
		//When
		event.update();
		
		//Then 
		assertThat(event.isOffline()).isFalse();
	}
}

