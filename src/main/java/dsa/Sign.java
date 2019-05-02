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
            BigInteger p = privateKey.getP();
            BigInteger q = privateKey.getQ();
            BigInteger g = privateKey.getG();
            BigInteger a = privateKey.getA();

            s1 = (g.modPow(r, p)).mod(q);
            s2 = (r.modInverse(q).multiply((m.add(a.multiply(s1))).mod(q))).mod(q);

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
        return r.modInverse(privateKey.getQ());
    }

    public Block[] getResults() {
        return results;
    }

}
