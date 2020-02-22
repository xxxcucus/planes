package com.planes.android;

public interface ObjectWithStringState {
    void setState(String stateName, String text);
    String getCurrentStateName();
}
