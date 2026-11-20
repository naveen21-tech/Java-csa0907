import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegistrationService {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final RegistrationDao dao = new RegistrationDao();

    public void register(String studentId, String name, String email, int eventId) throws SQLException {
        String id = studentId == null ? "" : studentId.trim();
        String n = name == null ? "" : name.trim();
        String em = email == null ? "" : email.trim();
        if (id.isEmpty() || n.isEmpty() || em.isEmpty())
            throw new IllegalArgumentException("Student ID, name and email are required.");
        if (!EMAIL.matcher(em).matches()) throw new IllegalArgumentException("Enter a valid email address.");
        if (eventId <= 0) throw new IllegalArgumentException("Choose an event.");
        dao.register(id, n, em, eventId);
    }
}
