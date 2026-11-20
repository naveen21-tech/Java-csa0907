import java.awt.*;
import java.awt.event.*;

/** The view gathers input and displays results; FeeService owns business validation. */
public class FeeWindow extends Frame {
    private final TextField regNo = new TextField(18);
    private final TextField amount = new TextField(18);
    private final Label status = new Label("Enter payment details.");
    private final FeeService service = new FeeService();

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new BorderLayout(8, 8));
        Panel fields = new Panel(new GridLayout(2, 2, 6, 6));
        fields.add(new Label("Registration number")); fields.add(regNo);
        fields.add(new Label("Amount (Rs.)")); fields.add(amount);
        add(fields, BorderLayout.CENTER);
        Panel actions = new Panel(new FlowLayout());
        Button pay = new Button("Pay"), clear = new Button("Clear"), receipt = new Button("Receipt");
        actions.add(pay); actions.add(clear); actions.add(receipt);
        add(actions, BorderLayout.NORTH); add(status, BorderLayout.SOUTH);

        pay.addActionListener(e -> pay());
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); status.setText("Fields cleared."); });
        receipt.addActionListener(e -> showReceipt());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(420, 190);
    }

    private void pay() {
        try {
            FeeService.Payment p = service.validate(regNo.getText(), amount.getText());
            status.setText(service.pay(p));
        } catch (IllegalArgumentException ex) {
            status.setText(ex.getMessage());
        }
    }

    private void showReceipt() {
        try {
            FeeService.Payment p = service.validate(regNo.getText(), amount.getText());
            new Dialog(this, "Receipt", true) {{
                setLayout(new BorderLayout());
                add(new Label("Receipt preview: " + p.registrationNumber() + " | Rs. "
                        + String.format("%.2f", p.amount())), BorderLayout.CENTER);
                Button close = new Button("Close"); close.addActionListener(e -> dispose());
                add(close, BorderLayout.SOUTH); setSize(340, 120); setLocationRelativeTo(FeeWindow.this);
            }}.setVisible(true);
        } catch (IllegalArgumentException ex) { status.setText(ex.getMessage()); }
    }

    public static void main(String[] args) { EventQueue.invokeLater(() -> new FeeWindow().setVisible(true)); }
}
