# Programming in Java — Assignment 4 (CO4)

**Student:** Naveen  
**Course:** Programming in Java (CSA09)  
**Course outcome:** CO4 — Applets, event delegation, listeners and adapters, inner/anonymous classes, AWT/Swing, layouts, menus/dialogs and GUI–JDBC integration.

## Requirements
- JDK 17 or later (the examples use standard AWT/Swing APIs).
- Tasks 1–5 compile without external libraries.
- Task 6 requires MySQL Server and MySQL Connector/J.

## Folder guide

| Folder | Contents |
|---|---|
| Task01 | Fee-window design review, separated FeeService and event handlers |
| Task02 | Hospital appointment event table, component hierarchy, pseudocode and event trace |
| Task03 | Resizable Swing library-search screen using nested layouts |
| Task04 | Corrected persistent drawing pad and completed TODO program |
| Task05 | Four event-handling styles and completed lambda converter |
| Task06 | Layered event-registration Swing application, JDBC DAO and SQL schema |

## Compile and run
From the repository root, compile a task's Java file(s) into a separate output folder. Example:

```bash
javac -d out assignment4/Task03/LibrarySearch.java
java -cp out LibrarySearch
```

For a task with multiple Java files, compile all files in that task folder, for example:
```bash
javac -d out assignment4/Task01/*.java
java -cp out FeeWindow
```

Task 6:
1. Create the database and tables with `mysql -u root -p < assignment4/Task06/schema.sql`.
2. Set environment variables `DB_URL`, `DB_USER`, and `DB_PASSWORD` (or edit `Database.java`).
3. Add MySQL Connector/J to the classpath.
4. Compile and run:
```bash
javac -cp "mysql-connector-j.jar" -d out assignment4/Task06/*.java
java -cp "out:mysql-connector-j.jar" EventRegistrationApp
```
On Windows, use `; ` rather than `:` in the classpath.

## Testing evidence
The assignment requires screenshots of executed interactions (including three window sizes for Task 3, before/after minimise for Task 4, valid/invalid converter input for Task 5, and normal/invalid/duplicate registration for Task 6). These should be captured on the student's machine after running the programs. No fabricated screenshots are included.

## Task results
- Task 1: Identifies design defects and separates presentation from validation/payment service.
- Task 2: Models appointment events, component containment and booking flow.
- Task 3: Uses BorderLayout and FlowLayout so the results region expands on resize.
- Task 4: Stores click points and redraws them from paint(), so repainting preserves dots.
- Task 5: Implements frame listener, named inner class, anonymous listener and lambda approaches.
- Task 6: Separates Swing UI, service and JDBC DAO; uses SwingWorker, uniqueness enforcement and paginated retrieval.

**Note:** The Task 6 database program must be tested with a configured MySQL instance.