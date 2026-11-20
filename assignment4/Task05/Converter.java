import java.awt.FlowLayout;
import javax.swing.*;

/** Completed TODO lambda version: one ActionListener instance handles button and Enter. */
public class Converter extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");

    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }

    public Converter() {
        super("Currency Converter");
        setLayout(new FlowLayout());
        JButton convertButton = new JButton("Convert");
        add(new JLabel("INR")); add(amount); add(target);
        add(convertButton); add(result);
        // TODO 1: one lambda listener handles both event sources.
        java.awt.event.ActionListener convertAction = e -> convert();
        convertButton.addActionListener(convertAction);
        amount.addActionListener(convertAction);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void convert() {
        // TODO 2: parse and display 2 decimal places; report invalid amount.
        try {
            double inr = Double.parseDouble(amount.getText().trim());
            if (!Double.isFinite(inr) || inr < 0) throw new NumberFormatException();
            String currency = (String) target.getSelectedItem();
            double converted = inr * rate(currency);
            String message = String.format("%.2f %s", converted, currency);
            result.setText(message);
            JOptionPane.showMessageDialog(this, message, "Conversion result", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Invalid amount", "Input error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Converter().setVisible(true));
    }
}
