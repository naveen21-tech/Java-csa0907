# Task 1 — GUI Design Review (6 marks)

## 1.1 Diagnosis
1. **View and business logic are mixed:** the listener parses amounts and writes to the database, making the window difficult to test independently.
2. **String comparison uses `==`:** Java compares object references, not string contents; use `"Pay".equals(e.getActionCommand())`.
3. **No input validation:** empty registration numbers, non-numeric values, zero, negative values and NaN can fail or create invalid records.
4. **Database I/O runs on the event-dispatch thread (EDT):** the window freezes during connection and SQL execution.
5. **`Thread.sleep(3000)` blocks the EDT:** repainting and user interaction pause for three seconds.
6. **No meaningful feedback:** printing “Paid” to the console does not tell the user whether payment succeeded or failed.
7. **Receipt action is not implemented:** the else branch clears fields for both Clear and Receipt.
8. **SQL is built by concatenation:** this permits SQL injection and quoting errors; use PreparedStatement.
9. **Resources are not closed:** connections/statements can leak; use try-with-resources.
10. **Window close handling is absent:** the application may not dispose resources cleanly.
11. **Checked exceptions are not handled:** JDBC and interruption exceptions must be handled or propagated.

## 1.2 Repair
The supplied `FeeWindow.java` handles only input/output and delegates validation and payment behaviour to `FeeService.java`. Each button has a dedicated listener. The example uses a demonstration payment response; a production version should call a payment gateway and perform JDBC work in a SwingWorker/background executor. A database implementation should use PreparedStatement and try-with-resources.

### Class structure
- `FeeWindow`: AWT controls, event registration, status and receipt display.
- `FeeService`: validation and payment business operation.
- A production `FeeRepository` would own JDBC persistence, separate from both classes.

## 1.3 Design defence (under 100 words)
The window can be tested without a database because validation and payment rules live in a separate service. The service can be unit-tested with valid, invalid and boundary inputs. UI changes do not require rewriting business rules, and payment implementation changes do not require changing button handlers. Dedicated listeners make each action easier to understand. This separation reduces coupling and improves maintenance.

## Alternative listener design and complexity
With a shared listener and an if–else chain, identifying an action may require up to (k) comparisons, so dispatch selection is (O(k)) in the number of buttons. A separate listener object per button performs direct dispatch in (O(1)) selection time, but stores (k) listener objects, or (O(k)) additional space. In practice event processing and UI work may dominate this small dispatch difference. A shared Action can also centralize reusable behaviour.
