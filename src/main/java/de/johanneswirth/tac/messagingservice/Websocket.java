package de.johanneswirth.tac.messagingservice;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.tac.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value="/public/websocket", encoders = ClientMessageEncoder.class)
@Timed
@Metered
@ExceptionMetered
public class Websocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(Websocket.class);

    private static final Map<String, List<Session>> connections = new ConcurrentHashMap<>();

    @OnOpen
    public void start(Session session) {
    }

    @OnMessage
    public void recvAuth(final Session session, String message) {
        String subject = Utils.validateJWT(message).getSubject();
        List<Session> sessions = connections.getOrDefault(subject, new LinkedList<>());
        connections.put(subject, sessions);
        sessions.add(session);
    }

    private void remove(Session session) {
        for (List<Session> sessions : connections.values()) {
            sessions.remove(session);
        }

    }

    @OnClose
    public void end(final Session session) {
        remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.warn("", throwable);
        remove(session);
    }

    public static void broadcast(List<String> recipients, ClientMessage data) {
        for (String recipient : recipients) {
            List<Session> sessions = connections.get(recipient);
            if (sessions == null) continue;
            for (Session session : sessions) {
                try {
                    session.getBasicRemote().sendObject(data);
                } catch (IOException e) {
                    LOGGER.warn("", e);
                    sessions.remove(session);
                    try {
                        session.close();
                    } catch (IOException e1) {
                        LOGGER.error("", e);
                    }
                } catch (EncodeException e) {
                    LOGGER.error("", e);
                }
            }
        }
    }
}
