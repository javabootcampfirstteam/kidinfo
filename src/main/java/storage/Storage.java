package storage;

import model.BotEvent;
import model.BotUser;

import java.util.HashMap;
import java.util.Map;

public class Storage {

    public static int eventIndex;

    public static Map<Integer, BotUser> USERS_TABLE = new HashMap<>();
    public static Map<Integer, BotEvent> EVENTS_TABLE = new HashMap<>();
}