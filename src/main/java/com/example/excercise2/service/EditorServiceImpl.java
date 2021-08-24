package com.example.excercise2.service;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.repositories.EditorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EditorServiceImpl implements EditorService {
    final EditorRepository editorRepository;

    public EditorServiceImpl(EditorRepository editorRepository) {
        this.editorRepository = editorRepository;
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
        if(Objects.nonNull(e)){
            e.setDisplayName(editor.getDisplayName());
            e.setContent(editor.getContent());
            e.setPublic(editor.getPublic());
            e.setVersion(editor.getVersion());
            editorRepository.save(e);
            return e;
        }
        throw new RuntimeException("Editor not found");
    }

    @Override
    public String deleteEditor(String id) {
        Editor e = editorRepository.findEditorById(id);
        if(Objects.nonNull(e)){
            editorRepository.delete(e);
            return "delete success";
        }
        throw new RuntimeException("Editor not found");
    }

    @Override
    public String deleteAllEditors(String userId) {
        List<Editor> editors = editorRepository.findEditorsByUserId(userId);
        if(editors.size() > 0){
            editorRepository.deleteAll(editors);
            return "delete success";
        }
        throw new RuntimeException("Editor not found");
    }
}
