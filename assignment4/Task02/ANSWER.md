# Task 2 — Hospital Outpatient Appointment Desk (8 marks)

## 2.1 Event model

| Event source | Event object | Listener | Action |
|---|---|---|---|
| Department JComboBox | ItemEvent | ItemListener | Load/choose doctors for selected department and refresh doctor combo |
| Doctor JComboBox | ItemEvent | ItemListener | Update available slots for selected doctor |
| Slot JComboBox | ItemEvent | ItemListener | Store selected slot |
| Book JButton | ActionEvent | ActionListener | Validate patient, department, doctor and slot; confirm and book |
| Patient ID JTextField | ActionEvent | ActionListener | Optionally trigger validation when Enter is pressed |
| Window | WindowEvent | WindowListener / WindowAdapter | Ask before closing while booking is in progress |

## 2.2 Component containment

```text
JFrame AppointmentDesk
└── JPanel (BorderLayout)
    ├── JLabel title (NORTH)
    ├── JPanel form (GridLayout(4,2)) (CENTER)
    │   ├── JLabel + JTextField patientId
    │   ├── JLabel + JComboBox department
    │   ├── JLabel + JComboBox doctor
    │   └── JLabel + JComboBox timeSlot
    └── JPanel actions (FlowLayout) (SOUTH)
        ├── JButton Book
        └── JLabel status
```

## 2.3 Handler pseudocode

### (a) Refresh doctor list
```text
PROCEDURE refreshDoctors
    selectedDepartment ← departmentCombo.getSelectedItem()
    doctorCombo.removeAllItems()
    slotCombo.removeAllItems()
    IF selectedDepartment IS NULL THEN
        RETURN
    ENDIF
    doctors ← doctorMap.get(selectedDepartment)
    FOR EACH doctor IN doctors
        doctorCombo.addItem(doctor)
    ENDFOR
    IF doctorCombo has an item THEN
        refreshSlots()
    ENDIF
ENDPROCEDURE
```

### (b) Validate and book
```text
PROCEDURE bookAppointment
    patient ← trim(patientIdField.getText())
    department ← departmentCombo.getSelectedItem()
    doctor ← doctorCombo.getSelectedItem()
    slot ← slotCombo.getSelectedItem()
    IF patient IS EMPTY OR department IS NULL OR doctor IS NULL OR slot IS NULL THEN
        show validation error
        RETURN
    ENDIF
    IF user does not confirm booking THEN RETURN
    bookingInProgress ← TRUE
    TRY
        bookingService.book(patient, doctor, slot)
        show confirmation
    CATCH booking error
        show error
    FINALLY
        bookingInProgress ← FALSE
    ENDTRY
ENDPROCEDURE
```

## 2.4 Event trace and applet comparison
1. `main` schedules UI creation on the EDT with `SwingUtilities.invokeLater`.
2. Constructor creates components, layouts and listeners.
3. `setVisible(true)` displays the frame.
4. User changes department → `itemStateChanged` → `refreshDoctors` → doctor model updates.
5. User selects slot and clicks Book → `actionPerformed` → validation → confirmation → service call → status update.
6. User closes window → `windowClosing` → confirmation if booking is in progress → `dispose()` if permitted.

Applet lifecycle methods are container-controlled: `init()` initializes once, `start()` begins/resumes, `paint()` renders, `stop()` pauses, and `destroy()` releases resources. A standalone JFrame instead uses its constructor, visibility, event callbacks and `dispose()`; it has no applet lifecycle.

## Alternative concept and complexity
If doctor lists are fetched on every department change, each change incurs database/network latency (L) plus result processing (O(m_d)), where (m_d) is the number of doctors for that department. Loading all data once into `Map<String,List<Doctor>>` makes selection approximately (O(m_d)) to populate the combo, with no network round trip; memory is (O(d+m)), where (d) is departments and (m) is total doctors. Database-on-demand uses lower client memory but higher repeated latency and needs asynchronous loading. Cached maps trade memory for responsiveness.
