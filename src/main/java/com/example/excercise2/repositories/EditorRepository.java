package com.example.excercise2.repositories;

import com.example.excercise2.entity.Editor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EditorRepository extends MongoRepository<Editor, String> {
    Editor findEditorById(String id);
    List<Editor> findEditorsByUserId(String userId);
}
