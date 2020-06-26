package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        System.out.println(input);
        String encodedMessage = encodeMessage(input);
        System.out.println(encodedMessage);
        String errorMessage = emulateError(encodedMessage);
        System.out.println(errorMessage);

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

    public static String emulateError(String input) {

        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789";
        int alphabetLength = alphabet.length();
        Random random = new Random();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); i += 3) {
            StringBuilder subString = new StringBuilder(i + 3 > input.length() 
                                                        ? input.substring(i) : input.substring(i, i + 3));
            int pos = random.nextInt(3);
            char error = alphabet.charAt(random.nextInt(alphabetLength));
            subString.setCharAt(pos, error);

            output.append(subString);
        }

        return output.toString();
    }
}
