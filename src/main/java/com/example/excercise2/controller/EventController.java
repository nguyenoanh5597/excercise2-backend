package com.example.excercise2.controller;

import com.example.excercise2.model.EditorEvent;
import com.example.excercise2.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@CrossOrigin
@RequestMapping("/event")
public class EventController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/editor/{editorId}")
    public EditorEvent getEditorEvent(@PathVariable(name = "editorId") String editorId) {
        final AtomicReference<EditorEvent> processed = new AtomicReference<>(null);
        String listenerId = eventService.addListener(editorId, editorEvent -> {
            processed.set(editorEvent);
            synchronized (processed) {
                processed.notify();
            }
        });
        LOGGER.info("Added EditorEvent listener {}", listenerId);

        synchronized (processed) {
            try {
                processed.wait();
            } catch (InterruptedException e) {
                // ignore
            }
        }
        return processed.get();
    }
}
