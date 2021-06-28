package com.hj.restapi.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

	
	@ParameterizedTest
	@MethodSource("paramsForTestFree")
	public void testFree(int basePrice, int maxPrice, boolean isFree) {
		//Given
		Event event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build();
		
		// When
		event.update();
		
		// Then
		assertThat(event.isFree()).isEqualTo(isFree);
	}
	
	private static Object[] paramsForTestFree() {
		return new Object[] {
			new Object[] {0, 0, true},
			new Object[] {100, 0, false},
			new Object[] {0, 100, false},
			new Object[] {100, 200, false}
		};
	}

	@ParameterizedTest
	@MethodSource("paramsForTestOffline")
	public void testOffline(String location, boolean isOffline) {
		// Given
		Event event = Event.builder()
				.location(location)
				.build();
		
		// When
		event.update();
		
		// Then 
		assertThat(event.isOffline()).isEqualTo(isOffline);
	}
	
	public static Object[] paramsForTestOffline() {
		return new Object[] {
				new Object[] {"종로 3가역", true},
				new Object[] {null, false},
				new Object[] {"     ", false}
		};
	}
}

