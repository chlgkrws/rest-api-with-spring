package com.hj.restapi.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class EventControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	EventRepository eventRepository;

	@Test
	public void createEvent() throws Exception{
		Event event = Event.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 16, 12, 29))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 17, 12, 29))
				.beginEventDateTime(LocalDateTime.of(2021, 6, 18, 12, 29))
				.endEventDateTime(LocalDateTime.of(2021, 6, 19, 12, 29))
				.basePrice(100)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("종로 3가역")
				.build();

		event.setId(10);
		Mockito.when(eventRepository.save(event)).thenReturn(event);

		mockMvc.perform(post("/api/events")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event)))
						.andDo(print())
						.andExpect(status().isCreated())		//or    is(201)
						.andExpect(jsonPath("id").exists());
	}
}
