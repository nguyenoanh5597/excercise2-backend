package com.example.excercise2.service;

import com.example.excercise2.entity.Editor;

public interface EditorEventService {
    void sendEditorUpdateEvent(Editor editor);

    void sendEditorVisibilityChangedEvent(Editor editor, Boolean isPublic);

    void sendEditorCreatedEvent(Editor newEditor);

    void sendEditorRemovedEvent(String editorId);
}
