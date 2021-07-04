package com.hj.restapi.index;

import com.hj.restapi.events.Event;
import com.hj.restapi.events.EventController;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public EntityModel<Event> index(){
        Event event = new Event();
        EntityModel<Event> index = EntityModel.of(event);

        index.add(linkTo(EventController.class).withRel("events"));
        return index;
    }
}
