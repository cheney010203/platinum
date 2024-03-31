package com.hzy.platinum.media.dmr;

public class UnsignedIntegerTwoBytes extends UnsignedVariableInteger {

    public UnsignedIntegerTwoBytes(long value) throws NumberFormatException {
        super(value);
    }

    public UnsignedIntegerTwoBytes(String s) throws NumberFormatException {
        super(s);
    }

    public Bits getBits() {
        return Bits.SIXTEEN;
    }

}
