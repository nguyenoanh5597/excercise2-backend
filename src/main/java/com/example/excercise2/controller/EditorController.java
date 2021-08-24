package com.example.excercise2.controller;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.repositories.EditorRepository;
import com.example.excercise2.service.EditorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("editor")
public class EditorController {
    final EditorRepository editorRepository;
    final EditorService editorService;

    public EditorController(EditorRepository editorRepository, EditorService editorService) {
        this.editorRepository = editorRepository;
        this.editorService = editorService;
    }

    @GetMapping("/all")
    public List<Editor> getAllEditors() {
        return editorRepository.findAll();
    }

    @GetMapping("/editors/{userId}")
    public List<Editor> getEditorsByUserId(@PathVariable("userId") String userId) {
        return editorRepository.findEditorsByUserId(userId);
    }

    @GetMapping(value ="/{editorId}")
    public Editor getEditorById(@PathVariable("editorId") String editorId) {
        Editor editor = editorRepository.findEditorById(editorId);
        if(Objects.nonNull(editor)){
            return editor;
        }
        throw new RuntimeException("Editor not found");
    }

    @PostMapping("/create")
    public Editor createEditor(@RequestBody Editor editor) {
        return editorService.createEditor(editor);
    }

    @PatchMapping("/update/{editorId}")
    public Editor updateEditor(@PathVariable("editorId") String editorId, @RequestBody Editor editor) {
        return editorService.updateEditor(editorId, editor);
    }

    @DeleteMapping("/delete/{editorId}")
    public String deleteEditor(@PathVariable("editorId") String editorId) {
        return editorService.deleteEditor(editorId);
    }


}
