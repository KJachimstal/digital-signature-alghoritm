package dsa;

import dsa.keys.PrivateKey;

import java.math.BigInteger;
import java.util.Random;

public class Sign extends Cryptography {
    private PrivateKey privateKey;
    private BigInteger r, s1, s2;
    private Block[] results;
    private Block[] data;

    public Sign(Block[] data, PrivateKey privateKey) {
        r = generateR(privateKey.getQ());
        this.data = data;
        this.privateKey = privateKey;
    }

    public void encrypt() {
        results = new Block[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            BigInteger m = new BigInteger(data[i].getData());
            s1 = privateKey.getH().modPow(r, privateKey.getP()).mod(privateKey.getQ());
            s2 = r.multiply(m.add(privateKey.getA().multiply(s1))).mod(privateKey.getQ());

            results[i * 2] = new Block(s1, privateKey.getFillSize());
            results[i * 2 + 1] = new Block(s2, privateKey.getFillSize());
        }
    }

    public BigInteger generateR(BigInteger q) {
        Random rng = new Random();
        int range = 1 + rng.nextInt(privateKey.getQ().subtract(BigInteger.ONE).toString().length());
        BigInteger result = new BigInteger(range, rng);

        return result.modInverse(q);
    }

    public Block[] getResults() {
        return results;
    }
}
