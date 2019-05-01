package dsa;

import dsa.keys.PrivateKey;

import java.math.BigInteger;
import java.util.Random;

public class Sign extends Cryptography {
    private PrivateKey privateKey;
    private BigInteger r, rPrim, s1, s2;
    private Block[] results;
    private Block[] data;

    public Sign(Block[] data, PrivateKey privateKey) {
        this.data = data;
        this.privateKey = privateKey;
        r = generateR();
        rPrim = generateRPrim();
    }

    public void encrypt() {
        results = new Block[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            BigInteger m = data[i].getBigInteger();

            s1 = privateKey.getH().modPow(r, privateKey.getP()).mod(privateKey.getQ());
            s2 = rPrim.multiply(m.add(privateKey.getA().multiply(s1))).mod(privateKey.getQ());

            results[i * 2] = new Block(s1, privateKey.getFillSize());
            results[i * 2 + 1] = new Block(s2, privateKey.getFillSize());
        }
    }

    public BigInteger generateR() {
        Random rng = new Random();
        BigInteger q = privateKey.getQ();
        BigInteger result;
        int range = 1 + rng.nextInt(q.bitLength() - 2);
        do {
            result = new BigInteger(range, rng);
        } while (result.equals(BigInteger.ZERO));

        return result;
    }

    public BigInteger generateRPrim() {
        System.out.println("R: "+r);
        System.out.println("Q: "+privateKey.getQ());
        return rPrim = r.modInverse(privateKey.getQ());
    }

    public Block[] getResults() {
        return results;
    }
}
