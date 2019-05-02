package dsa;

import java.math.BigInteger;

public class Block {
    private byte[] data;
    public int fill = -1;

    public Block(byte[] data) {
//        this.data = Operations.getHash(data).clone();
        this.data = data.clone();
    }

    public Block(BigInteger number, int fill) {
        this(Operations.fillArray(number.toByteArray(), fill));
        this.fill = fill;
    }

    public BigInteger getBigInteger() {
        return new BigInteger(data);
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < data.length; i++) {
            sb.append(data[i]);
            if (i != data.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
