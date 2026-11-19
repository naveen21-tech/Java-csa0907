import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SalesReport {
    // Safe version: every opened resource is closed, even if reading/writing fails.
    static void generateSafe(List<Path> inputs, Path output) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write("Daily sales summary"); writer.newLine();
            for (Path input : inputs) {
                try (BufferedReader reader = Files.newBufferedReader(input)) {
                    long total = 0; String line;
                    while ((line = reader.readLine()) != null) total += Long.parseLong(line.trim());
                    writer.write(input.getFileName() + ": " + total); writer.newLine();
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid sales data in " + input, e);
                }
            }
        }
    }

    // Deliberately demonstrates the fragile manual-close pattern for comparison.
    static void generateManual(List<Path> inputs, Path output) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(output);
        try {
            writer.write("Daily sales summary"); writer.newLine();
            for (Path input : inputs) {
                BufferedReader reader = Files.newBufferedReader(input);
                try {
                    long total = 0; String line;
                    while ((line = reader.readLine()) != null) total += Long.parseLong(line.trim());
                    writer.write(input.getFileName() + ": " + total); writer.newLine();
                } finally { reader.close(); }
            }
        } finally { writer.close(); }
    }

    public static void main(String[] args) throws IOException {
        Path one = Path.of("store1.txt"), two = Path.of("store2.txt");
        Files.writeString(one, "100\n250\n");
        Files.writeString(two, "75\n125\n");
        List<Path> inputs = List.of(one, two);
        generateManual(inputs, Path.of("manual-report.txt"));
        generateSafe(inputs, Path.of("safe-report.txt"));
        System.out.println(Files.readString(Path.of("safe-report.txt")));
        System.out.println("Manual and try-with-resources reports generated.");
    }
}