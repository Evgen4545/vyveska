package com.boom.vyveska.data;

import java.io.Serializable;

public class CartInfo implements Serializable {

    String text;

    String count;

    String id;
    public CartInfo(String text, String count, String id){
        this.count = count;
        this.text=text;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
