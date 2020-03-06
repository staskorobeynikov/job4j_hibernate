package ru.job4j.models;

import java.sql.Timestamp;
import java.util.Objects;

public class User {

    private int id;

    private String name;

    private Timestamp expired;

    public User() {
    }

    public User(int id, String name, Timestamp expired) {
        this.id = id;
        this.name = name;
        this.expired = expired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getExpired() {
        return expired;
    }

    public void setExpired(Timestamp expired) {
        this.expired = expired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("User: id=%s, name=%s, expired=%s.", id, name, expired);
    }
}
