package dsa;

import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class KeyGenerator {

    private BigInteger p, g, q, b, a;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    private int p_length;
    private int q_length;

    private static final int CERTAINTY = 10;

    public KeyGenerator(int p_length, int q_length) {
        this.p_length = p_length;
        this.q_length = q_length;
    }

    public void generate() {
        q = generateQ();
        p = generateP();
        g = generateG();
        a = generateA();
        b = generateB();

        publicKey = new PublicKey(p, g, q, b);
        privateKey = new PrivateKey(a, p, q, g);
    }

    public BigInteger generateQ() {
        SecureRandom rng = new SecureRandom();
        BigInteger result;
        do {
            result = new BigInteger(q_length, CERTAINTY, rng);
        } while (result.bitLength() != q_length);
        return result;
    }

    public BigInteger generateP() {
        SecureRandom rng = new SecureRandom();
        BigInteger tempP;
        BigInteger tempP2;
        do {
            tempP = new BigInteger(p_length, 20, rng);
            tempP2 = tempP.subtract(BigInteger.ONE);
            tempP = tempP.subtract(tempP2.remainder(q));
        } while(!tempP.isProbablePrime(CERTAINTY) || tempP.bitLength() != p_length);
        return tempP;
    }


    public  BigInteger generateG() {
        BigInteger h = generateH();
        return h.modPow(p.subtract(BigInteger.ONE).divide(q), p);
    }

    public BigInteger generateA() {
        Random rng = new Random();
        BigInteger result;
        int range = 1 + rng.nextInt(q.bitLength() - 2);
        do {
            result = new BigInteger(range, rng);
        } while (result.equals(BigInteger.ZERO));

        return result;
    }

    public BigInteger generateH() {
        SecureRandom rng = new SecureRandom();
        BigInteger result;
        do {
            result = new BigInteger(p.bitLength() - 1, CERTAINTY, rng);
        } while (result.modPow(p.subtract(BigInteger.ONE).divide(q), p).compareTo(BigInteger.ONE) != 1);
        return result;
    }

    public BigInteger generateB() {
        return g.modPow(a, p);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
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

    public BigInteger getA() {
        return a;
    }
}
