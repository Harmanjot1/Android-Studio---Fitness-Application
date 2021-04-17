package com.example.app1.StepCounter;

public class Steps {
    private int ID;
    private int Amount;
    private String Date;

    public Steps(int id,int amount,String date){
        this.ID = id;
        this.Amount = amount;
        this.Date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
