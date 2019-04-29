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
        p = generatePrimeNumber(length);
        q = generateQ();
    }

    public static BigInteger generatePrimeNumber(int length) {
        Random rng = new Random();
        BigInteger result;

        do {
            result = new BigInteger(length, rng);
        } while (!result.isProbablePrime(100) || result.bitLength() != 64);

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


    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
