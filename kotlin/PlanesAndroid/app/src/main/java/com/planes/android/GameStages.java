package com.planes.android;

public enum GameStages {
    GameNotStarted(2),
    BoardEditing(0),
    Game(1);

    private final int value;

    GameStages(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
