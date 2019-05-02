package dsa;

import dsa.keys.PublicKey;
import java.math.BigInteger;

public class Verify extends Cryptography {
    private PublicKey publicKey;
    private Block[] encrypted, data;

    public Verify(Block[] encrypted, Block[] data, PublicKey publicKey) {
        this.encrypted = encrypted;
        this.publicKey = publicKey;
        this.data = data;
    }

    public boolean check() {
        for (int i = 0; i < encrypted.length / 2; i++) {
            BigInteger p = publicKey.getP();
            BigInteger q = publicKey.getQ();
            BigInteger g = publicKey.getG();
            BigInteger b = publicKey.getB();

            BigInteger m = Operations.Hash(data[i].toString());
            BigInteger s1 = encrypted[i * 2].getBigInteger();
            BigInteger s2 = encrypted[i * 2 + 1].getBigInteger();

            BigInteger w = s2.modInverse(q);

            BigInteger u1 = m.multiply(w).mod(q);
            BigInteger u2 = s1.multiply(w).mod(q);
            BigInteger t = (g.modPow(u1, p).multiply(b.modPow(u2, p))).mod(p).mod(q);

            if (!t.equals(s1)) {
                return false;
            }
        }

        return true;
    }
}
