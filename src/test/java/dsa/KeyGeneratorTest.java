package dsa;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    static KeyGenerator key;

    @BeforeAll
    static void initialize() {
        key = new KeyGenerator(1024, 160);
        BigInteger p, h, q, b, a;
        key.generate();
    }

    @Test
    void generatePrimeNumber() {
        assertEquals(1024, key.getP().bitLength());
    }

    @Test
    void generateQ() {
        assertEquals(160, key.getQ().bitLength());
    }

    @Test
    void generateH() {
        assertTrue(key.getG().compareTo(key.getP()) == -1);
    }

    @Test
    void generateA() {
        assertTrue(key.getA().compareTo(key.getQ()) == -1);
    }

    @Test
    void generateB() {
        assertTrue(key.getB().compareTo(key.getG().modPow(key.getA(), key.getP())) == 0);
    }
}