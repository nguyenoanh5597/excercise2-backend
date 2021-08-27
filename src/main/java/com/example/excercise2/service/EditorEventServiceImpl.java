package com.example.excercise2.service;

import com.example.excercise2.config.AmqpConfig;
import com.example.excercise2.entity.Editor;
import com.example.excercise2.model.EditorEvent;
import com.example.excercise2.model.EventType;
import com.example.excercise2.model.LiveUpdateEvent;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EditorEventServiceImpl implements EditorEventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditorEventServiceImpl.class);

    private final Gson gson;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpConfig amqpConfig;

    public EditorEventServiceImpl(Gson gson, RabbitTemplate rabbitTemplate, AmqpConfig amqpConfig) {
        this.gson = gson;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpConfig = amqpConfig;
    }
    @Override
    public void sendEditorUpdateEvent(Editor editor) {
        EditorEvent event = new EditorEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType(EventType.EDITOR_UPDATE);
        event.setEditorId(editor.getId());
        event.setOwner(editor.getUserId());

        Map<String, String> eventData = new HashMap<>();
        eventData.put("displayName", editor.getDisplayName());
        eventData.put("owner", editor.getUserId());
        eventData.put("version", String.valueOf(editor.getVersion()));
        eventData.put("public", editor.getPublic() ? "true" : "false");
        event.setData(eventData);

//        if (editor.getPublic()) {
        broadcastEvent(event);
//        } else {
//            String userId = editor.getUserId();
//            sendEventToUser(event, userId);
//        }
    }

    @Override
    public void sendLiveUpdateEvent(LiveUpdateEvent updateEvent) {
        LOGGER.info("Handle live update event {}", updateEvent);

        EditorEvent event = new EditorEvent();
        event.setEditorId(updateEvent.getEditorId());
        event.setEventType(EventType.EDITOR_CONTENT_LIVE_UPDATE);
        event.setEventId(UUID.randomUUID().toString());
        Map<String, String> data = new HashMap<>();
        data.put("content", updateEvent.getContent());
        data.put("sourceId", updateEvent.getSourceId());
        event.setData(data);

        broadcastEvent(event);
    }

    @Override
    public void sendEditorVisibilityChangedEvent(Editor editor, Boolean isPublic) {
        EditorEvent event = new EditorEvent();
        event.setEditorId(editor.getId());
        event.setEventType(EventType.EDITOR_VISIBILITY_CHANGED);
        event.setEventId(UUID.randomUUID().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("public", isPublic);
        event.setData(data);
        broadcastEvent(event);
    }

    @Override
    public void sendEditorCreatedEvent(Editor newEditor) {
        EditorEvent event = new EditorEvent();
        event.setEditorId(newEditor.getId());
        event.setEventType(EventType.EDITOR_CREATED);
        event.setEventId(UUID.randomUUID().toString());
        event.setOwner(newEditor.getUserId());
        Map<String, Object> data = new HashMap<>();
        data.put("newEditor", newEditor);
        event.setData(data);
        broadcastEvent(event);
    }

    @Override
    public void sendEditorRemovedEvent(String editorId) {
        EditorEvent event = new EditorEvent();
        event.setEditorId(editorId);
        event.setEventType(EventType.EDITOR_REMOVED);
        event.setEventId(UUID.randomUUID().toString());
        broadcastEvent(event);
    }

    private void broadcastEvent(EditorEvent event) {
        String message = gson.toJson(event);
        rabbitTemplate.convertAndSend(amqpConfig.getEditorEventsFanout(), "", message);
    }
}
