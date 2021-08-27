package com.example.excercise2.repositories;

import com.example.excercise2.entity.Editor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EditorRepository extends MongoRepository<Editor, String> {
    @Query(fields="{content : 0}")
    List<Editor> findEditorsByUserId(String userId);

    Editor findEditorById(String id);

}
