package dsa;

import dsa.keys.PublicKey;
import java.math.BigInteger;

public class Verify extends Cryptography {
    private PublicKey publicKey;
    private BigInteger t, rPrim;
    private Block[] encrypted, data;

    // TODO: Delete rPrim in constructor in Verify class
    public Verify(Block[] encrypted, Block[] data, PublicKey publicKey, BigInteger rPrim) {
        this.encrypted = encrypted;
        this.publicKey = publicKey;
        this.data = data;
        this.rPrim = rPrim;
    }

    public boolean check() {
        for (int i = 0; i < encrypted.length / 2; i++) {
            BigInteger s = generateS(i); // sPrim
            BigInteger m = data[i].getBigInteger();
            BigInteger s1 = encrypted[i * 2].getBigInteger();

            // Ours
            BigInteger u1 = m.multiply(s).mod(publicKey.getQ());
            BigInteger u2 = s.multiply(s1).mod(publicKey.getQ());
            t = ((publicKey.getH().modPow(u1, publicKey.getP()))
                .multiply(publicKey.getB().modPow(u2, publicKey.getP())))
                .mod(publicKey.getP()).mod(publicKey.getQ());

            // Git
//            BigInteger pKey = new BigInteger(publicKey.toStringS());
//
//            BigInteger u1 = m.multiply(s).mod(publicKey.getQ());
//            BigInteger u2 = rPrim.multiply(s).mod(publicKey.getQ());
//
//            t = ((publicKey.getH().modPow(u1, publicKey.getP())
//                    .multiply(pKey.modPow(u2, publicKey.getP())))
//                    .mod(publicKey.getP()))
//                    .mod(publicKey.getQ());

            System.out.println("T: " + t);
            System.out.println("S1: " + s1);
            if (!t.equals(s1)) {
                return false;
            }
        }

        return true;
    }

    public BigInteger generateS(int i) {
        return encrypted[i * 2 + 1].getBigInteger().modInverse(publicKey.getQ());
    }

    public Block[] getEncrypted() {
        return encrypted;
    }

    public Block[] getData() {
        return data;
    }
}
