package dsa;

import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;

import java.math.BigInteger;
import java.util.Random;

public class Encryption extends Cryptography {
    private PrivateKey privateKey;
    private BigInteger r, s1, s2;
    private BigInteger[] results;
    private BigInteger[] data;

    public Encryption(BigInteger[] data, PrivateKey privateKey) {
        r = generateR(privateKey.getQ());
        this.data = data;
        this.privateKey = privateKey;
    }

    public void encrypt() {
        results = new BigInteger[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            BigInteger m = data[i];
            s1 = privateKey.getH().modPow(r, privateKey.getP()).mod(privateKey.getQ());
            s2 = r.multiply(m.add(privateKey.getA().multiply(s1))).mod(privateKey.getQ());

            results[i * 2] = s1;
            results[i * 2 + 1] = s2;
        }
    }

    public BigInteger generateR(BigInteger q) {
        Random rng = new Random();
        int range = 1 + rng.nextInt(privateKey.getQ().subtract(BigInteger.ONE).toString().length());
        BigInteger result = new BigInteger(range, rng);

        return result.modInverse(q);
    }
}
