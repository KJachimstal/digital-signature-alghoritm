package dsa;

import dsa.keys.PublicKey;

import java.math.BigInteger;
import java.util.Random;

public class Encryption extends Cryptography {
    private KeyGenerator keyGenerator;
    private BigInteger r, s1, s2;
    private BigInteger[] results;
    private BigInteger[] data;

    public Encryption(BigInteger[] data, int length) {
        keyGenerator = new KeyGenerator(length);
        r = generateR(keyGenerator.getQ());
        this.data = data;
    }

    public void encrypt() {
        results = new BigInteger[data.length*2];
        for (int i = 0; i < data.length; i++) {
            BigInteger m = data[i];
            s1 = keyGenerator.getH().modPow(r, keyGenerator.getP()).mod(keyGenerator.getQ());
            s2 = r.multiply(m.add(keyGenerator.getA().multiply(s1))).mod(keyGenerator.getQ());

            results[i * 2] = s1;
            results[i * 2 + 1] = s2;
        }
    }

    public BigInteger generateR(BigInteger q) {
        Random rng = new Random();
        int range = 1 + rng.nextInt(keyGenerator.getQ().subtract(BigInteger.ONE).toString().length());
        BigInteger result = new BigInteger(range, rng);

        return result.modInverse(q);
    }
}
