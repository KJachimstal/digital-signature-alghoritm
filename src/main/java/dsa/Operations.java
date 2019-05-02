package dsa;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class Operations {
    public static Block[] generateBlocks(byte[] data, int length) {
        Block[] blocks = new Block[(int)Math.ceil(data.length / (double)length)];

        byte[] tmp = new byte[length];
        int b = 0, i;

        for (i = 0; i < data.length; i++) {
            tmp[i % length] = data[i];
            if (i % length == length - 1) {
                BigInteger number = new BigInteger(tmp.clone());
                blocks[b++] = new Block(number, length);
                Arrays.fill(tmp, (byte)0);
            }
        }

        if (i % length != 0) {
            int ptr = 0;
            byte[] f_block = new byte[length];
            Arrays.fill(f_block, (byte)0);
            for (int j = length - (i % length); j < length; j++) {
                f_block[j] = tmp[ptr++];
            }

            BigInteger number = new BigInteger(f_block);
            blocks[blocks.length - 1] = new Block(number, length);
        }

        return blocks;
    }

    public static byte[] blocksToBytes(Block[] blocks, int length) {
        byte[] output = new byte[blocks.length * length];

        int n = 0;
        for (Block b : blocks) {
            byte[] data = b.getData();
            for (int i = 0; i < data.length; i++) {
                output[n++] = (byte)((short)data[i] & 0xff);
            }
        }
        return output;
    }

    public static byte[] fillArray(byte[] data, int length) {
        if (data.length > length) {
            return data;
        }
        byte[] out = new byte[length];
        Arrays.fill(out, (byte) 0);

        int ptr = 0;
        for (int i = out.length - data.length; i < out.length; i++){
            out[i] = data[ptr++];
        }

        return out.clone();
    }

    public static BigInteger Hash(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(message.getBytes(), 0, message.length());
            BigInteger res = new BigInteger(1, md.digest());
            return res;
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}
