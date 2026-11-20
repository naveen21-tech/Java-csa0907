import java.util.Objects;

/** Business rules for fee payments; independent of the AWT window. */
public class FeeService {
    public record Payment(String registrationNumber, double amount) {}

    public Payment validate(String registrationNumber, String amountText) {
        String reg = Objects.requireNonNullElse(registrationNumber, "").trim();
        if (reg.isEmpty()) throw new IllegalArgumentException("Registration number is required.");
        final double amount;
        try { amount = Double.parseDouble(Objects.requireNonNullElse(amountText, "").trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Enter a valid amount."); }
        if (!Double.isFinite(amount) || amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
        return new Payment(reg, amount);
    }

    /** Replace this demonstration method with a gateway call in a deployed system. */
    public String pay(Payment payment) {
        Objects.requireNonNull(payment);
        return "Payment request accepted for " + payment.registrationNumber()
                + " : Rs. " + String.format("%.2f", payment.amount());
    }
}
