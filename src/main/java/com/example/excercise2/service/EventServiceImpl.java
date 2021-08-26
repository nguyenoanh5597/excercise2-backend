package com.example.excercise2.service;

import com.example.excercise2.entity.Editor;
import com.example.excercise2.model.EditorEvent;
import com.example.excercise2.model.EventType;
import com.example.excercise2.model.LiveUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    private final Map<String, Map<String, EventListener>> listenersMap = new HashMap<>();

    @Override
    public String addListener(String userId, EventListener listener) {
        synchronized (listenersMap) {
            String listenerId = UUID.randomUUID().toString();
            Map<String, EventListener> eventListeners = listenersMap.get(userId);
            if (Objects.isNull(eventListeners)) {
                eventListeners = new HashMap<>();
                listenersMap.put(userId, eventListeners);
            }
            if (eventListeners.containsKey(listenerId)) {
                throw new RuntimeException("Duplicated event listener ID");
            }
            eventListeners.put(listenerId, listener);
            return listenerId;
        }
    }

    @Override
    public void removeListener(String userId, String listenerId) {
        synchronized (listenersMap) {
            Map<String, EventListener> listeners = listenersMap.get(userId);
            if (Objects.nonNull(listeners)) {
                listeners.remove(listenerId);
            }
        }
    }

    @Override
    public void broadcastEditorUpdateEvent(Editor editor) {

        EditorEvent event = new EditorEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType(EventType.EDITOR_CONTENT_UPDATE);
        event.setEditorId(editor.getId());
        event.setOwner(editor.getUserId());
        Map<String, String> eventData = new HashMap<>();
        eventData.put("content", editor.getContent());
        event.setData(eventData);

        if (editor.getPublic()) {
            broadcastEvent(event);
        } else {
            String userId = editor.getUserId();
            sendEventToUser(event, userId);
        }
    }

    @Override
    public void broadcastLiveUpdateEvent(LiveUpdateEvent updateEvent) {
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

    private void broadcastEvent(EditorEvent event) {
        synchronized (listenersMap) {
            listenersMap.forEach((k, listeners) -> {
                if (CollectionUtils.isEmpty(listeners)) {
                    return;
                }
                List<String> notified = new ArrayList<>();
                listeners.forEach((listenerId, v) -> {
                    LOGGER.info("Sending event for editor {}", k);
                    v.onNewEvent(event);
                    notified.add(listenerId);
                });
                notified.forEach(listeners::remove);
            });
        }
    }

    private void sendEventToUser(EditorEvent event, String userId) {
        synchronized (listenersMap) {
            Map<String, EventListener> listeners = listenersMap.get(userId);
            if (CollectionUtils.isEmpty(listeners)) {
                return;
            }
            List<String> notified = new ArrayList<>();
            listeners.forEach((k, v) -> {
                LOGGER.info("Sending event for editor {}", k);
                v.onNewEvent(event);
                notified.add(k);
            });
            notified.forEach(listeners::remove);
        }
    }
}
