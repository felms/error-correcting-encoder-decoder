package correcter;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String fileToEncode = "send.txt";
        String encodedFile = "encoded.txt";
        String corruptedFile = "received.txt";
        String decodedFile = "decoded.txt";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Write a mode: ");
        String mode = scanner.nextLine();

        String inputFile;
        String outputFile;

        if ("encode".equalsIgnoreCase(mode)) {
            inputFile = fileToEncode;
            outputFile = encodedFile;
        } else if ("send".equalsIgnoreCase(mode)) {
            inputFile = encodedFile;
            outputFile = corruptedFile;
        } else {
            inputFile = corruptedFile;
            outputFile = decodedFile;
        }

        try(FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile)){
            
            byte[] input = inputStream.readAllBytes();
            byte[] output;

            if ("encode".equalsIgnoreCase(mode)) {
                output = encodeMessage(input);
            } else if ("send".equalsIgnoreCase(mode)) {
                output = emulateError(input);
            } else {
                output = decodeMessage(input);
            }            
            
            outputStream.write(output);
            printResult(input, output, inputFile, outputFile);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static byte[] encodeMessage(byte[] input) {

        List<Byte> byteList = new ArrayList<>();
        int[] bits = new int[3];

        int newByte = 0;
        int addedBits = 0;

        for (int byteCount = 0; byteCount < input.length; byteCount++) {

            byte b = input[byteCount];

            for (int i = 0; i < 8; i++) {
                int currentBit =  b >>> (7 - i);
                currentBit = currentBit & 1;
                bits[addedBits] = currentBit;

                int duplicatedBit = currentBit << 1 | currentBit;
                newByte = newByte << 2 | duplicatedBit;
                addedBits++;

                if (addedBits > 2) {
                    int parityBit = bits[0] ^ bits[1] ^ bits[2];
                    parityBit = parityBit << 1 | parityBit;
                    newByte = newByte << 2 | parityBit;
                    byteList.add((byte)newByte);

                    addedBits = 0;
                    newByte = 0;
                    bits[0] = 0;
                    bits[1] = 0;
                    bits[2] = 0;
                }

            }

            if (byteCount == input.length - 1) {
                
                while (addedBits != 0) {
                    bits[addedBits] = 0;
                    newByte = newByte << 2;
                    addedBits++;

                    if (addedBits > 2) {
                        int parityBit = bits[0] ^ bits[1] ^ bits[2];
                        parityBit = parityBit << 1 | parityBit;
                        newByte = newByte << 2 | parityBit;
                        byteList.add((byte)newByte);
    
                        addedBits = 0;
                        newByte = 0;
                        bits[0] = 0;
                        bits[1] = 0;
                        bits[2] = 0;
                    }
                }
            }
        }

        byte[] output = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            output[i] = byteList.get(i);
        }

        return output;
    }

    public static byte[] decodeMessage(byte[] input) {
        
        int[] bits = new int[8];
        int newByte = 0;
        int addedBits = 0;
        List<Byte> byteList = new ArrayList<>();

        for (byte b : input) {

            for (int i = 0; i < 8; i++) {
                int currentBit =  b >>> (7 - i);
                currentBit = currentBit & 1;
                bits[i] = currentBit;
            }

            int bit0 = bits[0];
            int bit1 = bits[2];
            int bit2 = bits[4];

            if (bits[0] != bits[1]) {
                bit0 = bits[2] ^ bits[4] ^ bits[6];
            } else if (bits[2] != bits[3]) {
                bit1 = bits[0] ^ bits[4] ^ bits[6];
            } else if (bits[4] != bits[5]) {
                bit2 = bits[0] ^ bits[2] ^ bits[6];
            }

            newByte = newByte << 1 | bit0;
            newByte = newByte << 1 | bit1;
            newByte = newByte << 1 | bit2;

            addedBits += 3;

            if (addedBits >= 8) {
                int extraBits = (newByte << 8) >>> 8;
                byteList.add((byte) (newByte >>> (addedBits - 8)));
                
                newByte = extraBits;
                addedBits = addedBits - 8;
            }
           
        }

        byte[] output = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            output[i] = byteList.get(i);
        }

        return output;
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

    public static void printResult(byte[] input, byte[] output, String inputFile, String outputFile) {
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n" + inputFile + ":\n");
        sb.append("hex view: ");
        for (byte b : input) {
            sb.append((Integer.toHexString(b & 0xFF).toUpperCase() + " "));
        }

        sb.append("\nbin view: ");
        for (byte b : input) {
            sb.append((String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0') + " "));
        }

        sb.append("\n\n" + outputFile + ":\n");
        sb.append("hex view: ");
        for (byte b : output) {
            sb.append((Integer.toHexString(b & 0xFF).toUpperCase() + " "));
        }

        sb.append("\nbin view: ");
        for (byte b : output) {
            sb.append((String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0') + " "));
        }

        System.out.println(sb.toString());

    }
}