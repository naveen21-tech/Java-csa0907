import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ATM {
    private static final double BALANCE = 5000.00;

    static void withdraw(String account, double amount) throws IOException {
        if (!Files.exists(Path.of("card-reader.txt"))) {
            throw new IOException("Card-reader file is missing");
        }
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        if (amount > BALANCE) throw new IllegalStateException("Insufficient balance");
        System.out.printf("Account %s: Please collect Rs. %.2f%n", account, amount);
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Account number: ");
            String account = in.nextLine().trim();
            System.out.print("Withdrawal amount: ");
            double amount = Double.parseDouble(in.nextLine().trim());
            withdraw(account, amount);
        } catch (NumberFormatException | InputMismatchException e) {
            System.out.println("Invalid numeric input: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid amount: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Withdrawal rejected: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("ATM device error: " + e.getMessage());
        } finally {
            System.out.println("ATM session closed.");
        }
    }
}