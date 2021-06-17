package com.hj.restapi.events;

import java.net.URI;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

	private final EventRepository eventRepository;

	@PostMapping
	public ResponseEntity createEvent(@RequestBody Event event) {
		Event newEvent = eventRepository.save(event);

		URI createUri =  linkTo(EventController.class).slash(newEvent.getId()).toUri();
		event.setId(10);
		return ResponseEntity.created(createUri).body(event);
	}
}
