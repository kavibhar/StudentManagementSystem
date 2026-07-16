package com.sms.util;

import java.util.Scanner;

/** Small helpers for robust console input. */
public class ConsoleUtil {

    public static String readNonEmpty(Scanner sc, String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine().trim();
            if (!value.isEmpty()) return FileManager.sanitize(value);
            System.out.println("  Value cannot be empty. Try again.");
        }
    }

    public static String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        return FileManager.sanitize(sc.nextLine());
    }

    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid whole number.");
            }
        }
    }

    public static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double v = Double.parseDouble(line);
                if (v < 0 || v > 100) {
                    System.out.println("  Marks must be between 0 and 100.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number.");
            }
        }
    }

    public static void pause(Scanner sc) {
        System.out.print("\nPress ENTER to continue...");
        sc.nextLine();
    }
}
