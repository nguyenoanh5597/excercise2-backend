package com.example.excercise2.service;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.model.EditorEvent;
import com.example.excercise2.model.LiveUpdateEvent;

public interface EventService {

    String addListener(String userId, EventListener listener);

    void removeListener(String userId, String listenerId);

    void broadcastEditorUpdateEvent(Editor editor);

    void broadcastLiveUpdateEvent(LiveUpdateEvent updateEvent);

    void broadcastEditorVisibilityChangedEvent(Editor editor, Boolean isPublic);
}
