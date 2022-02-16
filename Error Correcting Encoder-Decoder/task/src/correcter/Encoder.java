package correcter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Encoder {

    public byte[] encodeString(String string) {
        List<Integer> list = stringToBitList(string);
        return listToByteArray(list);
    }

    private byte[] listToByteArray(List<Integer> list) {
        byte[] byteList = new byte[list.size() / 3];
        for (int i = 0; i < list.size(); i += 3) {
            byte b = triplexToByte(list.subList(i, i + 3));
            byteList[i / 3] = b;
        }
        return byteList;
    }

    private List<Integer> stringToBitList(String string) {
        List<Integer> list = new ArrayList<>();
        for (byte b : string.getBytes()) {
            list.addAll(byteToList(b));
        }
        while (list.size() % 3 != 0) {
            list.add(0);
        }
        return list;
    }

    private List<Integer> byteToList(byte c) {
        List<Integer> list = new ArrayList<>();
        for (int i = 7; i >= 0; i--) {
            int bit = c & (1 << i);
            list.add(bit == 0 ? 0 : 1);
        }
        return list;
    }

    private byte triplexToByte(List<Integer> triplex) {
        byte b = 0;
        b |= (byte) (triplex.get(0) << 7);
        b |= (byte) (triplex.get(0) << 6);
        b |= (byte) (triplex.get(1) << 5);
        b |= (byte) (triplex.get(1) << 4);
        b |= (byte) (triplex.get(2) << 3);
        b |= (byte) (triplex.get(2) << 2);
        b |= calcParityBit(triplex);
        return b;
    }

    private byte calcParityBit(List<Integer> triplex) {
        byte b = 0;
        for (int val : triplex) {
            b ^= val == 0x00 ? 0x00 : 0x03;
        }
        return b;
    }
}

class HammingEncoder {

    public byte[] encodeString(String string) {
        List<Integer> list = stringToBitList(string);
        return listToByteArray(list);
    }

    private List<Integer> charToBitArray(byte ch) {
        List<Integer> array = new ArrayList<>(8);
        for (int i = 0; i < 8 ; i++) {
            array.add((ch >> i) % 2 == 0 ? 0 : 1);
        }
        Collections.reverse(array);
        return array;
    }

    private List<Integer> stringToBitList(String string) {
        List<Integer> array = new ArrayList<>();
        for (byte ch : string.getBytes()) {
            array.addAll(charToBitArray(ch));
        }
        return array;
    }

    private byte[] listToByteArray(List<Integer> list) {
        byte[] byteList = new byte[list.size() / 4];
        for (int i = 0; i < list.size(); i += 4) {
            byte b = hammingCode(list.subList(i, i + 4));
            byteList[i / 4] = b;
        }
        return byteList;
    }

    private byte setBit(byte num, int pos) {
        return (byte) (num | (1 << pos));
    }

    private byte clearBit(byte num, int pos) {
        return (byte) (num & ~(1 << pos));
    }

    private byte hammingCode(List<Integer> list) {
        int p1 = (list.get(0) + list.get(1) + list.get(3)) % 2 == 0 ? 0 : 1;
        int p2 = (list.get(0) + list.get(2) + list.get(3)) % 2 == 0 ? 0 : 1;
        int p4 = (list.get(1) + list.get(2) + list.get(3)) % 2 == 0 ? 0 : 1;

        byte retByte = 0;
        if (p1 != 0) retByte = setBit(retByte, 7);
        if (p2 != 0) retByte = setBit(retByte, 6);
        if (list.get(0) != 0) retByte = setBit(retByte, 5);
        if (p4 != 0) retByte = setBit(retByte, 4);
        if (list.get(1)  != 0) retByte = setBit(retByte, 3);
        if (list.get(2)  != 0) retByte = setBit(retByte, 2);
        if (list.get(3)  != 0) retByte = setBit(retByte, 1);

        return retByte;
    }
}