# Task 6 — Campus Event Registration Desktop Application (13 marks)

## Stage 1 — Users, screens, data and testable rules
**Users:** students (submit registrations) and cultural committee staff (view, filter and export registrations).

**Screens:** registration form, registrations table/filter view, confirmation/error dialogs, CSV save chooser.

**Data:** event ID/name/date/venue; registration ID, student ID/name/email, event ID and registration timestamp.

**Testable requirements**
1. A registration is accepted only when student ID, name and syntactically valid email are supplied and an event is selected.
2. The system rejects a second registration for the same student and event.
3. Staff can view registrations in a table and filter by event.
4. The table loads a page of at most 50 records.
5. CSV export includes all records matching the current filter.
6. Database calls execute off the EDT; the window remains repaintable while work is running.
7. Database errors and invalid input are shown clearly.

## Stage 2 — Screen and event flow
```text
JFrame EventRegistrationApp
├── JMenuBar
│   └── File → Export CSV / Exit
├── Registration JPanel
│   ├── Student ID JTextField
│   ├── Name JTextField
│   ├── Email JTextField
│   ├── Event JComboBox
│   └── Register JButton → confirmation dialog → service
├── Filter controls
│   ├── Event JComboBox
│   ├── Apply / Previous / Next buttons
└── JTable inside JScrollPane
    └── status JLabel
```

## Stage 3 — Critical decisions
- UI-level required-field checks may improve immediate feedback, but authoritative validation belongs in RegistrationService.
- Duplicate checking is enforced by the database UNIQUE(student_id,event_id) constraint, preventing race conditions that a UI-only pre-check cannot prevent.
- JDBC statements use PreparedStatement and try-with-resources.
- Database access runs in SwingWorker.doInBackground(); done() updates Swing components on the EDT.
- Pagination uses SQL LIMIT/OFFSET. The supplied page size is 50.
- Export streams pages to a CSV file rather than collecting the entire result set in memory.

## Stage 4 — Classes and handlers
```text
EventRegistrationApp (Swing presentation)
          |
          v
RegistrationService (validation and business rules)
          |
          v
RegistrationDao (JDBC queries / persistence)
          |
          v
MySQL campus_events database
```

**Register pseudocode**
```text
PROCEDURE registerAction
    read studentId, name, email, eventId
    IF required fields missing OR email invalid OR event not selected THEN
        display validation error; RETURN
    ENDIF
    ask user to confirm
    IF not confirmed THEN RETURN
    run SwingWorker:
        service.register(studentId, name, email, eventId)
    on completion:
        display success and refresh current page
    on failure:
        display useful error; map unique-key violation to duplicate message
ENDPROCEDURE
```

**Filter pseudocode**
```text
PROCEDURE filterAction
    selectedEvent ← selected filter event (or ALL)
    page ← 0
    run SwingWorker:
        rows ← DAO.findPage(page × PAGE_SIZE, PAGE_SIZE, selectedEvent)
    on completion:
        clear table model
        append each returned row
        update status
ENDPROCEDURE
```

## Stage 5 — Implementation and demonstrations
The folder includes the Swing application, service, DAO, model, connection helper and schema. Demonstrate:
- Normal registration: submit a valid student and event; success message and row appears.
- Invalid registration: blank required field or malformed email; clear validation message.
- Duplicate registration: submit the same student ID/event pair twice; the unique constraint rejects the second attempt.
- Filter and export: choose an event and export CSV.

Configure MySQL Connector/J and environment variables as described in the repository README before running. The code has not been executed against the user's database; attach genuine screenshots after local testing.

## Alternative: load all 100,000 rows vs pagination
Loading all 100,000 registrations transfers and materializes the complete result set. Time and memory grow as (O(N)); the table model and row objects can consume substantial heap, increasing garbage collection and potentially freezing the UI if done on the EDT. Pagination retrieves (P) rows at a time: per-page transfer and client memory are (O(P)), with total traversal still (O(N)). SwingWorker keeps either approach off the EDT, but pagination limits memory and gives a faster first display. LIMIT/OFFSET may become slower at very large offsets; keyset pagination is a possible production refinement.
