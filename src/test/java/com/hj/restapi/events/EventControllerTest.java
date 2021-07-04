package com.hj.restapi.events;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hj.restapi.common.RestDocsConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	EventRepository eventRepository;

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
										linkWithRel("update-event").description("link to update"),
										linkWithRel("profile").description("link to profile")
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
				.andExpect(jsonPath("errors[0].objectName").exists())
				.andExpect(jsonPath("errors[0].defaultMessage").exists())
				.andExpect(jsonPath("errors[0].code").exists())
				.andExpect(jsonPath("_links.index").exists())
				.andDo(print())
				;
	}

	@Test
	@DisplayName("30개의 이벤트를 10개씩 두 번째 페이지 조회하기")
	public void queryEvents() throws Exception {
		// given
		IntStream.range(0, 30).forEach(i -> {
				this.generateEvent(i);
		});

		// when & then
		this.mockMvc.perform(get("/api/events")
				.param("page", "1")
				.param("size", "10")
				.param("sort", "name,DESC")
				.accept(MediaTypes.HAL_JSON)
		)
				.andExpect(status().isOk())
				.andExpect(jsonPath("page").exists())
				.andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
				.andExpect(jsonPath("_links.profile").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andDo(document("query-events",
						links(
								linkWithRel("first").description("link to first eventList"),
								linkWithRel("prev").description("link to prev eventList"),
								linkWithRel("self").description("link to present eventList"),
								linkWithRel("next").description("link to next eventList"),
								linkWithRel("last").description("link to last eventList"),
								linkWithRel("profile").description("link to profile")
						),
						requestHeaders(
								headerWithName(HttpHeaders.ACCEPT).description("accept header")
						),
						requestParameters(
								parameterWithName("page").description("page of events"),
								parameterWithName("size").description("size of events"),
								parameterWithName("sort").description("type of sort")
						),
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("json hal")
						),
						relaxedResponseFields(
								beneathPath("_embedded.eventList[0]"),
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
								fieldWithPath("eventStatus").description("event status"),
								fieldWithPath("_links.self").description("link to self")
						)
				))
				.andDo(print())
		;
	}
	
	// 이벤트 생성 및 저장 함수
	private Event generateEvent(int index) {
		Event event = Event.builder()
				.name("event"+ index)
				.description("test event")
				.build();
		
		return this.eventRepository.save(event);
	}

	@Test
	@DisplayName("기존의 이벤트를 하나 조회하기")
	public void getEvent() throws Exception{
		// given
		Event event = this.generateEvent(100);

		// when & then
		this.mockMvc.perform(get("/api/events/{id}", event.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("name").exists())
				.andExpect(jsonPath("id").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.profile").exists())
				.andDo(document("get-an-event"))

				;
	}

	@Test
	@DisplayName("없는 이벤트를 조회했을 때 404 응답받기")
	public void getEvent404() throws Exception{
		// when & then
		this.mockMvc.perform(get("/api/events/124124214"))
				.andExpect(status().isNotFound())
		;
	}
}
