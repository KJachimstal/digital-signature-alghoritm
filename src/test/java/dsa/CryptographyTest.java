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
    BigInteger rPrim, r;

    @BeforeEach
    void initialize(){
        data = new byte[] {1, 2, 3, 15, (byte)254};
        System.out.println(Arrays.toString(data));
        System.out.println(Arrays.toString(Operations.getHash(data)));
        System.out.println("-----------------------------------");


        KeyGenerator keyGenerator = new KeyGenerator(30);
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
        rPrim = sign.getrPrim();

        BigInteger z = new BigInteger("15", 16);
        System.out.println("Z: " + z);

        System.out.println("Dokument podpisany: \n" + Arrays.toString(sign.getResults()));
        verify = new Verify(sign.getResults(), blocks, publicKey, rPrim);
        System.out.println("----------------------VERIFY-----------------------");
        System.out.println("Dokument niepodpisany: \n" + Arrays.toString(verify.getData()));
        System.out.println("Dokument podpisany: \n" + Arrays.toString(verify.getEncrypted()));
        verify.check();
//        System.out.println("T: \n"+verify.getT());



        assertTrue(verify.check());
    }
}