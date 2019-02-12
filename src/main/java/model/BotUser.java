package model;

import java.util.ArrayList;
import java.util.List;

public class BotUser {

    private String name;

    public BotUser(String name) {
        this.name = name;
    }


    private List<String> context = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(List<String> context) {
        this.context = context;
    }
}
