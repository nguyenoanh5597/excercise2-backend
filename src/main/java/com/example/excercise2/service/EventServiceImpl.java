package com.example.excercise2.service;

import com.example.excercise2.model.EditorEvent;
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
    public void broadcastEvent(EditorEvent event) {
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

    @Override
    public void sendEventToUser(EditorEvent event, String userId) {
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
