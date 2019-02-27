package com.xhedu.entity;

public class SocketEntity {

    private String username;

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SocketEntity(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "SocketEntity{" +
                "username='" + username + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
