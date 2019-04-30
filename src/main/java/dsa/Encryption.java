package dsa;

import dsa.keys.PublicKey;

import java.math.BigInteger;
import java.util.Random;

public class Encryption extends Cryptography {
    private KeyGenerator keyGenerator;
    private BigInteger r, s1, s2;

    public Encryption(int length) {
        keyGenerator = new KeyGenerator(length);
        r = generateR(keyGenerator.getQ());
    }

    public void encrypt() {
    }

    public BigInteger generateR(BigInteger q) {
        Random rng = new Random();
        int range = 1 + rng.nextInt(keyGenerator.getQ().subtract(BigInteger.ONE).toString().length());
        BigInteger result = new BigInteger(range, rng);

        result = result.modInverse(q);

        return result;
    }
}
