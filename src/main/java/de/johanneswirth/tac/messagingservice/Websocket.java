package de.johanneswirth.tac.messagingservice;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.tac.common.Status;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import static java.util.logging.Logger.getGlobal;

@ServerEndpoint(value = "/websocket/")
@Timed
@Metered
@ExceptionMetered
public class Websocket {
    private static final Map<String, Websocket> connections = new ConcurrentHashMap<>();
    private Session session;

    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.put(session.getUserPrincipal().getName(), this);
    }

    @OnClose
    public void end() {
        connections.remove(this);
    }

    public static Status broadcast(List<String> recipients, ClientMessage data) {
        List<String> failed = new LinkedList<>();
        for (String recipient : recipients) {
            Websocket client = connections.get(recipient);
            try {
                client.session.getBasicRemote().sendObject(data);
            } catch (IOException e) {
                failed.add(recipient);
                getGlobal().log(Level.WARNING, "", e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    getGlobal().log(Level.SEVERE, "", e);
                }
            } catch (EncodeException e) {
                failed.add(recipient);
                getGlobal().log(Level.SEVERE, "", e);
            }
        }
        if (failed.isEmpty()) {
            return Status.OK();
        } else {
            return Status.MESSAGES_FAILED(failed);
        }
    }
}
