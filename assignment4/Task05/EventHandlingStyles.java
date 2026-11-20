import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

/** Four small independent examples of the same conversion action. */
public class EventHandlingStyles {
    private static final double RATE = 0.012;
    private static String calculate(JTextField amount) {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            if (!Double.isFinite(value) || value < 0) throw new NumberFormatException();
            return String.format("%.2f USD", value * RATE);
        } catch (NumberFormatException ex) { return "Invalid amount"; }
    }

    // (a) The frame itself implements ActionListener.
    static class FrameListenerVersion extends JFrame implements ActionListener {
        final JTextField amount = new JTextField(8);
        final JLabel result = new JLabel(" ");
        FrameListenerVersion() {
            super("Frame implements ActionListener"); setLayout(new FlowLayout());
            JButton button = new JButton("Convert"); add(amount); add(button); add(result);
            button.addActionListener(this);
            addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) { dispose(); } });
            setDefaultCloseOperation(DISPOSE_ON_CLOSE); pack();
        }
        @Override public void actionPerformed(ActionEvent e) { result.setText(calculate(amount)); }
    }

    // (b) Named inner class.
    static class NamedInnerVersion extends JFrame {
        final JTextField amount = new JTextField(8);
        final JLabel result = new JLabel(" ");
        NamedInnerVersion() {
            super("Named inner class"); setLayout(new FlowLayout());
            JButton button = new JButton("Convert"); add(amount); add(button); add(result);
            button.addActionListener(new ConvertHandler());
            addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) { dispose(); } });
            setDefaultCloseOperation(DISPOSE_ON_CLOSE); pack();
        }
        private class ConvertHandler implements ActionListener {
            @Override public void actionPerformed(ActionEvent e) { result.setText(calculate(amount)); }
        }
    }

    // (c) Anonymous inner class.
    static class AnonymousVersion extends JFrame {
        final JTextField amount = new JTextField(8);
        final JLabel result = new JLabel(" ");
        AnonymousVersion() {
            super("Anonymous inner class"); setLayout(new FlowLayout());
            JButton button = new JButton("Convert"); add(amount); add(button); add(result);
            button.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) { result.setText(calculate(amount)); }
            });
            addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) { dispose(); } });
            setDefaultCloseOperation(DISPOSE_ON_CLOSE); pack();
        }
    }

    // (d) Lambda expression.
    static class LambdaVersion extends JFrame {
        final JTextField amount = new JTextField(8);
        final JLabel result = new JLabel(" ");
        LambdaVersion() {
            super("Lambda expression"); setLayout(new FlowLayout());
            JButton button = new JButton("Convert"); add(amount); add(button); add(result);
            ActionListener action = e -> result.setText(calculate(amount));
            button.addActionListener(action); amount.addActionListener(action);
            addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) { dispose(); } });
            setDefaultCloseOperation(DISPOSE_ON_CLOSE); pack();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Frame listener", "Named inner", "Anonymous", "Lambda"};
            int choice = JOptionPane.showOptionDialog(null, "Choose implementation", "Event styles",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            JFrame frame = switch (choice) {
                case 0 -> new FrameListenerVersion();
                case 1 -> new NamedInnerVersion();
                case 2 -> new AnonymousVersion();
                default -> new LambdaVersion();
            };
            if (frame != null) frame.setVisible(true);
        });
    }
}
