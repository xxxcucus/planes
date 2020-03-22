package com.planes.common;

public enum GameStages {
    GameNotStarted(0),
    BoardEditing(1),
    Game(2);

    private final int value;

    GameStages(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}