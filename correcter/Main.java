package correcter;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Random;

public class Main {

    public static void main(String[] args) {

        String inputFile = "send.txt";
        String outputFile = "received.txt";

        try(FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile)){

            byte[] input = inputStream.readAllBytes();
            byte[] output = emulateError(input);
            outputStream.write(output);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static String encodeMessage(String input) {

        StringBuilder sb = new StringBuilder();

        for(char c : input.toCharArray()) {
            sb.append(c);
            sb.append(c);
            sb.append(c);
        }

        return sb.toString();
    }

    public static String decodeMessage(String input) {
        StringBuilder output = new StringBuilder();

        for(int i = 0; i < input.length(); i += 3) {
            char a = input.charAt(i);
            char b = input.charAt(i + 1);
            char c = input.charAt(i + 2);

            char o = (a == b || a == c) ? a : b;

            output.append(o);
        }

        return output.toString();
    }

    public static byte[] emulateError(byte[] input) {

        Random random = new Random();
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            int r = random.nextInt(8);

            output[i] = (byte) (input[i] ^ (1 << r));
        }

        return output;
    }
}