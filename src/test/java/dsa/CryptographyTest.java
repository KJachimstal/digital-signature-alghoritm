package dsa;

import dsa.exceptions.CorruptedDataException;
import dsa.keys.PrivateKey;
import dsa.keys.PublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CryptographyTest {

    Sign sign;
    Verify verify;
    PublicKey publicKey;
    PrivateKey privateKey;
    Block[] blocks;
    byte[] data;

    @BeforeEach
    void initialize(){
        data = new byte[] {1, 2, 3, 15, (byte)254};

        KeyGenerator keyGenerator = new KeyGenerator(512, 160);
        keyGenerator.generate();

        publicKey = keyGenerator.getPublicKey();
        privateKey = keyGenerator.getPrivateKey();
        blocks = new Block[] { new Block(data) };
    }

    @Test
    void encrypt() throws CorruptedDataException {
        sign = new Sign(blocks, privateKey);
        sign.sign();

        verify = new Verify(sign.getResults(), blocks, publicKey);

        assertTrue(verify.check());

        Block[] tmp = sign.getResults();
        tmp[0].getData()[0] = 0x05;
        tmp[0].getData()[5] = 0x01;
        Verify canBeInvalid = new Verify(tmp, blocks, publicKey);
        assertFalse(canBeInvalid.check());
    }
}