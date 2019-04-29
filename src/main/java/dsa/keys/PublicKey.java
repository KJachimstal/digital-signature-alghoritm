package dsa.keys;

import dsa.Key;

import java.math.BigInteger;

public class PublicKey implements Key {
    BigInteger p, g, h;

    public PublicKey(BigInteger p, BigInteger g, BigInteger h) {
        this.p = p;
        this.g = g;
        this.h = h;
        // TODO: Implement DSA values in PublicKey
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getH() {
        return h;
    }

    public byte[] getBytes() {
        return toString().getBytes();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(p.toString());
        sb.append("%");
        sb.append(g.toString());
        sb.append("%");
        sb.append(h.toString());
        return sb.toString();
    }

    public static String getPattern() {
        return "^([0-9]+)\\%([0-9]+)\\%([0-9]+)$";
    }

    public int getMaxLength() {
        return p.toString().length() - 1;
    }
}
