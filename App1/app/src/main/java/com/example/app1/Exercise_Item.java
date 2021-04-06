package com.example.app1;

public class Exercise_Item {

    private final int mImageResource;
    private final String mText1;
    private final String mText2;

    public Exercise_Item(int imageResource, String text1, String text2) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }

}
