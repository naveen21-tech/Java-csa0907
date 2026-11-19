# Java Assignment 3 – CO3

This folder contains Java implementations for all seven questions in Programming in Java Assignment 3 (CO3).

## Student details
- Name: **Add your full name**
- Register number: **Add your register number**
- Pair member: **Add partner's name**
- Java version: Java 17 or later (records are used in Task 7)
- Repository: https://github.com/naveen21-tech/Java-csa0907/tree/main/assignment3

## Folder structure
| Folder | Topic | Main class |
|---|---|---|
| Task01 | ATM exception handling | ATM |
| Task02 | User-defined exceptions and chaining | StorageDemo |
| Task03 | Resource safety and try-with-resources | SalesReport |
| Task04 | Thread creation and life cycle | ScannerStations |
| Task05 | Race conditions and synchronization | SeatBooking |
| Task06 | Deadlock and producer-consumer | TokenCounter |
| Task07 | Concurrent fault-tolerant processing | ResultProcessor |

## Compile and run
Open a terminal in this folder. For each task, compile from its directory and run the class:

```bash
cd Task01
javac ATM.java
java ATM
```

Repeat with the corresponding folder and class name in the table. Task 1 expects a `card-reader.txt` file in the working directory for a successful withdrawal; create an empty file with `touch card-reader.txt` (Windows: `type nul > card-reader.txt`). Task 3 creates sample store input files and reports. Task 7 creates sample score files in `scores/` and measures pools of 1, 2, 4 and 8 threads.

## Expected key results
- Task 2: saves answer01.pdf, then reports `Could not store answer02.pdf` with cause `Disk quota exceeded`.
- Task 5: `Seats sold: 10`.
- Task 6: `Sum of tokens collected: 210 (expected 210)`.
- Task 7: corrupt and out-of-range score files are reported as failures while valid files are processed.

## Important submission checks
1. Replace the student-detail placeholders above.
2. Run every program on your own computer and save genuine terminal screenshots under the relevant Task folder (for example, `Task01/output.png`). Screenshots and machine-specific timing data are intentionally not fabricated here.
3. Perform the requested repeated-run experiments and add your measured results to `ANSWERS.md`.
4. Complete the individual contribution record in the report with the actual Student A / Student B names and responsibilities.
