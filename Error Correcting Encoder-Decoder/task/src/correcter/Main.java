package correcter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class App {
    private final Scanner scanner = new Scanner(System.in);
//    private final Decoder decoder = new Decoder();
//    private final Encoder encoder = new Encoder();
    private final HammingEncoder encoder = new HammingEncoder();
    private final HammingDecoder decoder = new HammingDecoder();
    private final Transmitter transmitter = new Transmitter();

    public void run() {

        System.out.println("Write a mode: ");
        switch (scanner.nextLine()) {
            case "encode":
                try {
                    String inputString = new String(readFile("send.txt"));
                    byte[] encodedString = encoder.encodeString(inputString);
                    writeToFile("encoded.txt", encodedString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "send":
                try {
                    byte[] encodedString = readFile("encoded.txt");
                    byte[] transmittedString = transmitter.transmitWithMistake(encodedString);
                    writeToFile("received.txt", transmittedString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "decode":
                try {
                    byte[] receivedString = readFile("received.txt");
                    String result = decoder.decodeString(receivedString);
                    writeToFile("decoded.txt", result.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public byte[] readFile(String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(fileName));
    }

    public void writeToFile(String fileName, byte[] encodeString) throws IOException {
        FileOutputStream writer = new FileOutputStream(fileName);
        writer.write(encodeString);
        writer.close();
    }
}