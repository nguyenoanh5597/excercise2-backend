package com.example.excercise2.service;

import com.example.excercise2.model.EditorEvent;

public interface EventService {

    String addListener(String editorId, EventListener listener);

    void removeListener(String editorId, String listenerId);

    void broadcastEvent(EditorEvent editorEvent);
}
