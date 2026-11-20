# Task 5 — Same Screen, Different Event-Handling Styles (7 marks)

## 5.1 Four implementations
Run `EventHandlingStyles.java` and choose one of the four versions:
1. Frame implements ActionListener.
2. Named inner class implements ActionListener.
3. Anonymous inner class implements ActionListener.
4. Lambda expression implements ActionListener.

All four use a WindowAdapter for close handling. The completed `Converter.java` is the requested lambda version and registers one lambda listener for both the Convert button and Enter in the amount field. It displays a two-decimal result in a dialog and handles invalid input.

## 5.2 Requirement change
- Frame listener: add an ActionEvent source check or register the frame on the text field too; display JOptionPane in the handler.
- Named inner class: register the same handler on both sources and put dialog output in its method. This requires little change and preserves reuse.
- Anonymous class: register the same listener instance on both sources; avoid creating two duplicated anonymous handlers.
- Lambda: assign the lambda to one ActionListener variable and register it on both components; show the dialog in the shared action method.

## 5.3 Comparison

| Style | Readability | Access to frame fields | Reuse | Ease of change |
|---|---|---|---|---|
| Frame implements listener | Simple for tiny frame; mixes roles | Direct | Low; frame is the handler | Can make frame crowded |
| Named inner class | Clear named responsibility | Direct access to enclosing fields | Good | Good for complex handler |
| Anonymous inner class | Local but verbose | Direct | Limited unless stored in variable | Moderate |
| Lambda | Concise for one-method interface | Direct lexical access | Good when stored as a variable | Excellent for small actions |

For a large application, use a separate controller/service or named handler/Action rather than placing all event logic in the frame. It gives a meaningful name, supports reuse and unit testing, and prevents the UI class from becoming a monolith.

## Alternative: method reference/shared Action
`this::convert` is concise when the target method matches the listener signature (or use an adapter method that accepts the event). A shared Swing Action can be attached to a button, menu item and keyboard shortcut, centralizing enabled state, name and action behaviour. One shared Action/listener can serve multiple triggers, so listener storage is (O(1)) for that action; adding a trigger is another registration rather than duplicating logic. The four separate styles can create one handler per control unless the same listener object is reused. The conversion itself is constant time (O(1)); the key difference is maintainability and object reuse rather than computation time.
