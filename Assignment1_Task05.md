# Task 5

Static ticket counter is shared by all tickets and generates monotonically increasing IDs. After exit, active-map reference is removed and slot freed; unreferenced ticket objects become eligible for garbage collection. Static counter uses O(1) space.
