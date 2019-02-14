package model;

import java.time.LocalDateTime;

public class BotEvent {

    private String eventName;
    private String eventType;
    private LocalDateTime eventDateTime;
    private String eventLocation;
    private String eventContact;
    private String eventPhone;
    private String eventAdditional;
    private int eventOwner;


    public BotEvent(String eventName, String eventType, LocalDateTime eventDateTime, String eventLocation, String eventContact, String eventPhone, String eventAdditional) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
        this.eventLocation = eventLocation;
        this.eventContact = eventContact;
        this.eventPhone = eventPhone;
        this.eventAdditional = eventAdditional;
    }
}
