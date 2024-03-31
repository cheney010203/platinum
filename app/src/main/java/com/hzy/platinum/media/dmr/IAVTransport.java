package com.hzy.platinum.media.dmr;

public interface IAVTransport {
    void setAVTransportURI(String currentURI,String currentURIMetaData);
    void setNextAVTransportURI(String currentURI,String currentURIMetaData);
    void play(String speed);
    void pause();
    void seek(String unit,String target);
    void previous();
    void next();
    void stop();
    void setMute(String channelName,Boolean desiredMute );
    void setVolume(String channelName,UnsignedIntegerTwoBytes desiredVolume);
}
