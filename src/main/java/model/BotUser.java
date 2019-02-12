package model;

import java.util.ArrayList;
import java.util.List;

public class BotUser {


    public String telegramName;
    public String name;
    public String surname;




    public BotUser(String telegramName) {
        this.telegramName = telegramName;
    }



    private List<String> context = new ArrayList<>();

    public String getName() {
        return name;
    }


    public List<String> getContext() {
        return context;
    }

    public void setContext(List<String> context) {
        this.context = context;
    }
}
