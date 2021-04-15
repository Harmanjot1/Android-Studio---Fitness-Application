package com.example.app1.Exercise;

public class Exercise_Item {

    private final int mImageResource1;
    private final int mImageResource2;
    private final String mText1;
    private final String mText2;

    public Exercise_Item(int imageResource1,int imageresource2, String text1, String text2) {
        mImageResource1 = imageResource1;
        mImageResource2 = imageresource2;
        mText1 = text1;
        mText2 = text2;
    }

    public int getmImageResource1() {
        return mImageResource1;
    }
    public int getmImageResource2() {
        return mImageResource2;
    }

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }

}
