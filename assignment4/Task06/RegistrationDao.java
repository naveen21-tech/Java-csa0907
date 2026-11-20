import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDao {
    public void register(String studentId, String studentName, String email, int eventId) throws SQLException {
        String sql = "INSERT INTO registrations(student_id, student_name, email, event_id) VALUES(?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, studentId); ps.setString(2, studentName); ps.setString(3, email); ps.setInt(4, eventId);
            ps.executeUpdate();
        }
    }

    public List<Registration> findPage(int offset, int limit, Integer eventId) throws SQLException {
        String sql = "SELECT r.registration_id,r.student_id,r.student_name,r.email,r.event_id,e.event_name,r.registered_at "
                + "FROM registrations r JOIN events e ON e.event_id=r.event_id "
                + (eventId == null ? "" : "WHERE r.event_id=? ")
                + "ORDER BY r.registered_at DESC LIMIT ? OFFSET ?";
        List<Registration> rows = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            int i = 1;
            if (eventId != null) ps.setInt(i++, eventId);
            ps.setInt(i++, limit); ps.setInt(i, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("registered_at");
                    rows.add(new Registration(rs.getLong("registration_id"), rs.getString("student_id"),
                            rs.getString("student_name"), rs.getString("email"), rs.getInt("event_id"),
                            rs.getString("event_name"), ts == null ? null : ts.toLocalDateTime()));
                }
            }
        }
        return rows;
    }

    public List<String[]> findEvents() throws SQLException {
        List<String[]> events = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT event_id,event_name FROM events ORDER BY event_name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) events.add(new String[]{String.valueOf(rs.getInt(1)), rs.getString(2)});
        }
        return events;
    }

    public void exportCsv(java.io.File file, Integer eventId) throws Exception {
        try (java.io.PrintWriter out = new java.io.PrintWriter(file, java.nio.charset.StandardCharsets.UTF_8)) {
            out.println("Registration ID,Student ID,Student Name,Email,Event,Registered At");
            int offset = 0, pageSize = 500;
            while (true) {
                List<Registration> page = findPage(offset, pageSize, eventId);
                for (Registration r : page) {
                    out.println(csv(r.id()) + "," + csv(r.studentId()) + "," + csv(r.studentName()) + ","
                            + csv(r.email()) + "," + csv(r.eventName()) + "," + csv(String.valueOf(r.registeredAt())));
                }
                if (page.size() < pageSize) break;
                offset += pageSize;
            }
        }
    }

    private static String csv(String s) { return "\"" + s.replace("\"", "\"\"") + "\""; }
}
