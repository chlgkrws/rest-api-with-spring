package com.hj.restapi.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.restapi.common.RestDocsConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
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
						.andExpect(jsonPath("offline").value(true))
						.andExpect(jsonPath("free").value(false))
						.andExpect(jsonPath("_links.self").exists())
						.andExpect(jsonPath("_links.query-events").exists())
						.andExpect(jsonPath("_links.update-event").exists())
						.andDo(document("create-event",
								links(
										linkWithRel("self").description("link to self"),
										linkWithRel("query-events").description("link to query"),
										linkWithRel("update-event").description("link to update")
								),
								requestHeaders(
										HeaderDocumentation.headerWithName(HttpHeaders.ACCEPT).description("accept header"),
										HeaderDocumentation.headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
								),
								requestFields(
										fieldWithPath("name").description("Name of new event"),
										fieldWithPath("description").description("description of new event"),
										fieldWithPath("beginEnrollmentDateTime").description("data time of begin of new event"),
										fieldWithPath("closeEnrollmentDateTime").description("data time of close of new event"),
										fieldWithPath("beginEventDateTime").description("data time of begin of new event"),
										fieldWithPath("endEventDateTime").description("data time of end of new event"),
										fieldWithPath("location").description("location of new event"),
										fieldWithPath("basePrice").description("basePrice of new event"),
										fieldWithPath("maxPrice").description("maxPrice of new event"),
										fieldWithPath("limitOfEnrollment").description("limit of new event")
								),
								responseHeaders(
										headerWithName(HttpHeaders.LOCATION).description("Location of response"),
										headerWithName(HttpHeaders.CONTENT_TYPE).description("json-hal")
								),
								relaxedResponseFields(
										fieldWithPath("id").description("identifier of new event"),
										fieldWithPath("name").description("Name of new event"),
										fieldWithPath("description").description("description of new event"),
										fieldWithPath("beginEnrollmentDateTime").description("data time of begin of new event"),
										fieldWithPath("closeEnrollmentDateTime").description("data time of close of new event"),
										fieldWithPath("beginEventDateTime").description("data time of begin of new event"),
										fieldWithPath("endEventDateTime").description("data time of end of new event"),
										fieldWithPath("location").description("location of new event"),
										fieldWithPath("basePrice").description("basePrice of new event"),
										fieldWithPath("maxPrice").description("maxPrice of new event"),
										fieldWithPath("limitOfEnrollment").description("limit of new event"),
										fieldWithPath("free").description("it tells if this event is free or not"),
										fieldWithPath("offline").description("it tells if this event is offline or not"),
										fieldWithPath("eventStatus").description("event status")
								)
						))
						;

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
