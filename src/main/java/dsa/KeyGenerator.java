package dsa;

import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;

import java.math.BigInteger;
import java.util.Random;

public class KeyGenerator {

    private BigInteger p, h, q, b, a;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    private int length;

    public KeyGenerator(int length) {
        this.length = length;
    }

    public void generate() {
        p = generatePrimeNumber();
        q = generateQ();
        h = generateH();
        a = generateA();
        b = generateB();
        publicKey = new PublicKey(p, h, q, b);
        privateKey = new PrivateKey(a, p, q, h);
    }

    public BigInteger generatePrimeNumber() {
        Random rng = new Random();
        BigInteger result;
        do {
            result = new BigInteger(length, rng);
        } while (!result.isProbablePrime(100) || result.bitLength() != length);

        return result;
    }

    public BigInteger generateQ() {
        Random rng = new Random();
        BigInteger result;
        boolean remainder = true;
        do {
            result = new BigInteger(160, rng);
            remainder = p.subtract(BigInteger.ONE).mod(result).equals(BigInteger.ZERO);
        } while (!result.isProbablePrime(100) || result.bitLength() != 160 || remainder);
        return result;
    }

    public  BigInteger generateH() {
        Random rng = new Random();
        BigInteger result;
        int range = 1 + rng.nextInt(p.bitLength() - 2);
        do {
            result = new BigInteger(range, rng);
            if (result.modPow(q, p).equals(BigInteger.ONE)) {
                continue;
            }
        } while (!result.isProbablePrime(100));
        return result;
    }

    public BigInteger generateA() {
        Random rng = new Random();
        int range = 1 + rng.nextInt(q.bitLength() - 2);
        BigInteger result = new BigInteger(range, rng);

        return result;
    }

    public BigInteger generateB() {
        BigInteger result;
        result = h.modPow(a, p);

        return result;
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

    public BigInteger getH() {
        return h;
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
