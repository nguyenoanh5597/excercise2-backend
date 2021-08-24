package com.example.excercise2.service;


import com.example.excercise2.entity.Editor;

public interface EditorService {
    Editor createEditor(Editor editor);
    Editor updateEditor(String editorId, Editor editor);
    String deleteEditor(String id);
    String deleteAllEditors(String userId);
}
