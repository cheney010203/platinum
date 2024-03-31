package com.hzy.platinum.media.dmr;

import java.util.ArrayList;
import java.util.List;

public enum TransportAction {

    Play,
    Stop,
    Pause,
    Seek,
    Next,
    Previous,
    Record;

    public static TransportAction[] valueOfCommaSeparatedList(String s) {
        String[] strings = ModelUtil.fromCommaSeparatedList(s);
        if (strings == null) return new TransportAction[0];
        List<TransportAction> result = new ArrayList<>();
        for (String taString : strings) {
            for (TransportAction ta : values()) {
                if (ta.name().equals(taString)) {
                    result.add(ta);
                }
            }

        }
        return result.toArray(new TransportAction[result.size()]);
    }
}
