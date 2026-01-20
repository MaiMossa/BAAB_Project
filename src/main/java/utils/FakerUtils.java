package utils;

import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FakerUtils {
    private FakerUtils() {
        super();
    }

    public static String generateName() {
        return new Faker().name().firstName().replace("'", "''");
    }

    public static String generateCompany() {
        return new Faker().company().name().replace("'", "''");
    }

    public static String generateAddress() {
        return new Faker().address().fullAddress().replace("'", "''");
    }

    public static String generateCity() {
        return new Faker().address().city().replace("'", "''");
    }

    public static String generateZipCode() {
        return new Faker().address().zipCode();
    }

    public static String generatePhoneNumber() {
        return new Faker().phoneNumber().phoneNumber();
    }

    public static String generateEmail() {
        return new Faker().internet().emailAddress();
    }

    public static String generatePassword() {
        return new Faker().internet().password();
    }

    public static String generateText() {
        return new Faker().lorem().sentence();
    }

    public static String generateText(int length) {
        return new Faker().lorem().characters(length);
    }

    public static String generateText(int minLength, int maxLength) {
        return new Faker().lorem().characters(minLength, maxLength);
    }
    public static String generateStrongPassword() {
        int length = 12;
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String special = "!@#$%^&*()-_=+<>?";

        String allChars = upper + lower + numbers + special;
        StringBuilder password = new StringBuilder();

        password.append(upper.charAt((int)(Math.random() * upper.length())));
        password.append(lower.charAt((int)(Math.random() * lower.length())));
        password.append(numbers.charAt((int)(Math.random() * numbers.length())));
        password.append(special.charAt((int)(Math.random() * special.length())));

        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt((int)(Math.random() * allChars.length())));
        }

        // Random shuffle
        List<Character> pwdChars = password.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);

        StringBuilder finalPassword = new StringBuilder();
        for (char c : pwdChars) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }



}
