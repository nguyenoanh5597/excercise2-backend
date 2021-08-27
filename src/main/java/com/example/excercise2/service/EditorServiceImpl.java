package com.example.excercise2.service;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.repositories.EditorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

        sendEditorCreatedEvent(newEditor);

        return newEditor;
    }

    @Override
    public Editor updateEditor(String editorId, Editor editor) {
        Editor e = editorRepository.findEditorById(editorId);
        if (Objects.nonNull(e)) {
            Boolean previousPublic = e.getPublic();

            e.setDisplayName(editor.getDisplayName());
            e.setContent(editor.getContent());
            e.setPublic(editor.getPublic());
            e.setVersion(editor.getVersion());
            editorRepository.save(e);
            // TODO: only send event if something changed
            sendEditorUpdateEvent(e);
            if (previousPublic && !e.getPublic()) {
                // user make an editor from public -> private
                sendEditorVisibilityChangedEvent(editor, e.getPublic());
            }
            return e;
        }
        throw new RuntimeException("Editor not found");
    }

    @Override
    public String deleteEditor(String id) {
        Editor e = editorRepository.findEditorById(id);
        if (Objects.nonNull(e)) {
            editorRepository.delete(e);
            sendEditorRemovedEvent(id);
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

    private void sendEditorCreatedEvent(Editor newEditor) {
        eventService.broadcastEditorCreatedEvent(newEditor);
    }

    private void sendEditorRemovedEvent(String editorId) {
        eventService.broadcastEditorRemovedEvent(editorId);
    }

    private void sendEditorVisibilityChangedEvent(Editor editor, Boolean isPublic) {
        eventService.broadcastEditorVisibilityChangedEvent(editor, isPublic);
    }

    private void sendEditorUpdateEvent(Editor editor) {
        eventService.broadcastEditorUpdateEvent(editor);
    }
}
