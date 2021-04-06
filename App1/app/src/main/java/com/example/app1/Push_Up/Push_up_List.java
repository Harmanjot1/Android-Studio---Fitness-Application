package com.example.app1.Push_Up;

import java.util.ArrayList;
import java.util.List;

public class Push_up_List {

    List<Push_Up> myPush_Up_List;
    private int todays_pushups = 0;
    private int pb_pushups = 0;
    private String dateAdded = " ";

    Push_Up push_up;
    public  Push_up_List(){
        this.myPush_Up_List = new ArrayList<>();
    }

    public int get_Todays_Pushups(){
        for (int i = 0; i < myPush_Up_List.size(); i++) {

            push_up = myPush_Up_List.get(i);
            todays_pushups += push_up.getTodays_Pushups();
        }
        return todays_pushups;
    }
    public int get_PB_Pushups(){
        for (int i = 0; i < myPush_Up_List.size(); i++) {

            push_up = myPush_Up_List.get(i);
            pb_pushups += push_up.getPB_Pushups();
        }
        return pb_pushups;
    }
    public String getDateAdded() {
        for (int i = 0; i < myPush_Up_List.size(); i++) {
            push_up = myPush_Up_List.get(i);
            dateAdded = push_up.getDate();
        }
        return dateAdded;
    }

    public List<Push_Up> getMyPush_Up_List() {
        return myPush_Up_List;
    }

    public void setMyPush_Up_List(List<Push_Up> myPush_Up_List) {
        this.myPush_Up_List = myPush_Up_List;
    }
}
