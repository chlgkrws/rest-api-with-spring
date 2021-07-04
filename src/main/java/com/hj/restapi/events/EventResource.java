package com.hj.restapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class EventResource extends EntityModel<Event>{
	EventResource(Event event, Link... links){
        super.of(event, links);
	    add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
