package com.example.excercise2.service;

import com.example.excercise2.model.EditorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public String addListener(String editorId, EventListener listener) {
        synchronized (listenersMap) {
            String listenerId = UUID.randomUUID().toString();
            Map<String, EventListener> eventListeners = listenersMap.get(editorId);
            if (Objects.isNull(eventListeners)) {
                eventListeners = new HashMap<>();
                listenersMap.put(editorId, eventListeners);
            }
            if (eventListeners.containsKey(listenerId)) {
                throw new RuntimeException("Duplicated event listener ID");
            }
            eventListeners.put(listenerId, listener);
            return listenerId;
        }
    }

    @Override
    public void removeListener(String editorId, String listenerId) {
        synchronized (listenersMap) {
            Map<String, EventListener> listeners = listenersMap.get(editorId);
            if (Objects.nonNull(listeners)) {
                listeners.remove(listenerId);
            }
        }
    }

    @Override
    public void broadcastEvent(EditorEvent editorEvent) {
        synchronized (listenersMap) {
            Map<String, EventListener> listeners = listenersMap.get(editorEvent.getEditorId());
            List<String> notified = new ArrayList<>();
            listeners.forEach((k, v) -> {
                LOGGER.info("Sending event for editor {}", k);
                v.onNewEvent(editorEvent);
                notified.add(k);
            });
            notified.forEach(listeners::remove);
        }
    }
}
