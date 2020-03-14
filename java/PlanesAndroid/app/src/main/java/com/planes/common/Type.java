package com.planes.common;

//guesspoint type
public enum Type {
    Miss(0),
    Hit(1),
    Dead(2);

    private final int value;

    Type(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
