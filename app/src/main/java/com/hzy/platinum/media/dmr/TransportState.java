package com.hzy.platinum.media.dmr;

public enum TransportState {

    STOPPED,
    PLAYING,
    TRANSITIONING,
    PAUSED_PLAYBACK,
    PAUSED_RECORDING,
    RECORDING,
    NO_MEDIA_PRESENT,
    CUSTOM;

    String value;

    TransportState() {
        this.value = name();
    }

    public String getValue() {
        return value;
    }

    public TransportState setValue(String value) {
        this.value = value;
        return this;
    }

    public static TransportState valueOrCustomOf(String s) {
        try {
            return TransportState.valueOf(s);
        } catch (IllegalArgumentException ex) {
            return TransportState.CUSTOM.setValue(s);
        }
    }
}
