package com.hzy.platinum.media.dmr;

public class UnsignedIntegerFourBytes extends UnsignedVariableInteger {

    public UnsignedIntegerFourBytes(long value) throws NumberFormatException {
        super(value);
    }

    public UnsignedIntegerFourBytes(String s) throws NumberFormatException {
        super(s);
    }

    public Bits getBits() {
        return Bits.THIRTYTWO;
    }

}