package model;

import java.util.ArrayList;
import java.util.List;

public class BotUser {


    private String telegramName;
    private String name;
    private String surname;
    private String location;
    private String role;
    private String myEvents;


    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public BotUser(String telegramName) {
        this.telegramName = telegramName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private List<String> context = new ArrayList<>();

    public List<String> getContext() {
        return context;
    }

    public String getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(String myEvents) {
        this.myEvents = myEvents;
    }
}
