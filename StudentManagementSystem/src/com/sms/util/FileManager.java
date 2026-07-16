package com.sms.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic helper for reading/writing plain-text CSV data files.
 * Every "table" in this project is just a .csv file in the data/ folder.
 */
public class FileManager {

    public static final String DATA_DIR = "data";

    /** Make sure the data directory and the given file both exist. */
    public static void ensureFile(String fileName) {
        try {
            Path dir = Paths.get(DATA_DIR);
            if (!Files.exists(dir)) Files.createDirectories(dir);

            Path file = dir.resolve(fileName);
            if (!Files.exists(file)) Files.createFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize data file: " + fileName, e);
        }
    }

    /** Read all non-empty lines from a data file. */
    public static List<String> readLines(String fileName) {
        ensureFile(fileName);
        List<String> result = new ArrayList<>();
        try {
            List<String> raw = Files.readAllLines(Paths.get(DATA_DIR, fileName), StandardCharsets.UTF_8);
            for (String line : raw) {
                if (!line.trim().isEmpty()) result.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read data file: " + fileName, e);
        }
        return result;
    }

    /** Overwrite the entire file with the given lines. */
    public static void writeLines(String fileName, List<String> lines) {
        ensureFile(fileName);
        try {
            Files.write(Paths.get(DATA_DIR, fileName), lines, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Could not write data file: " + fileName, e);
        }
    }

    /** Append a single line to the file. */
    public static void appendLine(String fileName, String line) {
        ensureFile(fileName);
        try {
            Files.write(Paths.get(DATA_DIR, fileName), (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Could not append to data file: " + fileName, e);
        }
    }

    /** Strip commas/newlines from user input so it doesn't break the CSV format. */
    public static String sanitize(String value) {
        if (value == null) return "";
        return value.replace(",", " ").replace("\n", " ").trim();
    }
}
