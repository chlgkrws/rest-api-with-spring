package com.hj.restapi.events;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("정상적으로 이벤트를 생성하는 테스트")
	public void createEvent() throws Exception{
		EventDTO event = EventDTO.builder()
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

		mockMvc.perform(post("/api/events")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event)))
						.andDo(print())
						.andExpect(status().isCreated())		//or    is(201)
						.andExpect(jsonPath("id").exists())
						.andExpect(header().exists(HttpHeaders.LOCATION))
						.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
						.andExpect(jsonPath("id").value(Matchers.not(100)))
						.andExpect(jsonPath("free").value(Matchers.not(true)));

	}

	@Test
	@DisplayName("입력값이 비어있는 경우 에러가 발생하는 테스트")
	public void createEvent_Bad_Request() throws Exception{
		Event event = Event.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 16, 12, 29))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 17, 12, 29))
				.beginEventDateTime(LocalDateTime.of(2021, 6, 18, 12, 29))
				.endEventDateTime(LocalDateTime.of(2021, 6, 19, 12, 29))
				.basePrice(100)
				.maxPrice(200)
				.free(true)
				.offline(false)
				.limitOfEnrollment(100)
				.location("종로 3가역")
				.build();

		mockMvc.perform(post("/api/events")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event)))
						.andDo(print())
						.andExpect(status().isBadRequest())				//or    is(201)
		;
	}

	@Test
	@DisplayName("입력 값이 잘못된 경우 에러가 발생하는 테스트")
	public void createEvent_Bad_Request_Empty_Input() throws Exception {
		EventDTO eventDto = EventDTO.builder()
				.name("Spring")
				.description("REST API Development with Spring")
				.beginEnrollmentDateTime(LocalDateTime.of(2021, 8, 16, 12, 29))
				.closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 17, 12, 29))
				.beginEventDateTime(LocalDateTime.of(2021, 6, 18, 12, 29))
				.endEventDateTime(LocalDateTime.of(2021, 6, 19, 12, 29))
				.basePrice(100000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("종로 3가역")
				.build();


		this.mockMvc.perform(post("/api/events")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.objectMapper.writeValueAsString(eventDto))
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$[1].objectName").exists())
				//.andExpect(jsonPath("$[0].field").exists())
				.andExpect(jsonPath("$[1].defaultMessage").exists())
				.andExpect(jsonPath("$[1].code").exists())
				//.andExpect(jsonPath("$[0].rejectedValue").exists())
				.andDo(print())
				;
	}
}
