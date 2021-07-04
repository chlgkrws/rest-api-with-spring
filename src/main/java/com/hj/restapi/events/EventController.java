package com.hj.restapi.events;

import java.net.URI;

import javax.validation.Valid;

import com.hj.restapi.common.ErrorResource;
import com.hj.restapi.index.IndexController;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
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

	private final ModelMapper modelMapper;

	private final EventValidator eventValidator;

	@GetMapping
	public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler ){
		Page<Event> page = eventRepository.findAll(pageable);
		var pagedResources = assembler.toModel(page, e -> {
			EntityModel<Event> eventEntityModel = EntityModel.of(e);
			eventEntityModel.add(linkTo(EventController.class).slash(e.getId()).withSelfRel());
			return eventEntityModel;
		});
		pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
		return ResponseEntity.ok().body(pagedResources);
	}

	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Valid EventDTO eventDTO, Errors errors) {
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		eventValidator.validate(eventDTO, errors);
		if(errors.hasErrors()) {
			return badRequest(errors);
		}

		Event event = modelMapper.map(eventDTO, Event.class);
		event.update();
		
		Event newEvent = eventRepository.save(event);
		
		WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());
		URI createUri =  selfLinkBuilder.toUri();
		
		EntityModel<Event> eventResource = EntityModel.of(newEvent);
		eventResource.add(selfLinkBuilder.withSelfRel());
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
		eventResource.add(selfLinkBuilder.withRel("update-event"));
		eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
		
		return ResponseEntity.created(createUri).body(eventResource);
	}

	private ResponseEntity badRequest(Errors errors){
		EntityModel<Errors> errorsResource = EntityModel.of(errors);
		errorsResource.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
		return ResponseEntity.badRequest().body(errorsResource);
	}
}
