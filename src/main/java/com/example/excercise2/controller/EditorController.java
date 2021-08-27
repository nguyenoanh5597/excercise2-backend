package com.example.excercise2.controller;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.repositories.EditorRepository;
import com.example.excercise2.service.EditorService;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
public class EditorController extends BaseController {
    final EditorRepository editorRepository;
    final EditorService editorService;

    public EditorController(EditorRepository editorRepository, EditorService editorService) {
        this.editorRepository = editorRepository;
        this.editorService = editorService;
    }

    @GetMapping("/editors")
    public List<Editor> getEditors() {
        return editorService.getEditorsForUser(getCurrentUser().getUserId());
    }

    @PostMapping("/editors")
    public Editor createEditor(@RequestBody Editor editor) {
        editor.setUserId(getCurrentUser().getUserId());
        return editorService.createEditor(editor);
    }

    @GetMapping(value = "/editor/{editorId}")
    public Editor getEditorById(@PathVariable("editorId") String editorId) {
        Editor editor = editorService.getEditorById(editorId);
        if (Objects.nonNull(editor)) {
            if (editor.getPublic()) {
                return editor;
            } else {
                if (editor.getUserId().equals(getCurrentUser().getUserId())) {
                    return editor;
                }
            }
        }
        throw new RuntimeException("Editor not found!");
    }

    @PutMapping("/editor/{editorId}")
    public Editor updateEditor(@PathVariable("editorId") String editorId, @RequestBody Editor editor) {
        Editor existing = editorService.getEditorById(editorId);
        if (Objects.nonNull(existing) &&
            (existing.getPublic() || (existing.getUserId().equals(getCurrentUser().getUserId())))) {
            return editorService.updateEditor(editorId, editor);
        }
        throw new RuntimeException("Editor not found!");
    }

    @DeleteMapping("/editor/{editorId}")
    public String deleteEditor(@PathVariable("editorId") String editorId) {
        Editor existing = editorService.getEditorById(editorId);
        if (Objects.nonNull(existing) && existing.getUserId().equals(getCurrentUser().getUserId())) {
            return editorService.deleteEditor(editorId);
        }
        throw new RuntimeException("Editor not found!");
    }


}
