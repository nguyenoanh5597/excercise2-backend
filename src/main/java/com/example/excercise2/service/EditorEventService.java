package com.example.excercise2.service;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.model.LiveUpdateEvent;

public interface EditorEventService {
    void sendEditorUpdateEvent(Editor editor);

    void sendLiveUpdateEvent(LiveUpdateEvent updateEvent);

    void sendEditorVisibilityChangedEvent(Editor editor, Boolean isPublic);

    void sendEditorCreatedEvent(Editor newEditor);

    void sendEditorRemovedEvent(String editorId);
}
