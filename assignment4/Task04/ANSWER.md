# Task 4 — Event-Handling Forensics (7 marks)

## 4.1 Defect
The method is written `mouseclicked`, but the MouseListener/MouseAdapter callback name is exactly `mouseClicked` (capital C). Java is case-sensitive. Since it does not override the callback, the adapter's empty implementation runs and the count never changes. The label also is never updated in the original code.

## 4.2 Failing interaction
After correcting the method name, click several places and then minimise and restore the window. The dots drawn with `getGraphics()` disappear because drawing directly to a component's current graphics surface is temporary. A later repaint reconstructs the component from its paint method, and the program has not retained the points.

## 4.3 Repair and completed TODOs
`DrawPad.java` completes TODO 1 by overriding `mouseClicked`, storing `e.getPoint()`, updating the label and calling `repaint()`. TODO 2 loops through stored points in `paint(Graphics)` and draws a 6×6 circle centred at each point. It also handles window close.

## 4.4 Prevention
Adding `@Override` above the misspelled `mouseclicked` method causes a compile-time error because no superclass method is overridden. This exposes the spelling/capitalisation defect before execution.

## Alternative: off-screen BufferedImage
An alternative creates a BufferedImage once, draws each new dot into it when clicked, and copies the image in `paint()`. Repainting then copies a fixed width×height pixel buffer, approximately (O(W H)) pixel work (often accelerated), rather than redrawing (n) point objects in (O(n)). Point storage requires (O(n)) memory; an ARGB image requires (O(W H)) pixels, about (4WH) bytes, plus image overhead. For sparse dots, storing points is memory-efficient; for many complex strokes, an image can make repainting predictable.
