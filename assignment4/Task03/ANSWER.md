# Task 3 — Campus Library Search Screen (9 marks)

## 3.1 Layout choices
- Main frame: `BorderLayout`; the center component receives remaining space.
- Header/search container: nested `BorderLayout`; title at NORTH and search row at SOUTH.
- Search row: `FlowLayout(LEFT)`; controls retain preferred sizes and flow horizontally.
- Results: `JScrollPane` around JTable in CENTER; expands in both dimensions.
- Bottom container: `BorderLayout`; navigation buttons in CENTER and status at SOUTH.
- Navigation row: `FlowLayout(CENTER)`; buttons retain preferred sizes.
- GridLayout is not needed because the controls are not required to occupy equal-sized cells. CardLayout is for switching between stacked screens, not this single screen.

## 3.2 Nested structure
```text
JFrame [BorderLayout]
├── NORTH: JPanel [BorderLayout]
│   ├── NORTH: title JLabel
│   └── SOUTH: search JPanel [FlowLayout]
├── CENTER: JScrollPane → JTable
└── SOUTH: JPanel [BorderLayout]
    ├── CENTER: button JPanel [FlowLayout]
    └── SOUTH: status JLabel
```

## 3.3 Implementation
Run `LibrarySearch.java`. It demonstrates the layout and a sample result row; live catalogue retrieval is intentionally represented by a clear demo status because no library backend was specified.

## 3.4 Resize test
Run the program and capture the same screen at (a) 520×320, (b) 760×480 and (c) 1100×700. The scroll pane/results area should grow as the frame grows; title, search controls and bottom buttons keep their preferred sizes. Attach actual screenshots from your execution to the submission.

## Alternative: GridBagLayout or absolute positioning
GridBagLayout can express the same proportions with constraints and fewer nested panels, but requires more constraint configuration. Its layout pass is approximately linear in the number of components for a typical small form. Absolute positioning has low setup overhead but does not adapt to resizing, font changes or platform differences; components may overlap or become clipped. Nested panels add (O(p)) container space for (p) panels but make responsibilities and resizing behaviour clear. Layout time is negligible for this small screen; robustness is the deciding factor.
