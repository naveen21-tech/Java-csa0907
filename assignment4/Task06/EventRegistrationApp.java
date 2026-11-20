import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Swing UI; JDBC work runs in SwingWorker, never on the event-dispatch thread. */
public class EventRegistrationApp extends JFrame {
    private final JTextField studentId = new JTextField(12), studentName = new JTextField(14), email = new JTextField(16);
    private final JComboBox<EventChoice> event = new JComboBox<>(), filter = new JComboBox<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Student ID", "Name", "Email", "Event", "Registered At"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready");
    private final RegistrationDao dao = new RegistrationDao();
    private final RegistrationService service = new RegistrationService();
    private int page = 0;
    private static final int PAGE_SIZE = 50;

    private record EventChoice(int id, String name) {
        @Override public String toString() { return name; }
    }

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout(8, 8));
        setJMenuBar(createMenu());
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Student registration"));
        form.add(new JLabel("Student ID")); form.add(studentId);
        form.add(new JLabel("Student name")); form.add(studentName);
        form.add(new JLabel("Email")); form.add(email);
        form.add(new JLabel("Event")); form.add(event);
        JButton register = new JButton("Register");
        JPanel top = new JPanel(new BorderLayout()); top.add(form, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(register); actions.add(new JLabel("Filter event")); actions.add(filter);
        JButton apply = new JButton("Apply filter"), previous = new JButton("Previous"), next = new JButton("Next");
        actions.add(apply); actions.add(previous); actions.add(next); top.add(actions, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH); add(new JScrollPane(table), BorderLayout.CENTER); add(status, BorderLayout.SOUTH);
        register.addActionListener(e -> registerStudent());
        apply.addActionListener(e -> { page = 0; loadPage(); });
        previous.addActionListener(e -> { if (page > 0) page--; loadPage(); });
        next.addActionListener(e -> { page++; loadPage(); });
        addWindowListener(new WindowAdapter() { @Override public void windowClosing(WindowEvent e) { dispose(); } });
        setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(900, 560); setLocationRelativeTo(null); loadEvents();
    }

    private JMenuBar createMenu() {
        JMenuBar bar = new JMenuBar(); JMenu file = new JMenu("File");
        JMenuItem export = new JMenuItem("Export CSV"), exit = new JMenuItem("Exit");
        export.addActionListener(e -> exportCsv()); exit.addActionListener(e -> dispose());
        file.add(export); file.addSeparator(); file.add(exit); bar.add(file); return bar;
    }

    private Integer selectedFilterId() {
        EventChoice choice = (EventChoice) filter.getSelectedItem();
        return choice == null || choice.id() == 0 ? null : choice.id();
    }
    private int selectedEventId() {
        EventChoice choice = (EventChoice) event.getSelectedItem(); return choice == null ? -1 : choice.id();
    }

    private void loadEvents() {
        status.setText("Loading events...");
        new SwingWorker<List<String[]>, Void>() {
            @Override protected List<String[]> doInBackground() throws Exception { return dao.findEvents(); }
            @Override protected void done() {
                try {
                    event.removeAllItems(); filter.removeAllItems(); filter.addItem(new EventChoice(0, "All events"));
                    for (String[] row : get()) {
                        EventChoice choice = new EventChoice(Integer.parseInt(row[0]), row[1]);
                        event.addItem(choice); filter.addItem(choice);
                    }
                    status.setText("Events loaded."); loadPage();
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void registerStudent() {
        String id = studentId.getText(), name = studentName.getText(), mail = email.getText();
        int eventId = selectedEventId();
        if (JOptionPane.showConfirmDialog(this, "Submit this registration?", "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        setBusy(true, "Saving registration...");
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception { service.register(id, name, mail, eventId); return null; }
            @Override protected void done() {
                setBusy(false, "Ready");
                try {
                    get(); status.setText("Registration successful.");
                    studentId.setText(""); studentName.setText(""); email.setText(""); page = 0; loadPage();
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void loadPage() {
        final Integer eventId = selectedFilterId();
        final int requestedPage = page;
        status.setText("Loading registrations...");
        new SwingWorker<List<Registration>, Void>() {
            @Override protected List<Registration> doInBackground() throws Exception {
                return dao.findPage(requestedPage * PAGE_SIZE, PAGE_SIZE, eventId);
            }
            @Override protected void done() {
                try {
                    model.setRowCount(0);
                    for (Registration r : get()) model.addRow(new Object[]{r.id(), r.studentId(),
                            r.studentName(), r.email(), r.eventName(), r.registeredAt()});
                    status.setText("Page " + (requestedPage + 1) + " — " + model.getRowCount() + " registrations");
                } catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser(); chooser.setSelectedFile(new File("registrations.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        final File destination = chooser.getSelectedFile();
        final Integer eventId = selectedFilterId();
        setBusy(true, "Exporting...");
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception { dao.exportCsv(destination, eventId); return null; }
            @Override protected void done() {
                setBusy(false, "Ready");
                try { get(); status.setText("CSV exported: " + destination.getName()); }
                catch (Exception ex) { showError(ex); }
            }
        }.execute();
    }

    private void setBusy(boolean busy, String message) {
        status.setText(message); setCursor(Cursor.getPredefinedCursor(busy ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR));
    }
    private void showError(Exception ex) {
        Throwable cause = ex.getCause() == null ? ex : ex.getCause();
        String message = cause.getMessage() == null ? cause.toString() : cause.getMessage();
        if (cause instanceof SQLException && message.toLowerCase().contains("duplicate"))
            message = "This student is already registered for the selected event.";
        status.setText(message);
        JOptionPane.showMessageDialog(this, message, "Operation failed", JOptionPane.ERROR_MESSAGE);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventRegistrationApp().setVisible(true));
    }
}
