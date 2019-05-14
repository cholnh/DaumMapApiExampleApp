package com.pomangam.exampleapp.map.domain;

public enum MapState {
    PLAY(1),
    PAUSE(2),
    STOP(3),
    ERROR(4);

    private Integer value;

    MapState(Integer value) {
        this.value = value;
    }
}
