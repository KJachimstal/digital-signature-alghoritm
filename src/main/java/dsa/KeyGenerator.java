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
        System.out.println("Generate P");
        p = generatePrimeNumber();
        System.out.println("Generate Q");
        q = generateQ();
        System.out.println("Generate H");
        h = generateH();
        System.out.println("Generate A");
        a = generateA();
        System.out.println("Generate B");
        b = generateB();
        System.out.println("End generate");
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
        do {
            result = new BigInteger(160, rng);
        } while (!result.isProbablePrime(100) || result.bitLength() != 160 );
        return result;
    }

    public  BigInteger generateH() {
        Random rng = new Random();
        BigInteger result;
        int range = 1 + rng.nextInt(p.bitLength() - 2);

        do {
            result = new BigInteger(range, rng);
            BigInteger exponent = (p.subtract(BigInteger.ONE)).divide(result);
            if (result.modPow(exponent, p).equals(BigInteger.ONE)) {
                continue;
            }

        } while (result.equals(BigInteger.ZERO));
        return result;
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

    public BigInteger generateB() {
        return h.modPow(a, p);
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
