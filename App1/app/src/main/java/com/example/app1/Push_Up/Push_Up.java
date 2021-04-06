package com.example.app1.Push_Up;

public class Push_Up {
    private int Todays_Pushups;
    private int PB_Pushups;
    private String Date;

    public Push_Up(int todays_Pushups, int pb_pushups, String date) {
        Todays_Pushups = todays_Pushups;
        PB_Pushups = pb_pushups;
        Date = date;

    }

    public int getTodays_Pushups() {
        return Todays_Pushups;
    }

    public void setTodays_Pushups(int todays_Pushups) {
        Todays_Pushups = todays_Pushups;
    }

    public int getPB_Pushups() {
        return PB_Pushups;
    }

    public void setPB_Pushups(int PB_Pushups) {
        this.PB_Pushups = PB_Pushups;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}

