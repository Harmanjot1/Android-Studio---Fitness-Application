package com.example.app1.Calories_Burned;

public class Calories_Burned_Object {

    private String Reason_burned;
    private int amount_burned;
    private String DATE;
    private int ID;

    public Calories_Burned_Object(int ID,String reason, int amount_burned,String date){
        this.ID = ID;
        this.Reason_burned = reason;
        this.amount_burned = amount_burned;
        this.DATE = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getReason_burned() {
        return Reason_burned;
    }

    public void setReason_burned(String reason_burned) {
        Reason_burned = reason_burned;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getReason() {
        return Reason_burned;
    }

    public void setReason(String reason) {
        Reason_burned = reason;
    }

    public int getAmount_burned() {
        return amount_burned;
    }

    public void setAmount_burned(int amount_burned) {
        this.amount_burned = amount_burned;
    }
}
