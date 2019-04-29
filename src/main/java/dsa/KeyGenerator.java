package dsa;

import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;

import java.math.BigInteger;
import java.util.Random;

public class KeyGenerator {

    BigInteger p, h, q, b, a;
    PublicKey publicKey;
    PrivateKey privateKey;
    int length;

    public KeyGenerator(int length) {
        this.length = length;
    }

    public void generate() {
       // TODO: Implement generate method in KeyGenerator
    }

    public static BigInteger generatePrimeNumber(int length) {
        Random rng = new Random();
        BigInteger result;

        do {
            result = new BigInteger(length, rng);
        } while (!result.isProbablePrime(100) || result.bitLength() != 64);

        return result;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
