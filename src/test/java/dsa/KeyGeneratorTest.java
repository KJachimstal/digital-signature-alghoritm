package dsa;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @Test
    void generateBigInteger() {
        BigInteger result = KeyGenerator.generatePrimeNumber(64);
        assertEquals(64, result.bitLength() );
    }
}