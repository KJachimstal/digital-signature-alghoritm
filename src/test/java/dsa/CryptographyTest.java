package dsa;

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
        System.out.println("Dane: " + Arrays.toString(data));
//        System.out.println("Hash: " + Arrays.toString(Operations.getHash(data)));
        System.out.println("-----------------------------------");



        KeyGenerator keyGenerator = new KeyGenerator(1024);
        keyGenerator.generate();
        publicKey = keyGenerator.getPublicKey();
        privateKey = keyGenerator.getPrivateKey();
        blocks = Operations.generateBlocks(data, privateKey.getMaxLength());
        System.out.println(Arrays.toString(blocks));
    }

    @Test
    void encrypt() throws NoSuchAlgorithmException {
        sign = new Sign(blocks, privateKey);
        sign.encrypt();

        System.out.println("Dokument podpisany: \n" + Arrays.toString(sign.getResults()));
        verify = new Verify(sign.getResults(), blocks, publicKey);
        System.out.println("----------------------VERIFY-----------------------");
        System.out.println("Dokument niepodpisany: \n" + Arrays.toString(verify.getData()));
        System.out.println("Dokument podpisany: \n" + Arrays.toString(verify.getEncrypted()));
        verify.check();

        assertTrue(verify.check());
    }
}