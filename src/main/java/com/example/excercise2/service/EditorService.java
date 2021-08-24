package com.example.excercise2.service;


import com.example.excercise2.entity.Editor;

import java.util.List;

public interface EditorService {
    Editor createEditor(Editor editor);
    Editor updateEditor(String editorId, Editor editor);
    String deleteEditor(String id);
    Editor getEditorById(String editorId);

    List<Editor> getEditorsForUser(String userId);
}
