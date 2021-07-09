package com.planes.common;

//plane orientation
public enum Orientation {
    NorthSouth(0),
    SouthNorth(1),
    WestEast(2),
    EastWest(3);

    private final int value;

    Orientation(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

