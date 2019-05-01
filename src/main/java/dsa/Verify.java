package dsa;

import dsa.exceptions.CorruptedDataException;
import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;

import java.math.BigInteger;

public class Verify extends Cryptography {
    private PublicKey publicKey;
    private BigInteger t;
    private Block[] encrypted, data;


    public Verify(Block[] encrypted, Block[] data, PublicKey publicKey) {
        this.encrypted = encrypted;
        this.publicKey = publicKey;
        this.data = data;
    }

    public boolean check() {
        for (int i = 0; i < encrypted.length / 2; i++) {
            BigInteger s = generateS(i);
            BigInteger m = data[i].getBigInteger();
            BigInteger s1 = encrypted[i * 2].getBigInteger();

            BigInteger u1 = m.multiply(s).mod(publicKey.getQ());
            BigInteger u2 = s.multiply(s1).mod(publicKey.getQ());
            t = ((publicKey.getH().modPow(u1, publicKey.getP()))
                .multiply(publicKey.getB().modPow(u2, publicKey.getP())))
                .mod(publicKey.getP()).mod(publicKey.getQ());
            System.out.println("T: "+t);
            System.out.println("S1: "+s1);
            if (!t.equals(s1)) {
                return false;
            }
        }

        return true;
    }

    public BigInteger generateS(int i) {
        return encrypted[i * 2 + 1].getBigInteger().modInverse(publicKey.getQ());
    }

    public BigInteger getT() {
        return t;
    }

    public Block[] getEncrypted() {
        return encrypted;
    }

    public Block[] getData() {
        return data;
    }
}
