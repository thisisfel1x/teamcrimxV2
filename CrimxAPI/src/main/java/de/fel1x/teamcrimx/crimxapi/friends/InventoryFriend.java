package de.fel1x.teamcrimx.crimxapi.friends;

import java.util.UUID;

public class InventoryFriend {

    private UUID uuid;
    private String name;
    private boolean isOnline;
    private String connectedServer;

    public InventoryFriend(UUID uuid, String name, boolean isOnline, String connectedServer) {
        this.uuid = uuid;
        this.name = name;
        this.isOnline = isOnline;
        this.connectedServer = connectedServer;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnline() {
        return this.isOnline;
    }

    public void setOnline(boolean online) {
        this.isOnline = online;
    }

    public String getConnectedServer() {
        return this.connectedServer;
    }

    public void setConnectedServer(String connectedServer) {
        this.connectedServer = connectedServer;
    }

    @Override
    public String toString() {
        return "InventoryFriend{" +
                "uuid=" + this.uuid +
                ", name='" + this.name + '\'' +
                ", isOnline=" + this.isOnline +
                ", connectedServer='" + this.connectedServer + '\'' +
                '}';
    }
}
