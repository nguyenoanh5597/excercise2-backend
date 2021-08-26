package com.example.excercise2.service;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.model.EditorEvent;
import com.example.excercise2.model.EventType;
import com.example.excercise2.repositories.EditorRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class EditorServiceImpl implements EditorService {
    private final EditorRepository editorRepository;
    private final EventService eventService;

    public EditorServiceImpl(EditorRepository editorRepository, EventService eventService) {
        this.editorRepository = editorRepository;
        this.eventService = eventService;
    }

    @Override
    public Editor createEditor(Editor editor) {
        Editor newEditor = new Editor();
        newEditor.setDisplayName(editor.getDisplayName());
        newEditor.setUserId(editor.getUserId());
        newEditor.setPublic(editor.getPublic());
        newEditor.setVersion(0);
        newEditor.setContent("");
        editorRepository.save(newEditor);
        return newEditor;
    }

    @Override
    public Editor updateEditor(String editorId, Editor editor) {
        Editor e = editorRepository.findEditorById(editorId);
        if (Objects.nonNull(e)) {
            e.setDisplayName(editor.getDisplayName());
            e.setContent(editor.getContent());
            e.setPublic(editor.getPublic());
            e.setVersion(editor.getVersion());
            editorRepository.save(e);
            sendEditorContentUpdate(e);
            return e;
        }
        throw new RuntimeException("Editor not found");
    }

    private void sendEditorContentUpdate(Editor editor) {
        EditorEvent event = new EditorEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType(EventType.EDITOR_CONTENT_UPDATE);
        event.setEditorId(editor.getId());
        Map<String, String> eventData = new HashMap<>();
        eventData.put("content", editor.getContent());
        event.setData(eventData);
        eventService.broadcastEvent(event);
    }

    @Override
    public String deleteEditor(String id) {
        Editor e = editorRepository.findEditorById(id);
        if (Objects.nonNull(e)) {
            editorRepository.delete(e);
            return "delete success";
        }
        throw new RuntimeException("Editor not found");
    }

    @Override
    public Editor getEditorById(String editorId) {
        return editorRepository.findEditorById(editorId);
    }

    @Override
    public List<Editor> getEditorsForUser(String userId) {
        return editorRepository.findEditorsByUserId(userId);
    }
}
