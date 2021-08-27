package com.example.excercise2.controller;

import com.example.excercise2.model.Event;
import com.example.excercise2.model.LiveUpdateEvent;
import com.example.excercise2.model.UserInfo;
import com.example.excercise2.service.EditorEventService;
import com.example.excercise2.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@CrossOrigin
@RequestMapping("/event")
public class EventController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final EditorEventService editorEventService;

    public EventController(EventService eventService, EditorEventService editorEventService) {
        this.eventService = eventService;
        this.editorEventService = editorEventService;
    }

    @PostMapping("/editor/liveEvents")
    public void submitLiveEvent(@RequestBody LiveUpdateEvent updateEvent){
        editorEventService.sendLiveUpdateEvent(updateEvent);
    }

    @GetMapping
    public Event getEditorEvent() {
        UserInfo currentUser = getCurrentUser();
        final AtomicReference<Event> processed = new AtomicReference<>(null);
        String listenerId = eventService.addListener(currentUser.getUserId(), editorEvent -> {
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
