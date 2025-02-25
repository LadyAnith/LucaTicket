package com.ejemplos.spring.controller;
/**
 * @author Ana Díaz Muñoz */
import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ejemplos.spring.model.Event;
import com.ejemplos.spring.service.EventService;

@RestController
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private EventService service;


	@GetMapping(value = "/all")
	public Collection<Event> readEvents() {
		return service.findAll();
	}
	
	/*
	 * this orElseThrow method at the end of the findById call and what does it do?
	 * It is part of the java.util.Optional API. It returns the wrapped Student
	 * object if present, otherwise it throws the exception provided by the
	 * exception supplier. However, we can substitute the supplier with a method
	 * reference to our custom exception class’ constructor. When this exception is
	 * thrown, a “404 Not Found” status code is returned, as described above.
	 * https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html#orElseThrow
	 * -java.util.function.Supplier-.
	 */
	
	//Find event by ID
	@GetMapping(value = "/{id}")
	public Event readEvent(@PathVariable String id) {
		System.out.println("-------- readEvent");
		return service.findById(id)
				   .orElseThrow(EventNotFoundException::new);
	}

	//RequestBody --> The event will be the body of the response
	@PostMapping(value = "/add")
	public ResponseEntity<?> addEvent(@RequestBody Event event) {
		Event result = this.service.save(event);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
				.toUri();
		/*
		 * Inside the method body, we build a java.net.URI object using
		 * ServletUriComponentsBuilder. It builds the object by capturing the URI of the
		 * current request and appending the placeholder /{id} to create a template.
		 * buildAndExpand(result.getId()) inserts the id of the newly created student
		 * into the template. The result is the URI of the new resource.
		 */
		return ResponseEntity.created(location).build();
	}

	//Delete event by ID
	@DeleteMapping(value = "/{id}")
	public void deleteEvent(@PathVariable String id) {
		service.deleteById(id);
	}
	
	
}