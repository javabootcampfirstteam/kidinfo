package model;

import java.time.LocalDateTime;

public class BotEvent {

    private String eventName;
    private String eventType;
    private LocalDateTime eventDateTime;
    private String eventLocation;
    private String enetContact;
    private String eventPhone;
    private String eventAdditional;


    public BotEvent(String eventName, String eventType, LocalDateTime eventDateTime, String eventLocation, String enetContact, String eventPhone, String eventAdditional) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventDateTime = eventDateTime;
        this.eventLocation = eventLocation;
        this.enetContact = enetContact;
        this.eventPhone = eventPhone;
        this.eventAdditional = eventAdditional;
    }
}
