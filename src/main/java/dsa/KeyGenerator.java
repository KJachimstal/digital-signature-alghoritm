package dsa;

import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;

import java.math.BigInteger;

public class KeyGenerator {

    BigInteger p, g, a, h;
    PublicKey publicKey;
    PrivateKey privateKey;
    int length;

    public KeyGenerator(int length) {
        this.length = length;
    }

    public void generate() {
       // TODO: Implement generate method in KeyGenerator
    }

    public static BigInteger generateBigInteger(int min, int max) {
        // TODO: Implement generateBigInteger method in KeyGenerator
        // min, max - numbers of generated digits (change to BigInteger random number)
        return null;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
