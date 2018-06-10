package com.example.dengchong.remotefloatmenu.rx;

public class RxEvent {
    public static final String CHANGEIMAGE = "changeImage";
    private String tag;

    public RxEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
