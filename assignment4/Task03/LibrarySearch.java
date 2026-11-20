import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Resizable library search screen: the center results area receives extra space. */
public class LibrarySearch extends JFrame {
    private final JTextField query = new JTextField(22);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All", "Book", "Journal", "Thesis"});
    private final JLabel status = new JLabel("Ready");
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Author", "Category"}, 0);

    public LibrarySearch() {
        super("Campus Library Search");
        setLayout(new BorderLayout(8, 8));
        JLabel title = new JLabel("Campus Library", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(20f));
        add(title, BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchRow.add(new JLabel("Search")); searchRow.add(query);
        searchRow.add(category);
        JButton search = new JButton("Search"); searchRow.add(search);
        JPanel north = new JPanel(new BorderLayout()); north.add(title, BorderLayout.NORTH);
        north.add(searchRow, BorderLayout.SOUTH); add(north, BorderLayout.NORTH);

        JTable results = new JTable(model);
        results.setFillsViewportHeight(true);
        add(new JScrollPane(results), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("Prev"), next = new JButton("Next"), reserve = new JButton("Reserve");
        buttons.add(prev); buttons.add(next); buttons.add(reserve);
        bottom.add(buttons, BorderLayout.CENTER); bottom.add(status, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        search.addActionListener(e -> {
            model.setRowCount(0);
            String term = query.getText().trim();
            if (term.isEmpty()) { status.setText("Enter a search term."); return; }
            model.addRow(new Object[]{term + " — sample result", "Library catalogue", category.getSelectedItem()});
            status.setText("Demo result shown. Connect a catalogue service for live search.");
        });
        prev.addActionListener(e -> status.setText("Previous page"));
        next.addActionListener(e -> status.setText("Next page"));
        reserve.addActionListener(e -> status.setText(results.getSelectedRow() < 0
                ? "Select a result first." : "Reservation request selected."));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(520, 320));
        setSize(760, 480);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new LibrarySearch().setVisible(true)); }
}
