package correcter;

import java.util.ArrayList;
import java.util.List;

public class Decoder {

    public String decodeString(byte[] encodedString) {
        List<Integer> receivedList = byteArrayToList(encodedString);
        return listToString(receivedList);
    }

    private List<Integer> fixByteError(byte b) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((b & 0xC0) >> 6);
        bytes[1] = (byte) ((b & 0x30) >> 4);
        bytes[2]= (byte) ((b & 0x0C) >> 2);
        bytes[3] = (byte) ((b & 0x03)); // parity byte

        if (bytes[3] == 0x00 || bytes[3] == 0x03) {
            int corruptedByte = -1;
            byte fixedValue = bytes[3]; // parity byte
            for (int i = 0; i < bytes.length - 1; i++) {
                if (bytes[i] == 0x00 || bytes[i] == 0x03) {
                    fixedValue ^= bytes[i];
                } else {
                    corruptedByte = i;
                }
            }
            if(corruptedByte != -1) {
                bytes[corruptedByte] = fixedValue;
            }
        }

        List<Integer> triplex = new ArrayList<>();
        for (int i = 0; i < bytes.length - 1; i++) {
            triplex.add(bytes[i] == 0 ? 0 : 1);
        }
        return triplex;
    }

    private List<Integer> byteArrayToList(byte[] bytes) {
        List<Integer> ret = new ArrayList<>();
        for (byte b : bytes) {
            ret.addAll(fixByteError(b));
        }
        return ret.subList(0, ret.size() - ret.size() % 8);
    }

    private String listToString(List<Integer> list) {
        StringBuilder retString = new StringBuilder();
        char b = 0;
        int shift = 7;

        for (int integer : list) {
            b |= integer << shift;
            shift--;
            if (shift < 0) {
                retString.append(b);
                shift = 7;
                b = 0;
            }
        }
        return retString.toString();
    }
}

class HammingDecoder {
    public String decodeString(byte[] encodedString) {
        List<Integer> receivedList = byteArrayToList(encodedString);
        return listToString(receivedList);
    }

    private List<Integer> byteArrayToList(byte[] bytes) {
        List<Integer> ret = new ArrayList<>();
        for (byte b : bytes) {
            ret.addAll(fixByteError(b));
        }
        return ret.subList(0, ret.size() - ret.size() % 8);
    }

    private int getBitValue(byte val, int pos) {
        return (val & (1 << pos)) == 0 ? 0 : 1;
    }

    private byte inverseBit(byte val, int pos) {
        return (byte) (val ^ (1 << pos));
    }

    private List<Integer> fixByteError(byte b) {
        int p1 = (getBitValue(b, 5)
                + getBitValue(b, 3)
                + getBitValue(b, 1)
            ) % 2 == 0 ? 0 : 1;
        int p2 = (getBitValue(b, 5)
                + getBitValue(b, 2)
                + getBitValue(b, 1)
            ) % 2 == 0 ? 0 : 1;
        int p4 = (getBitValue(b, 3)
                + getBitValue(b, 2)
                + getBitValue(b, 1)
            ) % 2 == 0 ? 0 : 1;

        int errorPos = (p1 == getBitValue(b,7) ? 0 : 1)
                + (p2 == getBitValue(b,6) ? 0 : 2)
                + (p4 == getBitValue(b,4) ? 0 : 4);

        if (errorPos != 0) {
            b = inverseBit(b, (7 - errorPos) + 1);
        }

        List<Integer> retList = new ArrayList<>(4);
        retList.add(getBitValue(b,5));
        retList.add(getBitValue(b,3));
        retList.add(getBitValue(b,2));
        retList.add(getBitValue(b,1));
        return retList;
    }

    private String listToString(List<Integer> list) {
        StringBuilder retString = new StringBuilder();
        char b = 0;
        int shift = 7;

        for (int integer : list) {
            b |= integer << shift;
            shift--;
            if (shift < 0) {
                retString.append(b);
                shift = 7;
                b = 0;
            }
        }
        return retString.toString();
    }
}
