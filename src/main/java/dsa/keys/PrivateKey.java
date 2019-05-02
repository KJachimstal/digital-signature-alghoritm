package dsa.keys;

import dsa.Key;

import java.math.BigInteger;

public class PrivateKey implements Key {
    private BigInteger a;
    private BigInteger p;
    private BigInteger q;
    private BigInteger g;

    public PrivateKey(BigInteger a, BigInteger p, BigInteger q, BigInteger g) {
        this.a = a;
        this.p = p;
        this.q = q;
        this.g = g;
    }

    public BigInteger getA() {
        return a;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getG() { return g; }

    public byte[] getBytes() {
        return toString().getBytes();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(a.toString());
        sb.append("%");
        sb.append(p.toString());
        sb.append("%");
        sb.append(q.toString());

        return sb.toString();
    }

    public static String getPattern() {
        return "^([0-9]+)\\%([0-9]+)\\%([0-9]+)$";
    }

    public int getMaxLength() {
        return p.toString().length() - 1;
    }

    public int getFillSize() {
        return getMaxLength() * 3;
    }
}
