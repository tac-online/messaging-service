package de.johanneswirth.tac.messagingservice;

import de.johanneswirth.tac.common.Message;

public class ClientMessage {
    private String service;
    private String resource;
    private long version;
    private long timestamp;
    private String payload;

    public ClientMessage(Message message) {
        this.service = message.getService();
        this.resource = message.getResource();
        this.version = message.getVersion();
        this.payload = message.getPayload();
        this.timestamp = System.currentTimeMillis();
    }

    public String getService() {
        return service;
    }

    public String getResource() {
        return resource;
    }

    public long getVersion() {
        return version;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPayload() {
        return payload;
    }
}
