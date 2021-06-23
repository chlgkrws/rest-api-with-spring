package com.hj.restapi.events;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON)
@RequiredArgsConstructor
public class EventController {

	private final EventRepository eventRepository;

	private final ModelMapper modelMapper;

	private final EventValidator eventValidator;

	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Valid EventDTO eventDTO, Errors errors) {
		if(errors.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}

		eventValidator.validate(eventDTO, errors);
		if(errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors);
		}

		Event event = modelMapper.map(eventDTO, Event.class);
		Event newEvent = eventRepository.save(event);
		URI createUri =  linkTo(EventController.class).slash(newEvent.getId()).toUri();
		return ResponseEntity.created(createUri).body(event);
	}
}
