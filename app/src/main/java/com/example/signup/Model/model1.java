package com.example.signup.Model;

import java.util.ArrayList;

public class model1 {

    public enum STATE {
        CLOSED,
        OPENED
    }

   public String name;
   public int level;
  public   STATE state = STATE.CLOSED;
 public   ArrayList<model1> models = new ArrayList<>();
boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public model1(String name, int level ) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }
}
