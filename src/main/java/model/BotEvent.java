package model;

import bot.Point;

import java.time.LocalDateTime;

public class BotEvent {

    private String eventName;
    private String eventType;
    private LocalDateTime eventDateTime;
//    private String eventLocation;
    private String eventContact;
    private String eventPhone;
    private Point eventLocation;
//    private String eventAdditional;
//    private int eventOwner;


    public BotEvent(String eventName, String eventType, LocalDateTime eventDateTime, String eventContact, Point eventLocation) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
        this.eventLocation = eventLocation;
        this.eventContact = eventContact;
//        this.eventPhone = eventPhone;
//        this.eventAdditional = eventAdditional;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public String getEventContact() {
        return eventContact;
    }

    public Point getEventLocation() {
        return eventLocation;
    }
}
