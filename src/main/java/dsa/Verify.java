package dsa;

import dsa.exceptions.CorruptedDataException;
import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;

import java.math.BigInteger;

public class Verify extends Cryptography {
    private PublicKey publicKey;
    private BigInteger t;
    private BigInteger[] encrypted, data;


    public Verify(BigInteger[] encrypted, BigInteger[] data, PublicKey publicKey) {
        this.encrypted = encrypted;
        this.publicKey = publicKey;
        this.data = data;
    }

    public boolean check() {
        for (int i = 0; i < encrypted.length / 2; i++) {
            BigInteger s = generateS(i);
            BigInteger u1 = data[i].multiply(s).mod(publicKey.getQ());
            BigInteger u2 = s.multiply(encrypted[i * 2]).mod(publicKey.getQ());
            t = publicKey.getH().modPow(u1, publicKey.getP())
                .multiply(publicKey.getB().modPow(u2, publicKey.getP())
                .mod(publicKey.getP()).mod(publicKey.getP()));
            if (!t.equals(encrypted[i * 2])) {
                return false;
            }
        }

        return true;
    }

    public BigInteger generateS(int i) {
        return encrypted[i * 2 + 1].modInverse(publicKey.getQ());
    }
}
