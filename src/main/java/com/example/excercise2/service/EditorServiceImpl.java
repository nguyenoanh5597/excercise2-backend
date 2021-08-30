package com.example.excercise2.service;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.repositories.EditorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EditorServiceImpl implements EditorService {
    private final EditorRepository editorRepository;
    private final EditorEventService editorEventService;

    public EditorServiceImpl(EditorRepository editorRepository, EditorEventService editorEventService) {
        this.editorRepository = editorRepository;
        this.editorEventService = editorEventService;
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
        editorEventService.sendEditorCreatedEvent(newEditor);
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
            editorEventService.sendEditorUpdateEvent(e);
            if (previousPublic && !e.getPublic()) {
                editorEventService.sendEditorVisibilityChangedEvent(editor, e.getPublic());
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
            editorEventService.sendEditorRemovedEvent(id);
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
