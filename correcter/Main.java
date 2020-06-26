package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        System.out.println(emulateError(input));

    }

    public static String emulateError(String input) {

        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789";
        int alphabetLength = alphabet.length();
        Random random = new Random();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); i += 3) {
            String subString = i + 3 > input.length() ? input.substring(i) : input.substring(i, i + 3);
            int pos = random.nextInt(3);
            char error = alphabet.charAt(random.nextInt(alphabetLength));
            subString = subString.replace(subString.charAt(pos), error);

            output.append(subString);
        }

        return output.toString();
    }
}
