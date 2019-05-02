package dsa.keys;

import dsa.Key;

import java.math.BigInteger;

public class PublicKey implements Key {
    BigInteger p, g, q, b;

    public PublicKey(BigInteger p, BigInteger g, BigInteger q, BigInteger b) {
        this.p = p;
        this.g = g;
        this.q = q;
        this.b = b;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getB() {
        return b;
    }

    public byte[] getBytes() {
        return toString().getBytes();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(p.toString());
        sb.append("%");
        sb.append(g.toString());
        sb.append("%");
        sb.append(q.toString());
        sb.append("%");
        sb.append(b.toString());

        return sb.toString();
    }

    public static String getPattern() {
        return "^([0-9]+)\\%([0-9]+)\\%([0-9]+)\\%([0-9]+)$";
    }

    public int getMaxLength() {
        return p.toString().length() - 1;
    }

    public int getFillSize() {
        return getMaxLength() * 3;
    }
}
