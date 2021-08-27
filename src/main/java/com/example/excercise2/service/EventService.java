package com.example.excercise2.service;

import com.example.excercise2.model.EditorEvent;
import com.example.excercise2.model.EventListener;

import java.util.Optional;

public interface EventService {

    void broadcastEvent(EditorEvent event);

    EventListener createListener(String userId);

    void removeListener(String listenerId);

    Optional<EventListener> getListenerById(String listenerId);
}
