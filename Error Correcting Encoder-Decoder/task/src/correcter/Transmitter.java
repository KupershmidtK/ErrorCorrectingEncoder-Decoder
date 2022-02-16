package correcter;

import java.util.Random;

public class Transmitter {
//    private final static String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
    private final Random random = new Random();

    public byte[] transmitWithMistake(byte[] pack) {
        byte[] transmittedPack = new byte[pack.length];
        for (int i = 0; i < pack.length; i++) {
            transmittedPack[i] =
                    (byte) (pack[i] ^ (1 << random.nextInt(7)));
        }
        return transmittedPack;
    }
}
