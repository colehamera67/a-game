package com.zombiemmo.network;

/**
 * Base class for network messages
 */
public class NetworkMessage {
    private MessageType type;
    private String data;

    public NetworkMessage() {
    }

    public NetworkMessage(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
